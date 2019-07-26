package org.xxpay.merchant.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.DepositAmountMapper;
import org.xxpay.dal.dao.mapper.PayOrderMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.domain.DepositAmountDomainService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tanghaibo on 2019-4-2
 */
@Component
public class DepositAmountService {

    @Autowired
    private DepositAmountMapper depositAmountMapper;

    @Autowired
    private DepositAmountDomainService depositAmountDomainService;

    public List<DepositAmount> getList(int offset, int limit, DepositAmount payOrder) {
        DepositAmountExample example = new DepositAmountExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        DepositAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return depositAmountMapper.selectByExample(example);
    }

    public Integer count(DepositAmount payOrder) {
        DepositAmountExample example = new DepositAmountExample();
        DepositAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return depositAmountMapper.countByExample(example);
    }

    public Long sumLockedAmountByMchId(String mchId) {
        return  depositAmountDomainService.sumLockedAmountByMchId(mchId);
    }


    void setCriteria(DepositAmountExample.Criteria criteria, DepositAmount info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if(info.getState() != null && info.getState() != -99) criteria.andStateEqualTo(info.getState());
        }
    }


}
