package org.xxpay.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.MySeq;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.mapper.MchWithdrawApplyMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.model.dto.CashGroupSumAmountModel;
import org.xxpay.dal.dao.model.dto.CashSumAmountModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MchWithdrawApplyDomainService {

    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchAccountDomainService mchAccountDomainService;

    @Autowired
    private AgentAccountDomainService agentAccountDomainService;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private SystemSettingDomainService systemSettingDomainService;


    /**
     * 申请提现
     *
     * @param mchId
     * @param mchBankCard
     * @param applyAmount
     * @return
     */
    @Transactional
    public int applyCash(String mchId, MchType mchType, MchBankCard mchBankCard, Long applyAmount, String mchOrderNo) {
        /**
         * 1.从商户账户中扣除申请金额。再从余额中额外扣除提现手续费
         * 2.申请到账金额为申请金额
         */
        Assert.isTrue(StringUtils.isNotBlank(mchId), "商户号不能为空！");
        Assert.notNull(mchBankCard, "银行卡信息不能为空！");
        Assert.notNull(applyAmount, "申请金额不能为空！");
        Assert.isTrue(applyAmount >= 100, "申请金额不能为空，且必须大于100！申请值为：" + AmountUtil.convertCent2Dollar(applyAmount + ""));
        Assert.isTrue(applyAmount <= 4999900, "单笔金额不能超过49999元！");
        String cashOrderId = MySeq.getCash();
        Long actualAmount = applyAmount;
//        Long mchServiceCharge = systemSettingDomainService.getCashFee(mchType);
        Long mchServiceCharge = 0L;
        if (MchType.MERCHANT.equals(mchType)) {
            MchInfo mchInfo = mchInfoMapper.selectByPrimaryKey(mchId);
            mchServiceCharge = mchInfo.getCashDeduction();
            mchAccountDomainService.decreaseCash(mchId, mchBankCard, applyAmount, mchServiceCharge, cashOrderId);
        } else if (MchType.AGENT.equals(mchType)) {
            String agentId = mchId;
            mchServiceCharge = systemSettingDomainService.getCashFee(mchType);
            agentAccountDomainService.decreaseCash(agentId, mchBankCard, applyAmount, mchServiceCharge, cashOrderId);
        }

        MchWithdrawApply mchWithdrawApply = new MchWithdrawApply();
        mchWithdrawApply.setId(cashOrderId);
        mchWithdrawApply.setMchId(mchId);
        mchWithdrawApply.setMchType(mchType.getCode());
        mchWithdrawApply.setApplyAmount(applyAmount);
        mchWithdrawApply.setActualAmount(actualAmount);
//        mchWithdrawApply.setMchBankCardId(mchBackCardId);
        mchWithdrawApply.setNumber(mchBankCard.getNumber());
        mchWithdrawApply.setRegisteredBankName(mchBankCard.getRegisteredBankName());
        mchWithdrawApply.setBankName(mchBankCard.getBankName());
        mchWithdrawApply.setMobile(mchBankCard.getMobile());
        mchWithdrawApply.setState((byte) 0);
//        mchWithdrawApply.setPreBalance(preBalance);
//        mchWithdrawApply.setAfterBalance(afterBalance);
        mchWithdrawApply.setCreateBy(mchId);
        mchWithdrawApply.setAccountName(mchBankCard.getAccountName());
        mchWithdrawApply.setIdCard(mchBankCard.getIdCard());
        mchWithdrawApply.setProvince(mchBankCard.getProvince());
        mchWithdrawApply.setCity(mchBankCard.getCity());
        mchWithdrawApply.setPlatformDeduction(mchServiceCharge);
        mchWithdrawApply.setCashState((byte) 0);
        mchWithdrawApply.setCreateTime(new Date());
        mchWithdrawApply.setUpdateTime(new Date());
        mchWithdrawApply.setMchOrderNo(String.valueOf(System.currentTimeMillis()));
        mchWithdrawApply.setBankUnionCode(mchBankCard.getBankUnionCode());
        return mchWithdrawApplyMapper.insert(mchWithdrawApply);
    }

    /**
     * 审核
     *
     * @param id
     * @param isPass
     * @param auditor
     * @return
     */
    @Transactional
    public int audit(String id, boolean isPass, String auditor) {
        /**
         * 提现审核
         * 1.通过：
         *      1.1 抹去账户中申请中申请金额
         *      1.2 更新状态为已审核
         * 2.拒绝：
         *      2.1 抹去账户中申请中申请金额
         *      2.2 账户中增加申请金额 ，并增加资金变动记录
         *      2.3 账户中增加提现手续费金额 ,并增加资金变动记录
         *      2.4 更新状态为已拒绝
         */
        Assert.isTrue(StringUtils.isNotBlank(id), "申请id不能为空！");
        MchWithdrawApply mchWithdrawApply = mchWithdrawApplyMapper.selectByPrimaryKey(id);
        Assert.notNull(mchWithdrawApply, "申请信息不存在！id: " + id);
        Assert.isTrue(mchWithdrawApply.getState().equals((byte) 0), "该申请已被审核！");
        String mchId = mchWithdrawApply.getMchId();
        Byte mchType = mchWithdrawApply.getMchType();

        if (mchType.equals(MchType.MERCHANT.getCode())) {
            mchAccountDomainService.audit(mchId, mchWithdrawApply, isPass, auditor);
        } else if (mchType.equals(MchType.AGENT.getCode())) {
            String agentId = mchId;
            agentAccountDomainService.audit(agentId, mchWithdrawApply, isPass, auditor);
        } else {
            throw new IllegalArgumentException("无效的商户类型。mchType: " + mchType);
        }

        MchWithdrawApply updateMchWithdrawApplyItem = new MchWithdrawApply();
        if (isPass) {
            updateMchWithdrawApplyItem.setState((byte) 1);
        } else {
            updateMchWithdrawApplyItem.setState((byte) 2);
        }
        updateMchWithdrawApplyItem.setAuditBy(auditor);
        updateMchWithdrawApplyItem.setAuditTime(new Date());

        MchWithdrawApplyExample mchWithdrawApplyExample = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria mchWithdrawApplyCriteria = mchWithdrawApplyExample.createCriteria();
        mchWithdrawApplyCriteria.andIdEqualTo(id);
        mchWithdrawApplyCriteria.andStateEqualTo((byte) 0);
        int updateRows2 = mchWithdrawApplyMapper.updateByExampleSelective(updateMchWithdrawApplyItem, mchWithdrawApplyExample);
        Assert.isTrue(updateRows2 == 1, "更新申请表出错！");
        return updateRows2;
    }


    public MchWithdrawApply select(String id) {
        return mchWithdrawApplyMapper.selectByPrimaryKey(id);
    }

    public List<MchWithdrawApply> getList(int offset, int limit, MchWithdrawApply info) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchWithdrawApplyMapper.selectByExample(example);
    }

    public Integer count(MchWithdrawApply info) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchWithdrawApplyMapper.countByExample(example);
    }

    public Long sumApplyAmount(MchWithdrawApply info, List<Byte> statusIn, boolean limitToday) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        criteria.andStateIn(statusIn);
        if (limitToday) {
            Calendar calendar = Calendar.getInstance();
            Date end = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();
            criteria.andCreateTimeBetween(start, end);
        }
        return mchWithdrawApplyMapper.sumApplyAmountByExample(example);
    }

    public List<CashSumAmountModel> sumAmountByExample(MchWithdrawApply info, boolean limitToday) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        if (limitToday) {
            Calendar calendar = Calendar.getInstance();
            Date end = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();
            criteria.andCreateTimeBetween(start, end);
        }
        List<CashSumAmountModel> models = mchWithdrawApplyMapper.sumAmountByExample(example);
        return models;
    }

    public List<CashGroupSumAmountModel> groupMchIdSumAmountByExample(MchWithdrawApply info, boolean limitToday) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        if (limitToday) {
            Calendar calendar = Calendar.getInstance();
            Date end = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();
            criteria.andCreateTimeBetween(start, end);
        }
        List<CashGroupSumAmountModel> models = mchWithdrawApplyMapper.groupMchIdSumAmountByExample(example);
        return models;
    }

    void setCriteria(MchWithdrawApplyExample.Criteria criteria, MchWithdrawApply info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (info.getMchType() != null) criteria.andMchTypeEqualTo(info.getMchType());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }
}
