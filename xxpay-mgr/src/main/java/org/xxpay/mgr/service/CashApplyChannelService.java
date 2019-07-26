package org.xxpay.mgr.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.xxpay.common.constant.CashConstant;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.Bank;
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.mgr.controller.AgentAccountController;
import org.xxpay.mgr.service.channel.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CashApplyChannelService {
    @Autowired
    private MchWithdrawHistoryService mchWithdrawHistoryService;

    @Autowired
    private  BankService bankService;

    private final static MyLog _log = MyLog.getLog(CashApplyChannelService.class);

    @Transactional
    public void apply(MchWithdrawApply apply, CashChannel channel) throws UnsupportedEncodingException, IllegalAccessException, IOException {
        Assert.isTrue(apply.getState().equals((byte) 1), "该申请单必须先审核通过！");
        Assert.isTrue(apply.getCashState().equals((byte) 0), "该申请单已经申请过代付了！");
        Long applyAmount = apply.getApplyAmount();
        Long actualAmount = apply.getActualAmount();
        Long thirdDeduction = channel.getThirdDeduction();
//        long actualApplyAmount = BigDecimalUtils.add(thirdDeduction.longValue(), actualAmount.longValue());
//        Assert.isTrue(applyAmount.compareTo(actualApplyAmount) >= 0, "配置手续费有误！请检查提现手续费");
        long actualApplyAmount = applyAmount;
        Long min = channel.getMinTransactionAmount();
        Assert.isTrue(min.compareTo(actualApplyAmount) <= 0, "该平台的最小申请金额为" + AmountUtil.convertCent2Dollar(min + "") + " ,本次实际发放金额为：" + AmountUtil.convertCent2Dollar(actualApplyAmount + ""));
        Long max = channel.getMaxTransactionAmount();
        Assert.isTrue(max.compareTo(actualApplyAmount) >= 0, "该平台的最大申请金额为" + AmountUtil.convertCent2Dollar(max + "") + " ,本次实际发放金额为：" + AmountUtil.convertCent2Dollar(actualApplyAmount + ""));

        switch (channel.getName()) {
            case PayConstant
                    .CHANNEL_NAME_THREE10000: {
                ChannelThree10000 channelThree10000 = new ChannelThree10000();
                JSONObject result = channelThree10000.applyThree10000(apply, actualApplyAmount);
                boolean state = result.getBooleanValue("state");
                String applyId = apply.getId();
                String channelOrderNo = result.getString("channelOrderNo");
                String cashChannelId = channel.getId();
                if (state) {
                    mchWithdrawHistoryService.updateCashStatus4Apply(applyId, cashChannelId, actualApplyAmount, thirdDeduction, channelOrderNo);
                } else {
                    throw new IllegalArgumentException("上游失败。消息：" + result.getString("msg"));
                }
            }
            break;
            case PayConstant
                    .CHANNEL_NAME_O9N: {
                ChannelO9N channelO9N = new ChannelO9N();
                JSONObject result = channelO9N.apply(apply, actualApplyAmount);
                boolean state = result.getBooleanValue("state");
                String applyId = apply.getId();
                String channelOrderNo = result.getString("channelOrderNo");
                String cashChannelId = channel.getId();
                if (state) {
                    mchWithdrawHistoryService.updateCashStatus4Apply(applyId, cashChannelId, actualApplyAmount, thirdDeduction, channelOrderNo);
                } else {
                    throw new IllegalArgumentException("上游失败。消息：" + result.getString("msg"));
                }
            }
            break;
            case PayConstant.CHANNEL_NAME_O9N2: {
                ChannelO9N2 channelO9N2 = new ChannelO9N2();
                JSONObject result = channelO9N2.apply(apply, actualApplyAmount);
                boolean state = result.getBooleanValue("state");
                String applyId = apply.getId();
                String channelOrderNo = result.getString("channelOrderNo");
                String cashChannelId = channel.getId();
                if (state) {
                    mchWithdrawHistoryService.updateCashStatus4Apply(applyId, cashChannelId, actualApplyAmount, thirdDeduction, channelOrderNo);
                } else {
                    throw new IllegalArgumentException("上游失败。消息：" + result.getString("msg"));
                }

            }
            break;
            case PayConstant.CHANNEL_NAME_DS: {
                String bankName=apply.getBankName();
                Bank bank= bankService.getByName(bankName);
                Assert.notNull(bank,"暂不支持的银行。名称："+bankName);
                String bankCode=bank.getCode();

                ChannelDS channelDS = new ChannelDS();
                JSONObject result = channelDS.apply(apply, channel,bankCode);
                boolean state = result.getBooleanValue("state");
                String applyId = apply.getId();
                String channelOrderNo = result.getString("channelOrderNo");
                String cashChannelId = channel.getId();
                if (state) {
                    mchWithdrawHistoryService.updateCashStatus4Apply(applyId, cashChannelId, actualApplyAmount, thirdDeduction, channelOrderNo);
                } else {
                    throw new IllegalArgumentException("上游失败。消息：" + result.getString("msg"));
                }
            }
            break;
            case  PayConstant.CHANNEL_NAME_MZWYQ:{
                ChannelMzwyq channelMzwyq = new ChannelMzwyq();
                actualAmount = BigDecimalUtils.add(thirdDeduction, applyAmount);
                JSONObject result = channelMzwyq.applyThree10000(apply, actualAmount.longValue());
                boolean state = result.getBooleanValue("state");
                String applyId = apply.getId();
                String channelOrderNo = result.getString("channelOrderNo");
                String cashChannelId = channel.getId();
                if (state) {
                    mchWithdrawHistoryService.updateCashStatus4Apply(applyId, cashChannelId, actualApplyAmount, thirdDeduction, channelOrderNo);
                } else {
                    throw new IllegalArgumentException("上游失败。消息：" + result.getString("msg"));
                }
            }
            break;
            case PayConstant.CHANNEL_NAME_MFDF: {//敏付
                ChannelMinFu minFu = new ChannelMinFu();
                JSONObject result = minFu.doMinFuTransferReq(apply, actualApplyAmount);
                boolean state = result.getBooleanValue("state");
                String applyId = apply.getId();
                String channelOrderNo = result.getString("channelOrderNo");
                String cashChannelId = channel.getId();
                if (state) {
                    mchWithdrawHistoryService.updateCashStatus4Apply(applyId, cashChannelId, actualApplyAmount, thirdDeduction, channelOrderNo);
                } else {
                    throw new IllegalArgumentException("上游失败。消息：" + result.getString("msg"));
                }
            }
            break;
            default:
                throw new IllegalArgumentException("无效的代付渠道");

        }

    }

    /**
     * 标记为已打款
     * @param apply
     * @param channel
     */
    @Transactional
    public void markCashed(MchWithdrawApply apply,CashChannel channel){
        Assert.isTrue(apply.getState().equals((byte) 1), "该申请单必须先审核通过！");
        Assert.isTrue(apply.getCashState().equals((byte) 0), "该申请单已经申请过代付了！");
        mchWithdrawHistoryService.updateCashStatus4Apply(apply.getId(),channel.getId(),apply.getApplyAmount(),channel.getThirdDeduction(),null);
        mchWithdrawHistoryService.updateCashStatus4Success(apply.getId(),new Date());
    }

}
