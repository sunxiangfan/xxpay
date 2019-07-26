package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.PayChannelMapper;
import org.xxpay.dal.dao.model.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by dingzhiwei on 17/5/7.
 */
@Component
public class PayChannelService {

    @Autowired
    private PayChannelMapper payChannelMapper;

    public int add(PayChannel info) {
        info.setId(UUID.randomUUID().toString());
        return payChannelMapper.insertSelective(info);
    }

    public int update(PayChannel info) {
        info.setUpdateTime(new Date());
        return payChannelMapper.updateByPrimaryKeySelective(info);
    }

    public PayChannel select(String id) {
        return payChannelMapper.selectByPrimaryKey(id);
    }

    public List<PayChannel> select(List<String> ids) {
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        if(!CollectionUtils.isEmpty(ids)){
            criteria.andIdIn(ids);
        }
        return payChannelMapper.selectByExample(example);
    }
    public List<PayChannel> getList(int offset, int limit, PayChannel payChannel) {
        PayChannelExample example = new PayChannelExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payChannel);
        return payChannelMapper.selectByExample(example);
    }

    public List<PayChannel> getAllEnableList(){
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        PayChannel info=new PayChannel();
        info.setState((byte) 1);
        setCriteria(criteria, info);
        return payChannelMapper.selectByExample(example);
    }

    public List<PayChannel> getAllList(){
        PayChannelExample example = new PayChannelExample();
        return payChannelMapper.selectByExample(example);
    }

    public Integer count(PayChannel payChannel) {
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payChannel);
        return payChannelMapper.countByExample(example);
    }

    void setCriteria(PayChannelExample.Criteria criteria, PayChannel info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getName())) criteria.andNameEqualTo(info.getName());
            if(StringUtils.isNotBlank(info.getCode())) criteria.andCodeEqualTo(info.getCode());
            if(info.getState() !=null ) criteria.andStateEqualTo(info.getState());
        }
    }

}
