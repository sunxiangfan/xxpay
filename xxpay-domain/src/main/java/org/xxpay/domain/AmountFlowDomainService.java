package org.xxpay.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.AmountFlowMapper;
import org.xxpay.dal.dao.model.AmountFlow;
import org.xxpay.dal.dao.model.AmountFlowExample;

import java.util.List;
import java.util.UUID;

/**
 * Created by tanghaibo on 19/3/10.
 */
@Component
public class AmountFlowDomainService {


    @Autowired
    private AmountFlowMapper amountFlowMapper;

    @Transactional
    public int lockAmount(String mchId, MchType mchType, Long amount, Long preBalance, String remark) {
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setMchId(mchId);
        amountFlow.setAmount(-amount);
        amountFlow.setRemark(remark);
        amountFlow.setPreBalance(preBalance);
        int updateRows = this.insertDecrease(amountFlow, AmountType.LOCK_AMOUNT);
        Assert.isTrue(updateRows == 1, "保存失败！");
        return updateRows;
    }

    @Transactional
    public int unlockAmount(String mchId, MchType mchType, Long amount, Long preBalance, String remark) {
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setMchId(mchId);
        amountFlow.setAmount(amount);
        amountFlow.setRemark(remark);
        amountFlow.setPreBalance(preBalance);
        int updateRows = this.insertIncrease(amountFlow, AmountType.UNLOCK_AMOUNT);
        Assert.isTrue(updateRows == 1, "保存失败！");
        return updateRows;
    }

    @Transactional
    public int insertIncrease(AmountFlow amountFlow, AmountType amountType) {
        Assert.notNull(amountFlow, "商户账户金额变动表数据不能为空！");
        Assert.isTrue(StringUtils.isNotBlank(amountFlow.getMchId()), "商户号不能为空！");
        Assert.notNull(amountFlow.getAmount(), "调增金额不能为空！");
        Assert.notNull(amountFlow.getPreBalance(), "调增前账户余额不能为空！");
        Assert.notNull(amountType, "金额变动类型不能为空！");
        AmountFlowType amountFlowType = amountType.getFlowType();
        Assert.isTrue(amountFlowType.equals(AmountFlowType.IN), "调增类型不匹配！传入类型为：" + amountType);
        Assert.isTrue(amountFlow.getAmount().compareTo(0L) > 0, "调增金额不能小于等于0。传入金额为：" + amountFlow.getAmount());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setFlowType(amountFlowType.getCode().byteValue());
        amountFlow.setAmountType(amountType.getCode());
        return amountFlowMapper.insertSelective(amountFlow);
    }

    @Transactional
    public int insertDecrease(AmountFlow amountFlow, AmountType amountType) {
        Assert.notNull(amountFlow, "商户账户金额变动表数据不能为空！");
        Assert.isTrue(StringUtils.isNotBlank(amountFlow.getMchId()), "商户号不能为空！");
        Assert.notNull(amountFlow.getAmount(), "调减金额不能为空！");
        Assert.notNull(amountFlow.getPreBalance(), "调减前账户余额不能为空！");
        Assert.notNull(amountType, "金额变动类型不能为空！");
        AmountFlowType amountFlowType = amountType.getFlowType();
        Assert.isTrue(amountFlowType.equals(AmountFlowType.OUT), "调减类型不匹配！传入类型为：" + amountType);
        long afterDecreaseAmount = BigDecimalUtils.add(amountFlow.getAmount().longValue(), amountFlow.getPreBalance().longValue());
        Assert.isTrue(afterDecreaseAmount >= 0, "调减金额不能大于余额。");
        Assert.isTrue(amountFlow.getAmount().compareTo(0L) < 0, "调减金额不能大于等于0。传入金额为：" + amountFlow.getAmount());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setFlowType(amountFlowType.getCode().byteValue());
        amountFlow.setAmountType(amountType.getCode());
        return amountFlowMapper.insertSelective(amountFlow);
    }

    /**
     * 提现申请。添加提现扣除记录 和 提现手续费记录
     *
     * @param mchId
     * @param preBalance
     * @param applyAmount
     * @param mchServiceCharge
     * @param cashOrderId
     */
    @Transactional
    public void decreaseCash(String mchId, Long preBalance, Long applyAmount, Long mchServiceCharge, String cashOrderId) {
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setAmount(-applyAmount);
        amountFlow.setMchId(mchId);
        amountFlow.setFlowType(AmountFlowType.OUT.getCode());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setRemark("提现申请 单号：" + cashOrderId);
        amountFlow.setSourceId(cashOrderId);
        amountFlow.setPreBalance(preBalance);
        int updateRows = this.insertDecrease(amountFlow, AmountType.MCH_CASH);
        Assert.isTrue(updateRows > 0, "添加提现扣除资金变动记录失败！updateRows: " + updateRows);
        AmountFlow amountFlowFee = new AmountFlow();
        amountFlowFee.setAmount(-mchServiceCharge);
        amountFlowFee.setMchId(mchId);
        amountFlowFee.setFlowType(AmountFlowType.OUT.getCode());
        amountFlowFee.setId(UUID.randomUUID().toString());
        amountFlowFee.setRemark("提现手续费 单号：" + cashOrderId);
        amountFlowFee.setSourceId(cashOrderId);
        amountFlowFee.setPreBalance(BigDecimalUtils.sub(preBalance.longValue(), applyAmount.longValue()));
        updateRows = this.insertDecrease(amountFlowFee, AmountType.CASH_FEE);
        Assert.isTrue(updateRows > 0, "添加手续费资金变动记录失败！updateRows: " + updateRows);

    }

    /**
     * 提现被驳回。添加返还扣除记录 和 返还手续费记录
     * @param mchId
     * @param preBalance
     * @param applyAmount
     * @param mchServiceCharge
     * @param cashOrderId
     */
    @Transactional
    public void rejectCashIncrease(String mchId, Long preBalance, Long applyAmount, Long mchServiceCharge,String cashOrderId) {
        /**
         *
         *      1 账户中增加申请金额 ，并增加资金变动记录
         *      2 账户中增加提现手续费金额 ,并增加资金变动记录
         */
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setAmount(applyAmount);
        amountFlow.setMchId(mchId);
        amountFlow.setFlowType(AmountFlowType.IN.getCode());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setRemark("提现拒绝。返还提现金额 单号：" + cashOrderId);
        amountFlow.setSourceId(cashOrderId);
        amountFlow.setPreBalance(preBalance);
        int updateRows = this.insertIncrease(amountFlow, AmountType.RE_CASH);
        Assert.isTrue(updateRows > 0, "添加返还提现金额记录失败。updateRows: " + updateRows);

        AmountFlow amountFlowFee = new AmountFlow();
        amountFlowFee.setAmount(mchServiceCharge);
        amountFlowFee.setMchId(mchId);
        amountFlowFee.setFlowType(AmountFlowType.IN.getCode());
        amountFlowFee.setId(UUID.randomUUID().toString());
        amountFlowFee.setRemark("提现拒绝。返还提现手续费 单号：" + cashOrderId);
        amountFlowFee.setSourceId(cashOrderId);
        amountFlowFee.setPreBalance(BigDecimalUtils.add(preBalance.longValue(),applyAmount.longValue()));
        updateRows = this.insertIncrease(amountFlowFee, AmountType.RE_CASH_FEE);
        Assert.isTrue(updateRows > 0, "添加返还提现手续费记录失败。updateRows: " + updateRows);
    }

    public List<AmountFlow> getList(int offset, int limit, AmountFlow info) {
        AmountFlowExample example = new AmountFlowExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AmountFlowExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return amountFlowMapper.selectByExample(example);
    }

    public Integer count(AmountFlow info) {
        AmountFlowExample example = new AmountFlowExample();
        AmountFlowExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return amountFlowMapper.countByExample(example);
    }

    void setCriteria(AmountFlowExample.Criteria criteria, AmountFlow info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (info.getFlowType() != null && info.getFlowType() != -99)
                criteria.andFlowTypeEqualTo(info.getFlowType());
            if (StringUtils.isNotBlank(info.getAmountType())) criteria.andAmountTypeEqualTo(info.getAmountType());
        }
    }

}
