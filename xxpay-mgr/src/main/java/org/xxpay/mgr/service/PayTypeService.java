package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.PayTypeMapper;
import org.xxpay.dal.dao.model.PayType;
import org.xxpay.dal.dao.model.PayTypeExample;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class PayTypeService {

    @Autowired
    private PayTypeMapper payTypeMapper;

    public int add(PayType info) {
        info.setCreateTime(new Date());
        return payTypeMapper.insertSelective(info);
    }

    public int update(PayType info) {
        info.setUpdateTime(new Date());
        return payTypeMapper.updateByPrimaryKeySelective(info);
    }

    public PayType select(String id) {
        return payTypeMapper.selectByPrimaryKey(id);
    }

    public List<PayType> getList(int offset, int limit, PayType info) {
        PayTypeExample example = new PayTypeExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return payTypeMapper.selectByExample(example);
    }

    public List<PayType> getAllList(){
        PayTypeExample example = new PayTypeExample();
        PayTypeExample.Criteria criteria = example.createCriteria();
        PayType info=new PayType();
        setCriteria(criteria, info);
        return payTypeMapper.selectByExample(example);
    }

    public Integer count(PayType info) {
        PayTypeExample example = new PayTypeExample();
        PayTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return payTypeMapper.countByExample(example);
    }

    void setCriteria(PayTypeExample.Criteria criteria, PayType info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getName())) criteria.andNameEqualTo(info.getName());
        }
    }
}
