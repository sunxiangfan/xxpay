package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.DepositAmountMapper;
import org.xxpay.dal.dao.model.DepositAmount;
import org.xxpay.dal.dao.model.DepositAmountExample;
import org.xxpay.dal.dao.model.dto.DepositGroupMchIdSumAmount;
import org.xxpay.domain.DepositAmountDomainService;

import java.util.Date;
import java.util.List;

/**
 * Created by tanghaibo on 2019-4-4
 */
@Component
public class DepositAmountService {

    @Autowired
    private DepositAmountMapper depositAmountMapper;

    @Autowired
    private DepositAmountDomainService depositAmountDomainService;

    public List<DepositAmount> getList(int offset, int limit, DepositAmount payOrder, Date dateFrom, Date dateTo) {
        DepositAmountExample example = new DepositAmountExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        DepositAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        if (dateFrom != null && dateTo != null) {
            criteria.andPlanUnlockTimeBetween(dateFrom, dateTo);
        }
        return depositAmountMapper.selectByExample(example);
    }

    public Integer count(DepositAmount payOrder, Date dateFrom, Date dateTo) {
        DepositAmountExample example = new DepositAmountExample();
        DepositAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        if (dateFrom != null && dateTo != null) {
            criteria.andPlanUnlockTimeBetween(dateFrom, dateTo);
        }
        return depositAmountMapper.countByExample(example);
    }

    //    public Long sumLockedAmountByMchId(String mchId) {
//        return  depositAmountDomainService.sumLockedAmountByMchId(mchId);
//    }
    public List<DepositGroupMchIdSumAmount> groupMchIdSumAmountByExample(DepositAmount info, List<String> mchIds) {
        DepositAmountExample example = new DepositAmountExample();
        DepositAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        if (!CollectionUtils.isEmpty(mchIds)) {
            criteria.andMchIdIn(mchIds);
        }
        return depositAmountMapper.groupMchIdSumAmountByExample(example);
    }

    public int unlock(List<String> ids) {
        DepositAmountExample example = new DepositAmountExample();
        DepositAmountExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo((byte) 0);
        criteria.andIdIn(ids);

        DepositAmount updateItem = new DepositAmount();
        updateItem.setState((byte) 1);
        updateItem.setUnlockTime(new Date());
        return depositAmountMapper.updateByExampleSelective(updateItem, example);
    }

    /**
     * 统计保证金
     * @param info
     * @return
     */
    public Long sumAmount(DepositAmount info) {
        DepositAmountExample example = new DepositAmountExample();
        DepositAmountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return depositAmountMapper.sumAmountByExample(example);
    }

    void setCriteria(DepositAmountExample.Criteria criteria, DepositAmount info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (info.getState() != null && info.getState() != -99) criteria.andStateEqualTo(info.getState());
        }
    }


}
