package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.enumm.UserType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.common.util.PasswordUtil;
import org.xxpay.dal.dao.mapper.AgentMapper;
import org.xxpay.dal.dao.mapper.MchCompanyMapper;
import org.xxpay.dal.dao.mapper.AgentMapper;
import org.xxpay.dal.dao.mapper.UserMapper;
import org.xxpay.dal.dao.model.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private SettingService settingService;

    @Autowired
    private AmountFlowService amountFlowService;

    @Transactional
    public int add(Agent info) {
        String nextId = settingService.getNextAgentMchId();
        Assert.notNull(nextId, "生成代理商户号失败！");
        String mchId = nextId;
        info.setId(mchId);

        String rawPassword = info.getPassword();
        String encryptionPassword = PasswordUtil.getPassword(rawPassword, mchId);
        info.setPassword(encryptionPassword);
        return agentMapper.insertSelective(info);
    }

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

    @Transactional
    public int lockAmount(String agentId, Long amount, String remark) {
        Agent info = agentMapper.selectByPrimaryKey(agentId);
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.sub(preBalance, amount);
        Assert.isTrue(afterBalance.compareTo(0L) >= 0, "冻结金额不能超过余额！");

        int preVersion = info.getVersion();
        Long preLockAmount = info.getLockAmount();
        Long afterLockAmount = BigDecimalUtils.add(preLockAmount, amount);
        AgentExample example = new AgentExample();
        AgentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        Agent updateItem = new Agent();
        updateItem.setBalance(afterBalance);
        updateItem.setLockAmount(afterLockAmount);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = agentMapper.updateByExampleSelective(updateItem, example);
        Assert.isTrue(updateRows > 0, "保存失败！");
        updateRows = amountFlowService.lockAmount(agentId, MchType.AGENT, amount, preBalance, remark);
        Assert.isTrue(updateRows > 0, "保存变动表失败！");
        return updateRows;
    }


    @Transactional
    public int unlockAmount(String agentId, Long amount, String remark) {
        Agent info = agentMapper.selectByPrimaryKey(agentId);
        Long preLockAmount = info.getLockAmount();
        Long afterLockAmount = BigDecimalUtils.sub(preLockAmount, amount);
        Assert.isTrue(afterLockAmount.compareTo(0L) >= 0, "解冻金额不能超过已冻结金额！");

        int preVersion = info.getVersion();
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.add(preBalance, amount);
        AgentExample example = new AgentExample();
        AgentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        Agent updateItem = new Agent();
        updateItem.setBalance(afterBalance);
        updateItem.setLockAmount(afterLockAmount);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = agentMapper.updateByExampleSelective(updateItem, example);
        Assert.isTrue(updateRows > 0, "保存失败！");
        updateRows = amountFlowService.unlockAmount(agentId, MchType.AGENT, amount, preBalance, remark);
        Assert.isTrue(updateRows > 0, "保存变动表失败！");
        return updateRows;
    }


    /**
     * 手工补入账
     *
     * @param agentId
     * @param amount
     * @param remark
     * @return
     */
    @Transactional
    public int adjustIncreaseAmount(String agentId, Long amount, String remark) {
        Assert.notNull(amount, "金额不能为空！");
        Assert.isTrue(amount.compareTo(0L) > 0, "金额必须大于0！");
        Agent info = agentMapper.selectByPrimaryKey(agentId);
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.add(preBalance.longValue(), amount.longValue());
        int preVersion = info.getVersion();
        AgentExample example = new AgentExample();
        AgentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        Agent updateItem = new Agent();
        updateItem.setBalance(afterBalance);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = agentMapper.updateByExampleSelective(updateItem, example);
        Assert.isTrue(updateRows > 0, "保存失败！");
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setAmount(amount);
        amountFlow.setMchId(agentId);
        amountFlow.setFlowType(AmountFlowType.IN.getCode());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setRemark(remark);
        amountFlow.setPreBalance(preBalance);
        updateRows = amountFlowService.insertIncrease(amountFlow, AmountType.ADMIN_ADJUST_INCREASE);
        Assert.isTrue(updateRows > 0, "保存变动表失败！");
        return updateRows;
    }

    /**
     * 手工补出账
     *
     * @param agentId
     * @param amount
     * @param remark
     * @return
     */
    @Transactional
    public int adjustDecreaseAmount(String agentId, Long amount, String remark) {
        Assert.notNull(amount, "金额不能为空！");
        Assert.isTrue(amount.compareTo(0L) > 0, "金额必须大于0！");
        Agent info = agentMapper.selectByPrimaryKey(agentId);
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.sub(preBalance.longValue(), amount.longValue());
        int preVersion = info.getVersion();
        AgentExample example = new AgentExample();
        AgentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        Agent updateItem = new Agent();
        updateItem.setBalance(afterBalance);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = agentMapper.updateByExampleSelective(updateItem, example);
        Assert.isTrue(updateRows > 0, "保存失败！");
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setAmount(-amount);
        amountFlow.setMchId(agentId);
        amountFlow.setFlowType(AmountFlowType.OUT.getCode());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setRemark(remark);
        amountFlow.setPreBalance(preBalance);
        updateRows = amountFlowService.insertDecrease(amountFlow, AmountType.ADMIN_ADJUST_DECREASE);
        Assert.isTrue(updateRows > 0, "保存变动表失败！");
        return updateRows;
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

    void setCriteria(AgentExample.Criteria criteria, Agent info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }
}
