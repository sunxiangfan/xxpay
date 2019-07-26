package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.common.util.PasswordUtil;
import org.xxpay.dal.dao.mapper.ReserveAmountMapper;
import org.xxpay.dal.dao.model.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReserveAmountService {

    @Autowired
    private ReserveAmountMapper reserveAmountMapper;

    @Transactional
    public int add(ReserveAmount info) {
        return reserveAmountMapper.insertSelective(info);
    }
    public int update(ReserveAmount info) {
        return reserveAmountMapper.updateByPrimaryKeySelective(info);
    }
    public ReserveAmount select(String id) {
        return reserveAmountMapper.selectByPrimaryKey(id);
    }

    public Long sumAmount() {
        return reserveAmountMapper.sumAmountByExample(null);
    }

    public List<ReserveAmount> getList(int offset, int limit, ReserveAmount info) {
        ReserveAmountExample example = new ReserveAmountExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        ReserveAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return reserveAmountMapper.selectByExample(example);
    }

    public Integer count(ReserveAmount info) {
        ReserveAmountExample example = new ReserveAmountExample();
        ReserveAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return reserveAmountMapper.countByExample(example);
    }

    void setCriteria(ReserveAmountExample.Criteria criteria, ReserveAmount info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (info.getFlowType() != null) criteria.andFlowTypeEqualTo(info.getFlowType());
        }
    }
}
