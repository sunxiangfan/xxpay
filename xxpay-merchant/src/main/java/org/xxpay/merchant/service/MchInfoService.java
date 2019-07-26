package org.xxpay.merchant.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.enumm.MchState;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.PasswordUtil;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchInfoExample;

import java.util.List;

/**
 * Created by dingzhiwei on 17/5/4.
 */
@Component
public class MchInfoService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    public int update(MchInfo info) {
        return mchInfoMapper.updateByPrimaryKeySelective(info);
    }

    public MchInfo select(String id) {
        return mchInfoMapper.selectByPrimaryKey(id);
    }

    public List<MchInfo> select(List<String> ids) {
        MchInfoExample example = new MchInfoExample();
        MchInfoExample.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(ids)) {
            criteria.andIdIn(ids);
        }
        return mchInfoMapper.selectByExample(example);
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
    public MchInfo login(String loginAccount, String password) {
        Assert.isTrue(StringUtils.isNoneBlank(loginAccount), "用户名不能为空！");
        Assert.isTrue(StringUtils.isNoneBlank(password), "密码不能为空！");
        MchInfoExample example = new MchInfoExample();
        example.setLimit(1);
        MchInfoExample.Criteria criteria = example.createCriteria();
        MchInfo mchInfo = new MchInfo();
        mchInfo.setId(loginAccount);
        setCriteria(criteria, mchInfo);
        List<MchInfo> list = mchInfoMapper.selectByExample(example);
        Assert.notEmpty(list, "用户名或密码错误！");
        MchInfo findMchInfoByAccount = list.get(0);
        String mchId=findMchInfoByAccount.getId();
        String encryptionPassword= PasswordUtil.getPassword(password,mchId);
        Assert.isTrue(encryptionPassword.equals(findMchInfoByAccount.getPassword()), "用户名或密码错误！");
        Assert.isTrue(MchState.ENABLE.getCode().equals(findMchInfoByAccount.getState()),"该商户号已被停用!");
        return findMchInfoByAccount;
    }

    /**
     * 修改密码
     * @param mchId
     * @param oldPasswod
     * @param newPassword
     * @return
     */
    public MchInfo changePassword(String mchId, String oldPasswod,String newPassword) {
        Assert.isTrue(StringUtils.isNoneBlank(mchId), "商户号不能为空！");
        Assert.isTrue(StringUtils.isNoneBlank(oldPasswod), "原始密码不能为空！");
        Assert.isTrue(StringUtils.isNoneBlank(newPassword), "新密码不能为空！");
        MchInfoExample example = new MchInfoExample();
        example.setLimit(1);
        MchInfoExample.Criteria criteria = example.createCriteria();
        MchInfo mchInfo = new MchInfo();
        mchInfo.setId(mchId);
        setCriteria(criteria, mchInfo);
        List<MchInfo> list = mchInfoMapper.selectByExample(example);
        Assert.notEmpty(list, "用户名或密码错误！");
        MchInfo findMchInfoByAccount = list.get(0);
        String encryptionPassword= PasswordUtil.getPassword(oldPasswod,mchId);
        Assert.isTrue(encryptionPassword.equals(findMchInfoByAccount.getPassword()), "原始密码错误！");
        Assert.isTrue(MchState.ENABLE.getCode().equals(findMchInfoByAccount.getState()),"该商户号已被停用!");
        String encryptionNewPassword= PasswordUtil.getPassword(newPassword,mchId);
        MchInfo updateItem=new MchInfo();
        updateItem.setId(mchId);
        updateItem.setPassword(encryptionNewPassword);
        int updateRows = mchInfoMapper.updateByPrimaryKeySelective(updateItem);
        Assert.isTrue(updateRows == 1, "修改密码失败！");
        return findMchInfoByAccount;
    }

    public Long selectBalanceById(String id) {
        return mchInfoMapper.selectBalanceById(id);
    }

    void setCriteria(MchInfoExample.Criteria criteria, MchInfo info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getAgentId())) criteria.andAgentIdEqualTo(info.getAgentId());
            if (info.getState() != null && info.getState() != -99) criteria.andStateEqualTo(info.getState());
        }
    }

}
