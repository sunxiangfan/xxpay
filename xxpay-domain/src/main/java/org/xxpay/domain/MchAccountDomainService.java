package org.xxpay.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.enumm.MchState;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.*;
import org.xxpay.dal.dao.model.*;

import java.util.*;

@Service
public class MchAccountDomainService {
    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private PayOrderDomainService payOrderDomainService;

    @Autowired
    private MchWithdrawApplyDomainService mchWithdrawApplyDomainService;


    @Autowired
    private AmountFlowDomainService amountFlowDomainService;

    @Autowired
    private DepositAmountDomainService depositAmountDomainService;

    @Transactional
    public int decreaseCash(String mchId, MchBankCard mchBankCard, Long applyAmount, long mchServiceCharge, String cashOrderId) {
        /**
         * 1.从商户账户中扣除申请金额。再从余额中额外扣除提现手续费
         * 2.申请到账金额为申请金额
         */
        Assert.isTrue(StringUtils.isNotBlank(mchId), "商户号不能为空！");
        Assert.notNull(mchBankCard, "银行卡信息不能为空！");
        Assert.notNull(applyAmount, "申请金额不能为空！");
        Assert.isTrue(applyAmount >= 100, "申请金额不能为空，且必须大于100！申请值为：" + AmountUtil.convertCent2Dollar(applyAmount + ""));
        MchInfo preMchInfo = mchInfoMapper.selectByPrimaryKey(mchId);
        Assert.notNull(preMchInfo, "商户信息不存在！mchId: " + mchId);
        Assert.isTrue(preMchInfo.getState().equals(MchState.ENABLE.getCode()), "商户已被停用！mchId: " + mchId);
        Long preBalance = preMchInfo.getBalance();
        Assert.notNull(preBalance, "商户余额为空！mchId: " + mchId);
        Assert.isTrue(applyAmount.compareTo(preBalance) <= 0, "余额不足！余额：" + AmountUtil.convertCent2Dollar(preBalance + ""));

        Assert.isTrue(applyAmount.compareTo(mchServiceCharge) > 0, "提现金额必须大于提现手续费！手续费：" + AmountUtil.convertCent2Dollar(mchServiceCharge + "") + "元");
        long totalDecreaseAmount = BigDecimalUtils.add(applyAmount.longValue(), mchServiceCharge);
        Assert.isTrue(preBalance.compareTo(totalDecreaseAmount) >= 0, "余额不足！余额：" + AmountUtil.convertCent2Dollar(preBalance + "") + ",本次需扣除：" + AmountUtil.convertCent2Dollar(totalDecreaseAmount + ""));

//        //验证D0比例
//        PayOrder payOrderCondition = new PayOrder();
//        payOrderCondition.setMchId(mchId);
//        List<Byte> successStatus = new ArrayList<>();
//        successStatus.add(PayConstant.PAY_STATUS_SUCCESS);
//        successStatus.add(PayConstant.PAY_STATUS_COMPLETE);
//        //今日到账金额
//        Long todayAmount = payOrderDomainService.sumMchActualAmount(payOrderCondition, successStatus, true);
//        todayAmount = todayAmount == null ? 0 : todayAmount;
//        //今日有效提现金额
//        MchWithdrawApply cashCondition = new MchWithdrawApply();
//        cashCondition.setMchId(mchId);
//
//        List<Byte> cashSuccessStatus = new ArrayList<>();
//        cashSuccessStatus.add((byte) 0);
//        cashSuccessStatus.add((byte) 1);
//        //今天已提现金额
//        Long todayCashed = mchWithdrawApplyDomainService.sumApplyAmount(cashCondition, cashSuccessStatus, true);
//        todayCashed = todayCashed == null ? 0 : todayCashed;
//        //可用余额：总余额- 保证金 -D1部分
//        //保证金
//        Long depositAmount = preMchInfo.getDeposit();
//        depositAmount = depositAmount == null ? 0 : depositAmount;
//        Double d0Rate = preMchInfo.getD0Rate();
//        Double d1Rate = BigDecimalUtils.sub(1.0, d0Rate.doubleValue());
//        Long d1Amount = BigDecimalUtils.mul(todayAmount.longValue(), d1Rate.doubleValue());
////        Long enableUseAmount = preBalance.longValue() - depositAmount.longValue() - d1Amount.longValue();
////        Long todayCashing = BigDecimalUtils.add(todayCashed.longValue(), applyAmount.longValue());
////        Assert.isTrue(enableUseAmount.compareTo(todayCashing) >= 0, "超出今日可提现金额！今天上限：" + AmountUtil.convertCent2Dollar(enableUseAmount + "") + " , 今日已提现：" + AmountUtil.convertCent2Dollar(todayCashed + ""));
        //保证 交易后余额大于等于D1部分
        Long depositAmount = depositAmountDomainService.sumLockedAmountByMchId(mchId);
        Long afterBalance = preBalance - totalDecreaseAmount;
        Assert.isTrue(afterBalance.compareTo(depositAmount) >= 0, "可提现金额不足！本次需扣除" + AmountUtil.convertCent2Dollar(totalDecreaseAmount + "") + "元");

        Long actualAmount = applyAmount;
        Long preCashingAmount = preMchInfo.getCashingAmount();
        Long afterCashingAmount = applyAmount;
        if (preCashingAmount != null && preCashingAmount.longValue() > 0) {
            afterCashingAmount = BigDecimalUtils.add(preCashingAmount.longValue(), applyAmount.longValue());
        }

        Integer preVersion = preMchInfo.getVersion();
        Integer afterVersion = preVersion + 1;
        MchInfo updateMchInfoItem = new MchInfo();
        updateMchInfoItem.setId(preMchInfo.getId());
        updateMchInfoItem.setVersion(afterVersion);
        updateMchInfoItem.setBalance(afterBalance);
        updateMchInfoItem.setCashingAmount(afterCashingAmount);

        MchInfoExample mchInfoExample = new MchInfoExample();
        MchInfoExample.Criteria mchInfoCriteria = mchInfoExample.createCriteria();
        mchInfoCriteria.andIdEqualTo(mchId);
        mchInfoCriteria.andVersionEqualTo(preVersion);
        mchInfoCriteria.andBalanceEqualTo(preBalance);
        int updateRows = mchInfoMapper.updateByExampleSelective(updateMchInfoItem, mchInfoExample);
        Assert.isTrue(updateRows > 0, "更新商户账户失败。updateRows: " + updateRows);

        amountFlowDomainService.decreaseCash(mchId, preBalance, applyAmount, mchServiceCharge, cashOrderId);
        return 1;
    }

    /**
     * 提现审核
     *
     * @param mchId
     * @param apply
     * @param isPass
     * @param auditor
     * @return
     */
    @Transactional
    public int audit(String mchId, MchWithdrawApply apply, boolean isPass, String auditor) {
        /**
         * 提现审核
         * 1.通过：
         *      1.1 抹去账户中申请中申请金额
         * 2.拒绝：
         *      2.1 抹去账户中申请中申请金额
         *      2.2 账户中增加申请金额 ，并增加资金变动记录
         *      2.3 账户中增加提现手续费金额 ,并增加资金变动记录
         */
        Long applyAmount = apply.getApplyAmount();
        MchInfo preMchInfo = mchInfoMapper.selectByPrimaryKey(mchId);
        Assert.notNull(preMchInfo, "商户信息不存在！mchId: " + mchId);
        Assert.isTrue(preMchInfo.getState().equals(MchState.ENABLE.getCode()), "商户已被停用！mchId: " + mchId);
        Long preCashingAmount = preMchInfo.getCashingAmount();
        Assert.isTrue(preCashingAmount.compareTo(applyAmount) >= 0, "商户累计待审批金额有误。待审批金额为：" + AmountUtil.convertCent2Dollar(preCashingAmount + "") + " 不能小于本次申请提现金额： " + AmountUtil.convertCent2Dollar(applyAmount + ""));
        Long afterCashingAmount = BigDecimalUtils.sub(preCashingAmount, applyAmount);

        Long cashFee = apply.getPlatformDeduction();
        Integer preVersion = preMchInfo.getVersion();
        Integer afterVersion = preVersion + 1;
        MchInfo updateMchInfoItem = new MchInfo();
        updateMchInfoItem.setVersion(afterVersion);
        updateMchInfoItem.setCashingAmount(afterCashingAmount);
        if (!isPass) {
            Long preBalance = preMchInfo.getBalance();
            if (preBalance == null || preBalance.longValue() < 0) {
                preBalance = 0L;
            }
            long totalDeduction = BigDecimalUtils.add(applyAmount.longValue(), cashFee.longValue());
            Long afterBalance = BigDecimalUtils.add(preBalance.longValue(), totalDeduction);
            updateMchInfoItem.setBalance(afterBalance);
        }

        MchInfoExample mchInfoExample = new MchInfoExample();
        MchInfoExample.Criteria mchInfoCriteria = mchInfoExample.createCriteria();
        mchInfoCriteria.andIdEqualTo(mchId);
        mchInfoCriteria.andVersionEqualTo(preVersion);
        int updateRows = mchInfoMapper.updateByExampleSelective(updateMchInfoItem, mchInfoExample);
        Assert.isTrue(updateRows > 0, "更新账户余额表出错。updateRows: " + updateRows);

        if (!isPass) {
            //拒绝时，添加返还申请金额，手续费的变动记录
            String cashOrderId = apply.getId();
            Long preBalance = preMchInfo.getBalance();
            Long mchServiceCharge = apply.getPlatformDeduction();
            amountFlowDomainService.rejectCashIncrease(mchId, preBalance, applyAmount, mchServiceCharge, cashOrderId);
        }
        return 1;

    }


}
