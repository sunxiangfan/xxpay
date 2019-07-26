package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.AgentMchPayChannelMapper;
import org.xxpay.dal.dao.model.AgentMchPayChannel;
import org.xxpay.dal.dao.model.AgentMchPayChannelExample;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tanghaibo on 19/2/17.
 */
@Component
public class AgentMchPayChannelService {

    @Autowired
    private AgentMchPayChannelMapper agentMchPayChannelMapper;

    public int add(AgentMchPayChannel info) {
        info.setId(UUID.randomUUID().toString());
        return agentMchPayChannelMapper.insertSelective(info);
    }

    public int update(AgentMchPayChannel info) {
        info.setUpdateTime(new Date());
        return agentMchPayChannelMapper.updateByPrimaryKeySelective(info);
    }

    public AgentMchPayChannel select(String id) {
        return agentMchPayChannelMapper.selectByPrimaryKey(id);
    }

    public List<AgentMchPayChannel> getList(int offset, int limit, AgentMchPayChannel info) {
        AgentMchPayChannelExample example = new AgentMchPayChannelExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentMchPayChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return agentMchPayChannelMapper.selectByExample(example);
    }

    public Integer count(AgentMchPayChannel info) {
        AgentMchPayChannelExample example = new AgentMchPayChannelExample();
        AgentMchPayChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return agentMchPayChannelMapper.countByExample(example);
    }

    void setCriteria(AgentMchPayChannelExample.Criteria criteria, AgentMchPayChannel info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if(StringUtils.isNotBlank(info.getPayChannelId())) criteria.andPayChannelIdEqualTo(info.getPayChannelId());
            if(info.getState() !=null ) criteria.andStateEqualTo(info.getState());
        }
    }

}
