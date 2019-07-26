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
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.common.util.PasswordUtil;
import org.xxpay.dal.dao.mapper.MchCompanyMapper;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.mapper.UserMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.common.enumm.MchType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MchInfoService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private MchCompanyMapper mchCompanyMapper;

    @Autowired
    private AmountFlowService amountFlowService;

    @Autowired
    private SettingService settingService;

    @Transactional(propagation = Propagation.REQUIRED)
    public int add(MchInfo info, MchCompany mchCompany) {
        String nextId = settingService.getNextSubMchId();
        Assert.notNull(nextId, "生成子商户号失败！");
        String mchId = nextId;
        info.setId(mchId);
        MchCompany company = new MchCompany();
        company.setId(UUID.randomUUID().toString());
        company.setCompanyName(info.getName());
        company.setMchId(mchId);
        company.setCreateBy(info.getCreateBy());
        String rawPassword = info.getPassword();
        String encryptionPassword = PasswordUtil.getPassword(rawPassword, mchId);
        info.setPassword(encryptionPassword);

        mchCompany.setMchId(mchId);
        int updateRows = mchInfoMapper.insertSelective(info);
        Assert.isTrue(updateRows == 1, "保存商户信息失败！");
        updateRows = mchCompanyMapper.insertSelective(mchCompany);
        Assert.isTrue(updateRows == 1, "保存进件信息失败！");
        return updateRows;
    }

    public int update(MchInfo info, MchCompany mchCompany) {
        String mchId = info.getId();
        String rawPassword = info.getPassword();
        if (StringUtils.isNoneBlank(rawPassword)) {
            String encryptionPassword = PasswordUtil.getPassword(rawPassword, mchId);
            info.setPassword(encryptionPassword);
        }
        int updateRows = mchInfoMapper.updateByPrimaryKeySelective(info);
        Assert.isTrue(updateRows == 1, "保存商户信息失败！");
        updateRows = mchCompanyMapper.updateByPrimaryKeySelective(mchCompany);
        Assert.isTrue(updateRows == 1, "保存进件信息失败！");
        return updateRows;
    }

    public MchInfo select(String id) {
        return mchInfoMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public int lockAmount(String mchId, Long amount, String remark) {
        MchInfo info = mchInfoMapper.selectByPrimaryKey(mchId);
        String agentId = info.getId();
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.sub(preBalance, amount);
        Assert.isTrue(afterBalance.compareTo(0L) >= 0, "冻结金额不能超过余额！");

        int preVersion = info.getVersion();
        Long preLockAmount = info.getLockAmount();
        Long afterLockAmount = BigDecimalUtils.add(preLockAmount, amount);
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        MchInfo updateItem = new MchInfo();
        updateItem.setBalance(afterBalance);
        updateItem.setLockAmount(afterLockAmount);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = mchInfoMapper.updateByExampleSelective(updateItem, example);
        Assert.isTrue(updateRows > 0, "保存失败！");

        updateRows = amountFlowService.lockAmount(agentId, MchType.MERCHANT, amount, preBalance, remark);
        Assert.isTrue(updateRows > 0, "保存变动表失败！");
        return updateRows;
    }


    @Transactional
    public int unlockAmount(String mchId, Long amount, String remark) {
        MchInfo info = mchInfoMapper.selectByPrimaryKey(mchId);
        String agentId = info.getId();
        Long preLockAmount = info.getLockAmount();
        Long afterLockAmount = BigDecimalUtils.sub(preLockAmount, amount);
        Assert.isTrue(afterLockAmount.compareTo(0L) >= 0, "解冻金额不能超过已冻结金额！");

        int preVersion = info.getVersion();
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.add(preBalance, amount);
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        MchInfo updateItem = new MchInfo();
        updateItem.setBalance(afterBalance);
        updateItem.setLockAmount(afterLockAmount);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = mchInfoMapper.updateByExampleSelective(updateItem, example);
        Assert.isTrue(updateRows > 0, "保存失败！");
        updateRows = amountFlowService.unlockAmount(agentId, MchType.MERCHANT, amount, preBalance, remark);
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
        MchInfo info = mchInfoMapper.selectByPrimaryKey(agentId);
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.add(preBalance.longValue(), amount.longValue());
        int preVersion = info.getVersion();
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        MchInfo updateItem = new MchInfo();
        updateItem.setBalance(afterBalance);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = mchInfoMapper.updateByExampleSelective(updateItem, example);
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
        MchInfo info = mchInfoMapper.selectByPrimaryKey(agentId);
        Long preBalance = info.getBalance();
        Long afterBalance = BigDecimalUtils.sub(preBalance.longValue(), amount.longValue());
        int preVersion = info.getVersion();
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(agentId);
        criteria.andVersionEqualTo(preVersion);
        criteria.andBalanceEqualTo(preBalance);
        int afterVersion = preVersion + 1;
        MchInfo updateItem = new MchInfo();
        updateItem.setBalance(afterBalance);
        updateItem.setVersion(afterVersion);
        updateItem.setUpdateTime(new Date());
        int updateRows = mchInfoMapper.updateByExampleSelective(updateItem, example);
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
    public List<MchInfo> select(List<String> ids) {
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(ids)) {
            criteria.andIdIn(ids);
        }
        return mchInfoMapper.selectByExample(example);
    }

    /**
     * 汇总余额
     * @param info
     * @return
     */
    public  Long sumBalance(MchInfo info){
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchInfoMapper.sumBalanceByExample(example);
    }

    void setCriteria(MchInfoExample.Criteria criteria, MchInfo info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getAgentId())) criteria.andAgentIdEqualTo(info.getAgentId());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }
}
