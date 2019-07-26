package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.common.constant.CashConstant;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.mapper.MchWithdrawApplyMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.model.dto.CashGroupSumAmountModel;
import org.xxpay.dal.dao.model.dto.CashSumAmountModel;
import org.xxpay.domain.MchWithdrawApplyDomainService;

import java.util.*;

@Service
public class MchWithdrawHistoryService {

    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private MchWithdrawApplyDomainService mchWithdrawApplyDomainService;


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


    void setCriteria(MchWithdrawApplyExample.Criteria criteria, MchWithdrawApply info) {
        List<Byte> states = new ArrayList<>();
        states.add((byte) 1);
        states.add((byte) 2);
        criteria.andStateIn(states);
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (StringUtils.isNotBlank(info.getCashChannelId())) criteria.andCashChannelIdEqualTo(info.getCashChannelId());
            if (StringUtils.isNotBlank(info.getAccountName())) criteria.andAccountNameEqualTo(info.getAccountName());
            if (info.getMchType() != null) criteria.andMchTypeEqualTo(info.getMchType());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }

    public int updateCashStatus4Apply(String applyId, String cashChannelId, Long cashAmount, Long thirdDeduction, String channelOrderNo) {
        MchWithdrawApply apply = new MchWithdrawApply();
        apply.setCashState(CashConstant.CASH_STATUS_APPLY);
        apply.setCashChannelId(cashChannelId);
        apply.setCashAmount(cashAmount);
        apply.setThirdDeduction(thirdDeduction);
        if (channelOrderNo != null) apply.setChannelOrderNo(channelOrderNo);
//        apply.setPaySuccTime(System.currentTimeMillis());
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(applyId);
        criteria.andCashStateEqualTo(CashConstant.CASH_STATUS_INIT);
        return mchWithdrawApplyMapper.updateByExampleSelective(apply, example);
    }

    public int updateCashStatus4Success(String applyId, Date cashSuccTime) {
        MchWithdrawApply apply = new MchWithdrawApply();
        apply.setCashState(CashConstant.CASH_STATUS_SUCCESS);
        apply.setCashSuccTime(cashSuccTime);
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(applyId);
        criteria.andCashStateEqualTo(CashConstant.CASH_STATUS_APPLY);
        return mchWithdrawApplyMapper.updateByExampleSelective(apply, example);
    }

    public int updateCashStatus4Fail(String applyId) {
        MchWithdrawApply apply = new MchWithdrawApply();
        apply.setCashState(CashConstant.CASH_STATUS_FAIL);
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(applyId);
        criteria.andCashStateEqualTo(CashConstant.CASH_STATUS_APPLY);
        return mchWithdrawApplyMapper.updateByExampleSelective(apply, example);
    }


    public Long sumCommissionAmount(MchWithdrawApply info, List<Byte> statusIn, boolean limitToday) {
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
        return mchWithdrawApplyMapper.sumCommissionByExample(example);
    }

    public List<CashSumAmountModel> sumAmountByExample(MchWithdrawApply info, boolean limitToday) {
        List<CashSumAmountModel> models = mchWithdrawApplyDomainService.sumAmountByExample(info, limitToday);
        return models;
    }

}
