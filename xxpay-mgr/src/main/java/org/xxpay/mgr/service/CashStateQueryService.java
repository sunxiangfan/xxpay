package org.xxpay.mgr.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.common.constant.CashConstant;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.mapper.CashChannelMapper;
import org.xxpay.dal.dao.mapper.MchWithdrawApplyMapper;
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.model.MchWithdrawApplyExample;
import org.xxpay.mgr.service.channel.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CashStateQueryService {
    private final static MyLog _log = MyLog.getLog(CashStateQueryService.class);
    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchWithdrawHistoryService mchWithdrawHistoryService;

    @Autowired
    private CashChannelMapper cashChannelMapper;

    public void query() {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        example.setLimit(10);
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        criteria.andCashStateEqualTo(CashConstant.CASH_STATUS_APPLY);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -5);
        Date startTime = calendar.getTime();
        criteria.andCreateTimeGreaterThan(startTime);
        List<MchWithdrawApply> applyList = mchWithdrawApplyMapper.selectByExample(example);
        for (MchWithdrawApply apply : applyList) {
            String cashOrderId = apply.getId();
            String cashChannelId = apply.getCashChannelId();
            if (StringUtils.isBlank(cashChannelId)) {
                continue;
            }
            try {
                CashChannel cashChannel = cashChannelMapper.selectByPrimaryKey(cashChannelId);
                String channelName = cashChannel.getName();
                switch (channelName) {
                    case PayConstant.CHANNEL_NAME_THREE10000: {
                        ChannelThree10000 channelThree10000 = new ChannelThree10000();
                        JSONObject result = channelThree10000.queryCashState(cashOrderId);
                        byte state = result.getByte("state");
                        if (state == CashConstant.CASH_STATUS_APPLY) {
                            continue;
                        }
                        if (state == CashConstant.CASH_STATUS_SUCCESS) {
                            Date cashSuccTime = result.getDate("cashSuccTime");
                            mchWithdrawHistoryService.updateCashStatus4Success(cashOrderId, cashSuccTime);
                        }
                        if (state == CashConstant.CASH_STATUS_FAIL) {
                            mchWithdrawHistoryService.updateCashStatus4Fail(cashOrderId);
                            _log.error("提现申请被拒绝。单号：" + cashOrderId);
                        }
                    }
                    break;
                    case PayConstant.CHANNEL_NAME_O9N: {
                        ChannelO9N channelO9N = new ChannelO9N();
                        JSONObject result = channelO9N.queryCashState(cashOrderId);
                        byte state = result.getByte("state");
                        if (state == CashConstant.CASH_STATUS_APPLY) {
                            continue;
                        }
                        if (state == CashConstant.CASH_STATUS_SUCCESS) {
                            Date cashSuccTime = result.getDate("cashSuccTime");
                            mchWithdrawHistoryService.updateCashStatus4Success(cashOrderId, cashSuccTime);
                        }
                        if (state == CashConstant.CASH_STATUS_FAIL) {
                            mchWithdrawHistoryService.updateCashStatus4Fail(cashOrderId);
                            _log.error("提现申请被拒绝。单号：" + cashOrderId);
                        }
                    }
                    break;
                    case PayConstant.CHANNEL_NAME_O9N2: {
                        ChannelO9N2 channelO9N2 = new ChannelO9N2();
                        JSONObject result = channelO9N2.queryCashState(cashOrderId);
                        byte state = result.getByte("state");
                        if (state == CashConstant.CASH_STATUS_APPLY) {
                            continue;
                        }
                        if (state == CashConstant.CASH_STATUS_SUCCESS) {
                            Date cashSuccTime = result.getDate("cashSuccTime");
                            mchWithdrawHistoryService.updateCashStatus4Success(cashOrderId, cashSuccTime);
                        }
                        if (state == CashConstant.CASH_STATUS_FAIL) {
                            mchWithdrawHistoryService.updateCashStatus4Fail(cashOrderId);
                            _log.error("提现申请被拒绝。单号：" + cashOrderId);
                        }
                    }
                    break;

                    case PayConstant.CHANNEL_NAME_DS: {
                        ChannelDS channelDS = new ChannelDS();
                        JSONObject result = channelDS.queryCashState(cashOrderId);
                        byte state = result.getByte("state");
                        if (state == CashConstant.CASH_STATUS_APPLY) {
                            continue;
                        }
                        if (state == CashConstant.CASH_STATUS_SUCCESS) {
                            Date cashSuccTime = result.getDate("cashSuccTime");
                            mchWithdrawHistoryService.updateCashStatus4Success(cashOrderId, cashSuccTime);
                        }
                        if (state == CashConstant.CASH_STATUS_FAIL) {
                            mchWithdrawHistoryService.updateCashStatus4Fail(cashOrderId);
                            _log.error("提现申请被拒绝。单号：" + cashOrderId);
                        }
                    }
                    break;
                    case  PayConstant.CHANNEL_NAME_MZWYQ:{
                        ChannelMzwyq channelMzwyq = new ChannelMzwyq();
                        JSONObject result = channelMzwyq.queryCashState(cashOrderId);
                        byte state = result.getByte("state");
                        if (state == CashConstant.CASH_STATUS_APPLY) {
                            continue;
                        }
                        if (state == CashConstant.CASH_STATUS_SUCCESS) {
                            Date cashSuccTime = result.getDate("cashSuccTime");
                            mchWithdrawHistoryService.updateCashStatus4Success(cashOrderId, cashSuccTime);
                        }
                        if (state == CashConstant.CASH_STATUS_FAIL) {
                            mchWithdrawHistoryService.updateCashStatus4Fail(cashOrderId);
                            _log.error("提现申请被拒绝。单号：" + cashOrderId);
                        }
                    }
                    break;
                }
            } catch (Exception ex) {
                _log.error("查询代付状态异常！", ex);
            }
        }
    }
}
