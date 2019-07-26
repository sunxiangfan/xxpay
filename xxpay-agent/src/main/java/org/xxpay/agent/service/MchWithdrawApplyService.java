package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.constant.SystemSettingConstant;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.*;
import org.xxpay.dal.dao.model.*;
import org.xxpay.domain.MchWithdrawApplyDomainService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MchWithdrawApplyService {

    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private SystemSettingMapper systemSettingMapper;

    @Autowired
    private MchBankCardMapper mchBankCardMapper;

    @Autowired
    private MchWithdrawApplyDomainService mchWithdrawApplyDomainService;

    public int add(MchWithdrawApply info) {
        info.setId(UUID.randomUUID().toString());
        return mchWithdrawApplyMapper.insertSelective(info);
    }

    @Transactional
    public int apply(String mchId, String mchBackCardId, Long applyAmount) {
        MchBankCard mchBankCard = mchBankCardMapper.selectByPrimaryKey(mchBackCardId);
        return mchWithdrawApplyDomainService.applyCash(mchId, MchType.AGENT, mchBankCard, applyAmount, null);
    }

    public int update(MchWithdrawApply info) {
        info.setUpdateTime(new Date());
        return mchWithdrawApplyMapper.updateByPrimaryKeySelective(info);
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

    void setCriteria(MchWithdrawApplyExample.Criteria criteria, MchWithdrawApply info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (info.getMchType() != null) criteria.andMchTypeEqualTo(info.getMchType());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }
}
