package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.MchBankCardMapper;
import org.xxpay.dal.dao.model.MchBankCard;
import org.xxpay.dal.dao.model.MchBankCardExample;
import org.xxpay.common.enumm.MchType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class MchBankCardService {

    @Autowired
    private MchBankCardMapper mchBankCardMapper;

    public int add(MchBankCard info) {
        info.setMchType(MchType.AGENT.getCode());
        info.setId(UUID.randomUUID().toString());
        return mchBankCardMapper.insertSelective(info);
    }

    public int update(MchBankCard info) {
        info.setUpdateTime(new Date());
        return mchBankCardMapper.updateByPrimaryKeySelective(info);
    }

    public MchBankCard select(String id) {
        return mchBankCardMapper.selectByPrimaryKey(id);
    }

    public List<MchBankCard> getList(int offset, int limit, MchBankCard info) {
        MchBankCardExample example = new MchBankCardExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchBankCardExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchBankCardMapper.selectByExample(example);
    }

    public Integer count(MchBankCard info) {
        MchBankCardExample example = new MchBankCardExample();
        MchBankCardExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchBankCardMapper.countByExample(example);
    }

    void setCriteria(MchBankCardExample.Criteria criteria, MchBankCard info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if(StringUtils.isNotBlank(info.getNumber())) criteria.andNumberEqualTo(info.getNumber());
            if(info.getType() !=null ) criteria.andTypeEqualTo(info.getType());
        }
    }
}
