package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.CashChannelMapper;
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.CashChannelExample;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tanghaibo on 19/3/28.
 */
@Component
public class CashChannelService {

    @Autowired
    private CashChannelMapper cashChannelMapper;

    public int add(CashChannel info) {
        info.setId(UUID.randomUUID().toString());
        return cashChannelMapper.insertSelective(info);
    }

    public int update(CashChannel info) {
        info.setUpdateTime(new Date());
        return cashChannelMapper.updateByPrimaryKeySelective(info);
    }

    public List<CashChannel> getAllEnableList() {
        CashChannelExample example = new CashChannelExample();
        example.setOrderByClause("createTime DESC");
        CashChannelExample.Criteria criteria = example.createCriteria();
        CashChannel payChannel = new CashChannel();
        payChannel.setState((byte)1);
        setCriteria(criteria, payChannel);
        return cashChannelMapper.selectByExample(example);
    }

    public List<CashChannel> select(List<String> ids) {
        CashChannelExample example = new CashChannelExample();
        CashChannelExample.Criteria criteria = example.createCriteria();
        if(!CollectionUtils.isEmpty(ids)){
            criteria.andIdIn(ids);
        }
        return cashChannelMapper.selectByExample(example);
    }

    public CashChannel select(String id) {
        return cashChannelMapper.selectByPrimaryKey(id);
    }

    public List<CashChannel> getList(int offset, int limit, CashChannel payChannel) {
        CashChannelExample example = new CashChannelExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        CashChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payChannel);
        return cashChannelMapper.selectByExample(example);
    }

    public Integer count(CashChannel payChannel) {
        CashChannelExample example = new CashChannelExample();
        CashChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payChannel);
        return cashChannelMapper.countByExample(example);
    }

    void setCriteria(CashChannelExample.Criteria criteria, CashChannel info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getName())) criteria.andNameEqualTo(info.getName());
            if (StringUtils.isNotBlank(info.getCode())) criteria.andCodeEqualTo(info.getCode());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
        }
    }

}
