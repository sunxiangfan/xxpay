package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.MchPayChannelMapper;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.MchPayChannelExample;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by dingzhiwei on 17/5/7.
 */
@Component
public class MchPayChannelService {

    @Autowired
    private MchPayChannelMapper mchPayChannelMapper;

    public int add(MchPayChannel info) {
        info.setId(UUID.randomUUID().toString());
        return mchPayChannelMapper.insertSelective(info);
    }

    public int update(MchPayChannel info) {
        info.setUpdateTime(new Date());
        return mchPayChannelMapper.updateByPrimaryKeySelective(info);
    }

    public MchPayChannel select(String id) {
        return mchPayChannelMapper.selectByPrimaryKey(id);
    }

    public List<MchPayChannel> getList(int offset, int limit, MchPayChannel info) {
        MchPayChannelExample example = new MchPayChannelExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchPayChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchPayChannelMapper.selectByExample(example);
    }

    public Integer count(MchPayChannel info) {
        MchPayChannelExample example = new MchPayChannelExample();
        MchPayChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchPayChannelMapper.countByExample(example);
    }

    void setCriteria(MchPayChannelExample.Criteria criteria, MchPayChannel info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if(StringUtils.isNotBlank(info.getPayChannelId())) criteria.andPayChannelIdEqualTo(info.getPayChannelId());
            if(info.getState() !=null ) criteria.andStateEqualTo(info.getState());
            if(info.getMchType() !=null ) criteria.andMchTypeEqualTo(info.getMchType());
        }
    }

}
