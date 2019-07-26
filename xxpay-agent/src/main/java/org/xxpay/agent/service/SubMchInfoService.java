package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchInfoExample;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class SubMchInfoService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    public int add(MchInfo info) {
        info.setId(UUID.randomUUID().toString());
        return mchInfoMapper.insertSelective(info);
    }

    public int update(MchInfo info) {
        info.setUpdateTime(new Date());
        return mchInfoMapper.updateByPrimaryKeySelective(info);
    }

    public MchInfo select(String userId) {
        return mchInfoMapper.selectByPrimaryKey(userId);
    }

    public List<MchInfo> getList(int offset, int limit, MchInfo info) {
        MchInfoExample example = new MchInfoExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchInfoExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchInfoMapper.selectByExample(example);
    }

    public Integer count(MchInfo info) {
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchInfoMapper.countByExample(example);
    }

    void setCriteria(MchInfoExample.Criteria criteria, MchInfo info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getAgentId())) criteria.andAgentIdEqualTo(info.getAgentId());
            if(info.getState() !=null ) criteria.andStateEqualTo(info.getState());
        }
    }
}
