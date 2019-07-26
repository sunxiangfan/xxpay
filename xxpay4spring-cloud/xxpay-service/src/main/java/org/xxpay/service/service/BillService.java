package org.xxpay.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.constant.SystemSettingConstant;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.common.util.HolidayUtil;
import org.xxpay.common.util.MyTimeUtils;
import org.xxpay.dal.dao.mapper.*;
import org.xxpay.dal.dao.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BillService {
    @Autowired
    private BillMapper billMapper;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private MchPayChannelMapper mchPayChannelMapper;

    @Autowired
    private AmountFlowService amountFlowService;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private PayChannelMapper payChannelMapper;

    @Autowired
    private DepositAmountMapper depositAmountMapper;

    @Autowired
    private SystemSettingMapper systemSettingMapper;

    @Transactional
    public int createBillByOrder(PayOrder payOrder) {
        Assert.notNull(payOrder, "计算账单出错,订单信息不能为空。");
        Bill bill = new Bill();
        bill.setId(UUID.randomUUID().toString());
        bill.setPayOrderId(payOrder.getId());
        bill.setMchId(payOrder.getMchId());
        bill.setAgentMchId(payOrder.getAgentMchId());
        String mchPayChannelId = payOrder.getMchPayChannelId();
        MchPayChannel mchPayChannel = mchPayChannelMapper.selectByPrimaryKey(mchPayChannelId);
        Assert.notNull(mchPayChannel, "未找到该支付渠道。mchPayChannelId: " + mchPayChannelId);
        String payChannelId = mchPayChannel.getPayChannelId();
        bill.setPayChannelId(payChannelId);
        Long amount = payOrder.getAmount();
        bill.setAmount(payOrder.getAmount());
        bill.setPlatformActualAmount(payOrder.getPlatformActualAmount());
        bill.setMchActualAmount(payOrder.getSubMchActualAmount());
        Double thirdDeductionRate = payOrder.getThirdDeductionRate();
        bill.setThirdDeductionRate(thirdDeductionRate);
        long thirdDeduction = BigDecimalUtils.mul(amount.longValue(), thirdDeductionRate.doubleValue());
        bill.setThirdDeduction(thirdDeduction);
        Double agentCommissionRate = payOrder.getAgentMchCommissionRate();
        long agentCommission = BigDecimalUtils.mul(amount.longValue(), agentCommissionRate.doubleValue());
        bill.setAgentMchCommission(agentCommission);
        bill.setAgentMchCommissionRate(agentCommissionRate);
        Double platformDeductionRate = payOrder.getPlatformDeductionRate();
        bill.setPlatformDeductionRate(platformDeductionRate);
        long platformDeduction = BigDecimalUtils.mul(amount.longValue(), platformDeductionRate.doubleValue());
        bill.setPlatformDeduction(platformDeduction);

        //计算平台分润率 = 平台手续费率 - 三方手续费率 - 代理分润率
        double doubleTemp = BigDecimalUtils.sub(platformDeductionRate.doubleValue(), thirdDeductionRate.doubleValue());
        double platformCommissionRate = BigDecimalUtils.sub(doubleTemp, agentCommissionRate.doubleValue());
        Assert.isTrue(platformCommissionRate >= 0, "计算分润率小于0。分润率：" + platformCommissionRate);
        bill.setPlatformCommissionRate(platformCommissionRate);

        //计算平台分润金额 =  平台手续费 - 三方手续费 - 代理分润
        long longTemp = BigDecimalUtils.sub(platformDeduction, thirdDeduction);
        long platformCommission = BigDecimalUtils.sub(longTemp, agentCommission);
        Assert.isTrue(platformCommission >= 0L, "计算分润金额小于0。分润金额：" + platformCommission);
        bill.setPlatformCommission(platformCommission);

        bill.setPayOrderTime(payOrder.getCreateTime());
        return billMapper.insertSelective(bill);
    }

    @Transactional
    public void computeAmountFlowById(PayOrder payOrder) {
        //为商户添加支付的调增
        String payOrderId = payOrder.getId();
        BillExample billExample = new BillExample();
        billExample.setLimit(1);
        BillExample.Criteria billCriteria = billExample.createCriteria();
        billCriteria.andPayOrderIdEqualTo(payOrderId);
        List<Bill> lists = billMapper.selectByExample(billExample);
        Assert.isTrue(lists.size() == 1, "未找到账单数据。payOrderId: " + payOrderId);
        Bill bill = lists.get(0);
        Assert.isTrue(bill.getState() == 0, "该账单已更新。payOrderId: " + payOrderId);
        String mchId = bill.getMchId();
        MchInfo mchInfo = mchInfoMapper.selectByPrimaryKey(mchId);
        AmountFlow mchIncrease = new AmountFlow();
        Long opAmount = bill.getAmount();
        mchIncrease.setAmount(opAmount);
        mchIncrease.setPreBalance(mchInfo.getBalance());
        mchIncrease.setMchId(mchId);
        mchIncrease.setId(UUID.randomUUID().toString());
        mchIncrease.setSourceId(payOrderId);
        MchInfo updateMchInfo = new MchInfo();
        updateMchInfo.setVersion(mchInfo.getVersion() + 1);
        long afterBalance = BigDecimalUtils.add(mchInfo.getBalance().longValue(), opAmount.longValue());
        updateMchInfo.setBalance(afterBalance);
        MchInfoExample mchInfoExample = new MchInfoExample();
        MchInfoExample.Criteria mchCriteria = mchInfoExample.createCriteria();
        mchCriteria.andIdEqualTo(mchId);
        mchCriteria.andVersionEqualTo(mchInfo.getVersion());
        int updateRows = mchInfoMapper.updateByExampleSelective(updateMchInfo, mchInfoExample);
        Assert.isTrue(updateRows == 1, "更新商户余额条目出错。updateRows: " + updateRows);
        updateRows = amountFlowService.insertIncrease(mchIncrease, AmountType.TRANSACTION_AMOUNT);
        Assert.isTrue(updateRows == 1, "添加商户调增记录出错。updateRows: " + updateRows);

        //为商户添加手续费的调减
        mchInfo = mchInfoMapper.selectByPrimaryKey(mchId);
        opAmount = -bill.getPlatformDeduction();
        AmountFlow mchDecrease = new AmountFlow();
        mchDecrease.setAmount(opAmount);
        mchDecrease.setPreBalance(mchInfo.getBalance());
        mchDecrease.setMchId(mchId);
        mchDecrease.setId(UUID.randomUUID().toString());
        mchDecrease.setSourceId(payOrderId);
        updateMchInfo = new MchInfo();
        updateMchInfo.setVersion(mchInfo.getVersion() + 1);
        afterBalance = BigDecimalUtils.add(mchInfo.getBalance().longValue(), opAmount.longValue());
        updateMchInfo.setBalance(afterBalance);
        mchInfoExample = new MchInfoExample();
        mchCriteria = mchInfoExample.createCriteria();
        mchCriteria.andIdEqualTo(mchId);
        mchCriteria.andVersionEqualTo(mchInfo.getVersion());
        updateRows = mchInfoMapper.updateByExampleSelective(updateMchInfo, mchInfoExample);
        Assert.isTrue(updateRows == 1, "更新商户余额条目出错。updateRows: " + updateRows);
        updateRows = amountFlowService.insertDecrease(mchDecrease, AmountType.TRANSACTION_FEE);
        Assert.isTrue(updateRows == 1, "添加商户调减记录出错。updateRows: " + updateRows);

        //为代理商添加分成的调增
        String agentId = bill.getAgentMchId();
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        opAmount = bill.getAgentMchCommission();
        if (opAmount != null && opAmount.compareTo(0L) > 0) {
            mchIncrease = new AmountFlow();
            mchIncrease.setAmount(opAmount);
            mchIncrease.setPreBalance(agent.getBalance());
            mchIncrease.setMchId(agentId);
            mchIncrease.setId(UUID.randomUUID().toString());
            mchIncrease.setSourceId(payOrderId);
            Agent updateAgent = new Agent();
            updateAgent.setVersion(agent.getVersion() + 1);
            afterBalance = BigDecimalUtils.add(agent.getBalance().longValue(), opAmount.longValue());
            updateAgent.setBalance(afterBalance);

            AgentExample agentExample = new AgentExample();
            AgentExample.Criteria agentCriteria = agentExample.createCriteria();
            agentCriteria.andIdEqualTo(agentId);
            agentCriteria.andVersionEqualTo(agent.getVersion());
            updateRows = agentMapper.updateByExampleSelective(updateAgent, agentExample);
            Assert.isTrue(updateRows == 1, "更新代理商户余额条目出错。updateRows: " + updateRows);
            updateRows = amountFlowService.insertIncrease(mchIncrease, AmountType.AGENT_COMMISSION);
            Assert.isTrue(updateRows == 1, "添加代理商户调增记录出错。updateRows: " + updateRows);
        }

        //添加商户的保证金
        String payChannelId = payOrder.getPayChannelId();
        PayChannel payChannel = payChannelMapper.selectByPrimaryKey(payChannelId);
        Assert.notNull(payChannel, "查询支付渠道为空。payChannelId: " + payChannelId);
        Double t1Rate = payChannel.getT1Rate();
        Assert.notNull(t1Rate, "T1比例不能为空！payChannelId: " + payChannelId);
        if (t1Rate > 0) {
            Long mchActualAmount = bill.getMchActualAmount();
            Assert.notNull(mchActualAmount, "商户实际到账金额不能为空！payOrderId: " + payOrderId);
            //保证金
            long mchDepositAmount = BigDecimalUtils.mul(mchActualAmount.longValue(), t1Rate.doubleValue());
            String strT1Time = payChannel.getT1Time();
            Assert.notNull(strT1Time, "t1Time 不能为空！payChannelId: " + payChannelId);
            boolean t1TimeTestValid = MyTimeUtils.testIsHourAndMinuteString(strT1Time);
            Assert.isTrue(t1TimeTestValid, "t1Time is error. payChannelId: " + payChannelId);

            SystemSetting legalHoliday = systemSettingMapper.selectByName(SystemSettingConstant.KEY_LEGAL_HOLIDAY);
            Assert.notNull(legalHoliday, "系统参数错误。缺少配置项：" + SystemSettingConstant.KEY_LEGAL_HOLIDAY);
            String jsonLegalHoliday = legalHoliday.getParamValue();
            JSONObject objLegalHoliday = JSON.parseObject(jsonLegalHoliday);

            SystemSetting legalWeekday = systemSettingMapper.selectByName(SystemSettingConstant.KEY_LEGAL_WEEKDAY);
            Assert.notNull(legalWeekday, "系统参数错误。缺少配置项：" + SystemSettingConstant.KEY_LEGAL_WEEKDAY);
            String jsonLegalWeekday = legalWeekday.getParamValue();
            JSONObject objLegalWeekday = JSON.parseObject(jsonLegalWeekday);
            Date payOrderTime = payOrder.getCreateTime();
            Date nextWeekday = HolidayUtil.getNextWeekday(payOrderTime, objLegalHoliday, objLegalWeekday);

            MyTimeUtils.HourAndMinute hourAndMinute = MyTimeUtils.convert2HourAndMinute(strT1Time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nextWeekday);
            calendar.set(Calendar.HOUR_OF_DAY, hourAndMinute.getHour().intValue());
            calendar.set(Calendar.MINUTE, hourAndMinute.getMinute().intValue());
            calendar.set(Calendar.SECOND, 0);
            Date planUnlockTime = calendar.getTime();

            DepositAmount mchDepositAmountEntity = new DepositAmount();
            mchDepositAmountEntity.setId(UUID.randomUUID().toString());
            mchDepositAmountEntity.setAmount(mchDepositAmount);
            mchDepositAmountEntity.setMchId(bill.getMchId());
            mchDepositAmountEntity.setMchType(MchType.MERCHANT.getCode());
            mchDepositAmountEntity.setCreateTime(new Date());
            mchDepositAmountEntity.setPlanUnlockTime(planUnlockTime);
            mchDepositAmountEntity.setPayOrderId(payOrderId);
            mchDepositAmountEntity.setState((byte) 0);
//            mchDepositAmount.setRemark("T1");
            updateRows = depositAmountMapper.insertSelective(mchDepositAmountEntity);
            Assert.isTrue(updateRows == 1, "添加商户保证金数据失败。updateRows: " + updateRows);

            //添加代理的保证金
            Long agentCommission = bill.getAgentMchCommission();
            if (agentCommission != null && agentCommission.longValue() > 0) {
                //保证金
                long agentDepositAmount = BigDecimalUtils.mul(agentCommission.longValue(), t1Rate.doubleValue());
                DepositAmount agentDepositAmountEntity = new DepositAmount();
                agentDepositAmountEntity.setId(UUID.randomUUID().toString());
                agentDepositAmountEntity.setAmount(agentDepositAmount);
                agentDepositAmountEntity.setMchId(bill.getAgentMchId());
                agentDepositAmountEntity.setMchType(MchType.AGENT.getCode());
                agentDepositAmountEntity.setCreateTime(new Date());
                agentDepositAmountEntity.setPlanUnlockTime(planUnlockTime);
                agentDepositAmountEntity.setPayOrderId(payOrderId);
                agentDepositAmountEntity.setState((byte) 0);
                updateRows = depositAmountMapper.insertSelective(agentDepositAmountEntity);
                Assert.isTrue(updateRows == 1, "添加代理保证金数据失败。updateRows: " + updateRows);
            }
        }
        //设置Bill的状态
        Bill billUpdate = new Bill();
        billUpdate.setState((byte) 1);
        billUpdate.setUpdateTime(new Date());
        billExample = new BillExample();
        billCriteria = billExample.createCriteria();
        billCriteria.andIdEqualTo(bill.getId());
        billCriteria.andStateEqualTo((byte) 0);
        updateRows = billMapper.updateByExampleSelective(billUpdate, billExample);
        Assert.isTrue(updateRows == 1, "更新账单表状态出错。billId: " + bill.getId());
    }
}
