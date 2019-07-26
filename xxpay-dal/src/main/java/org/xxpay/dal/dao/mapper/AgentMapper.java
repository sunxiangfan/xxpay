package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.AgentExample;

public interface AgentMapper extends BaseMapper<Agent, AgentExample>{
    Long selectBalanceById(String id);
}