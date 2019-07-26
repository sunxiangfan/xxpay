package org.xxpay.merchant.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.AmountFlowMapper;
import org.xxpay.dal.dao.model.AmountFlow;
import org.xxpay.dal.dao.model.AmountFlowExample;

import java.util.List;

/**
 * Created by tanghaibo on 2019-4-9
 */
@Component
public class AmountFlowService {

    @Autowired
    private AmountFlowMapper amountFlowMapper;

    public List<AmountFlow> getList(int offset, int limit, AmountFlow payOrder) {
        AmountFlowExample example = new AmountFlowExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AmountFlowExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return amountFlowMapper.selectByExample(example);
    }

    public Integer count(AmountFlow payOrder) {
        AmountFlowExample example = new AmountFlowExample();
        AmountFlowExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return amountFlowMapper.countByExample(example);
    }


    void setCriteria(AmountFlowExample.Criteria criteria, AmountFlow info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
        }
    }


}
