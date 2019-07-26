package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.constant.SystemSettingConstant;
import org.xxpay.dal.dao.mapper.SystemSettingMapper;
import org.xxpay.dal.dao.model.SystemSetting;
import org.xxpay.dal.dao.model.SystemSettingExample;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SettingService {

    @Autowired
    private SystemSettingMapper settingMapper;

    public int add(SystemSetting info) {
        info.setId(UUID.randomUUID().toString());
        info.setCreateBy("admin");
        return settingMapper.insertSelective(info);
    }

    public int update(SystemSetting info) {
        info.setUpdateTime(new Date());
        return settingMapper.updateByPrimaryKeySelective(info);
    }

    public SystemSetting select(String userId) {
        return settingMapper.selectByPrimaryKey(userId);
    }

    public List<SystemSetting> getList(int offset, int limit, SystemSetting info) {
        SystemSettingExample example = new SystemSettingExample();
        example.setOrderByClause("paramOrder");
        example.setOffset(offset);
        example.setLimit(limit);
        SystemSettingExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return settingMapper.selectByExample(example);
    }

    public Integer count(SystemSetting info) {
        SystemSettingExample example = new SystemSettingExample();
        SystemSettingExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return settingMapper.countByExample(example);
    }

    public SystemSetting selectByName(String key) {
        return settingMapper.selectByName(key);
    }

    /**
     * 生成新的代理商户号
     *
     * @return
     */
    @Transactional
    public String getNextAgentMchId() {
        SystemSetting setting = settingMapper.selectByName(SystemSettingConstant.KEY_MCHID_MAX);
        Assert.notNull(setting, "系统错误！缺少系统参数项：" + SystemSettingConstant.KEY_MCHID_MAX);
        Long maxNumber = Long.parseLong(setting.getParamValue());
        Long nextNumber = maxNumber + 1;
        Integer preVersion = setting.getVersion();
        Integer nextVersion = preVersion + 1;

        SystemSetting settingPrefix = settingMapper.selectByName(SystemSettingConstant.KEY_AGENT_MCHID_PREFIX);
        Assert.notNull(settingPrefix, "系统错误！缺少系统参数项：" + SystemSettingConstant.KEY_AGENT_MCHID_PREFIX);
        String prefix = settingPrefix.getParamValue();

        SystemSetting updateSettingItem = new SystemSetting();
        updateSettingItem.setParamValue(nextNumber + "");
        updateSettingItem.setVersion(nextVersion);

        SystemSettingExample settingExample = new SystemSettingExample();
        SystemSettingExample.Criteria criteria = settingExample.createCriteria();
        criteria.andIdEqualTo(setting.getId());
        criteria.andVersionEqualTo(preVersion);

        int rowUpdates = settingMapper.updateByExampleSelective(updateSettingItem, settingExample);
        if (rowUpdates <= 0) {
            return null;
        }
        String nextAgentMchId = prefix + nextNumber;
        return nextAgentMchId;
    }

    /**
     * 生成新的子商户号
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String getNextSubMchId() {
        SystemSetting setting = settingMapper.selectByName(SystemSettingConstant.KEY_MCHID_MAX);
        Assert.notNull(setting, "系统错误！缺少系统参数项：" + SystemSettingConstant.KEY_MCHID_MAX);
        Long maxNumber = Long.parseLong(setting.getParamValue());
        Long nextNumber = maxNumber + 1;
        Integer preVersion = setting.getVersion();
        Integer nextVersion = preVersion + 1;

        SystemSetting updateSettingItem = new SystemSetting();
        updateSettingItem.setParamValue(nextNumber + "");
        updateSettingItem.setVersion(nextVersion);

        SystemSettingExample settingExample = new SystemSettingExample();
        SystemSettingExample.Criteria criteria = settingExample.createCriteria();
        criteria.andIdEqualTo(setting.getId());
        criteria.andVersionEqualTo(preVersion);

        int rowUpdates = settingMapper.updateByExampleSelective(updateSettingItem, settingExample);
        if (rowUpdates <= 0) {
            return null;
        }
        String nextAgentMchId = nextNumber + "";
        return nextAgentMchId;
    }

    void setCriteria(SystemSettingExample.Criteria criteria, SystemSetting info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getParamName())) criteria.andParamNameEqualTo(info.getParamName());
            if (info.getVersion() != null) criteria.andVersionEqualTo(info.getVersion());
        }
    }
}
