package org.xxpay.merchant.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.enumm.MchType;
import org.xxpay.dal.dao.mapper.MchBankCardMapper;
import org.xxpay.dal.dao.mapper.MchWithdrawApplyMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.model.dto.CashGroupSumAmountModel;
import org.xxpay.dal.dao.model.dto.CashSumAmountModel;
import org.xxpay.domain.MchWithdrawApplyDomainService;

import java.util.*;

@Service
public class MchWithdrawApplyService {

    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchBankCardMapper mchBankCardMapper;

    @Autowired
    private MchWithdrawApplyDomainService mchWithdrawApplyDomainService;


    @Transactional
    public int apply(String mchId, String mchBackCardId, Long applyAmount) {
        MchBankCard mchBankCard = mchBankCardMapper.selectByPrimaryKey(mchBackCardId);
        Assert.isTrue(StringUtils.isNoneBlank(mchBankCard.getBankUnionCode()),"开户行联行号不能为空。请先完善该银行卡信息。");
        return mchWithdrawApplyDomainService.applyCash(mchId, MchType.MERCHANT, mchBankCard, applyAmount,null);
    }

    public  List<CashSumAmountModel> sumAmountByExample(MchWithdrawApply info, boolean limitToday){
        List<CashSumAmountModel> models=mchWithdrawApplyDomainService.sumAmountByExample(info,limitToday);
        return models;
    }
    public MchWithdrawApply select(String id) {
        return mchWithdrawApplyMapper.selectByPrimaryKey(id);
    }

    public List<MchWithdrawApply> getList(int offset, int limit, MchWithdrawApply info, Date dateFrom, Date dateTo) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        if (dateFrom != null && dateTo != null) {
            criteria.andCreateTimeBetween(dateFrom, dateTo);
        }
        return mchWithdrawApplyMapper.selectByExample(example);
    }

    public Integer count(MchWithdrawApply info, Date dateFrom, Date dateTo) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        if (dateFrom != null && dateTo != null) {
            criteria.andCreateTimeBetween(dateFrom, dateTo);
        }
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

    void setCriteria(MchWithdrawApplyExample.Criteria criteria, MchWithdrawApply info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (StringUtils.isNotBlank(info.getMchOrderNo())) criteria.andMchOrderNoEqualTo(info.getMchOrderNo());
            if (info.getMchType() != null) criteria.andMchTypeEqualTo(info.getMchType());
            if (StringUtils.isNotBlank(info.getAccountName())) criteria.andAccountNameEqualTo(info.getAccountName());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }
}
