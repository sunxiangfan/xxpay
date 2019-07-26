package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.ChannelNameMapper;
import org.xxpay.dal.dao.model.ChannelName;
import org.xxpay.dal.dao.model.ChannelNameExample;

import java.util.Date;
import java.util.List;

@Component
public class ChannelNameService {

    @Autowired
    private ChannelNameMapper channelNameMapper;

    public int add(ChannelName info) {
        info.setCreateTime(new Date());
        return channelNameMapper.insertSelective(info);
    }

    public int update(ChannelName info) {
        info.setUpdateTime(new Date());
        return channelNameMapper.updateByPrimaryKeySelective(info);
    }

    public ChannelName select(String id) {
        return channelNameMapper.selectByPrimaryKey(id);
    }

    public List<ChannelName> getList(int offset, int limit, ChannelName info) {
        ChannelNameExample example = new ChannelNameExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        ChannelNameExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return channelNameMapper.selectByExample(example);
    }

    public List<ChannelName> getAllList(){
        ChannelNameExample example = new ChannelNameExample();
        ChannelNameExample.Criteria criteria = example.createCriteria();
        ChannelName info=new ChannelName();
        setCriteria(criteria, info);
        return channelNameMapper.selectByExample(example);
    }

    public Integer count(ChannelName info) {
        ChannelNameExample example = new ChannelNameExample();
        ChannelNameExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return channelNameMapper.countByExample(example);
    }

    void setCriteria(ChannelNameExample.Criteria criteria, ChannelName info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(StringUtils.isNotBlank(info.getName())) criteria.andNameEqualTo(info.getName());
        }
    }
}
