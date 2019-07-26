package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.MchCompanyMapper;
import org.xxpay.dal.dao.model.MchCompany;
import org.xxpay.dal.dao.model.MchCompanyExample;

import java.util.List;

/**
 * Created by tanghaibo on 19/2/19.
 */
@Component
public class MchCompanyService {

    @Autowired
    private MchCompanyMapper mchCompanyMapper;

    public MchCompany select(String id) {
        return mchCompanyMapper.selectByPrimaryKey(id);
    }

    public List<MchCompany> select(List<String> ids) {
        MchCompanyExample example = new MchCompanyExample();
        MchCompanyExample.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(ids)) {
            criteria.andIdIn(ids);
        }
        return mchCompanyMapper.selectByExample(example);
    }

    public List<MchCompany> select(MchCompany info) {
        MchCompanyExample example = new MchCompanyExample();
        MchCompanyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchCompanyMapper.selectByExample(example);
    }

    public List<MchCompany> selectByMchIds(List<String> mchIds) {
        MchCompanyExample example = new MchCompanyExample();
        MchCompanyExample.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(mchIds)) {
            criteria.andMchIdIn(mchIds);
        }
        return mchCompanyMapper.selectByExample(example);
    }

    public MchCompany selectByMchId(String mchId) {
        MchCompanyExample example = new MchCompanyExample();
        MchCompanyExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(mchId)) {
            criteria.andMchIdEqualTo(mchId);
        }
        List<MchCompany> result = mchCompanyMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        Assert.isTrue(result.size()==1,"查到多条进件数据。商户号："+mchId);
        return result.get(0);
    }

    public List<MchCompany> getList(int offset, int limit, MchCompany info) {
        MchCompanyExample example = new MchCompanyExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchCompanyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchCompanyMapper.selectByExample(example);
    }

    public Integer count(MchCompany info) {
        MchCompanyExample example = new MchCompanyExample();
        MchCompanyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchCompanyMapper.countByExample(example);
    }

    void setCriteria(MchCompanyExample.Criteria criteria, MchCompany info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
        }
    }

}
