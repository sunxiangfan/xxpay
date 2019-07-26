package org.xxpay.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.AgentMchPayChannelMapper;
import org.xxpay.dal.dao.mapper.MchPayChannelMapper;
import org.xxpay.dal.dao.model.AgentMchPayChannel;
import org.xxpay.dal.dao.model.AgentMchPayChannelExample;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.MchPayChannelExample;

import java.util.List;

/**
 * @Description:
 * @author tanghaibo
 * @date 2019-02-26
 * @version V1.0
 */
@Component
public class AgentMchPayChannelService {

    @Autowired
    private AgentMchPayChannelMapper agentMchPayChannelMapper;

    public AgentMchPayChannel selectAgentMchPayChannel(String payChannelId, String agentMchId) {
        AgentMchPayChannelExample example = new AgentMchPayChannelExample();
        AgentMchPayChannelExample.Criteria criteria = example.createCriteria();
        criteria.andPayChannelIdEqualTo(payChannelId);
        criteria.andMchIdEqualTo(agentMchId);
        List<AgentMchPayChannel> agentMchPayChannelList = agentMchPayChannelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(agentMchPayChannelList)) return null;
        return agentMchPayChannelList.get(0);
    }

}
