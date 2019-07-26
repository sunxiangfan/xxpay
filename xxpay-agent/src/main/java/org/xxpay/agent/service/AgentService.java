package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.enumm.MchState;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.common.util.PasswordUtil;
import org.xxpay.dal.dao.mapper.AgentMapper;
import org.xxpay.dal.dao.model.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

    @Autowired
    private AgentMapper agentMapper;

    public List<Agent> select(List<String> ids) {
        AgentExample example = new AgentExample();
        AgentExample.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(ids)) {
            criteria.andIdIn(ids);
        }
        return agentMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int update(Agent info) {
        String mchId = info.getId();
        String rawPassword = info.getPassword();
        if (StringUtils.isNoneBlank(rawPassword)) {
            String encryptionPassword = PasswordUtil.getPassword(rawPassword, mchId);
            info.setPassword(encryptionPassword);
        }
        return agentMapper.updateByPrimaryKeySelective(info);
    }

    public Agent login(String loginAccount, String password) {
        Assert.isTrue(StringUtils.isNoneBlank(loginAccount), "用户名不能为空！");
        Assert.isTrue(StringUtils.isNoneBlank(password), "密码不能为空！");
        AgentExample example = new AgentExample();
        example.setLimit(1);
        AgentExample.Criteria criteria = example.createCriteria();
        Agent agent = new Agent();
        agent.setId(loginAccount);
        setCriteria(criteria, agent);
        List<Agent> list = agentMapper.selectByExample(example);
        Assert.notEmpty(list, "用户名或密码错误！");
        Agent findAgentByAccount = list.get(0);
        String mchId=findAgentByAccount.getId();
        String encryptionPassword= PasswordUtil.getPassword(password,mchId);
        Assert.isTrue(encryptionPassword.equals(findAgentByAccount.getPassword()), "用户名或密码错误！");
        Assert.isTrue(MchState.ENABLE.getCode().equals(findAgentByAccount.getState()),"该商户号已被停用!");
        return findAgentByAccount;
    }


    public Agent select(String id) {
        return agentMapper.selectByPrimaryKey(id);
    }

    public List<Agent> getList(int offset, int limit, Agent info) {
        AgentExample example = new AgentExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return agentMapper.selectByExample(example);
    }

    public Integer count(Agent info) {
        AgentExample example = new AgentExample();
        AgentExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return agentMapper.countByExample(example);
    }

    public Long selectBalanceById(String id) {
        return agentMapper.selectBalanceById(id);
    }

    void setCriteria(AgentExample.Criteria criteria, Agent info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }
}
