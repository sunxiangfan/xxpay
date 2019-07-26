package org.xxpay.merchant.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.BankMapper;
import org.xxpay.dal.dao.model.Bank;
import org.xxpay.dal.dao.model.BankExample;

import java.util.List;
import java.util.UUID;

/**
 * Created by tanghaibo on 19/4/18.
 */
@Component
public class BankService {

    @Autowired
    private BankMapper bankMapper;

    public int add(Bank info) {
        info.setId(UUID.randomUUID().toString());
        return bankMapper.insertSelective(info);
    }

    public int update(Bank info) {
        return bankMapper.updateByPrimaryKeySelective(info);
    }

    public Bank select(String id) {
        return bankMapper.selectByPrimaryKey(id);
    }

    public List<Bank> getList(int offset, int limit, Bank payChannel) {
        BankExample example = new BankExample();
        example.setOrderByClause("name");
        example.setOffset(offset);
        example.setLimit(limit);
        BankExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payChannel);
        return bankMapper.selectByExample(example);
    }

    public List<Bank> getAllList(){
        BankExample example = new BankExample();
        return bankMapper.selectByExample(example);
    }

    public Integer count(Bank payChannel) {
        BankExample example = new BankExample();
        BankExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payChannel);
        return bankMapper.countByExample(example);
    }

    void setCriteria(BankExample.Criteria criteria, Bank info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getName())) criteria.andNameEqualTo(info.getName());
            if(StringUtils.isNotBlank(info.getCode())) criteria.andCodeEqualTo(info.getCode());
        }
    }

}
