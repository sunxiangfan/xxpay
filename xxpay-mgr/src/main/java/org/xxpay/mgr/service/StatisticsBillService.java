package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.mapper.BillMapper;
import org.xxpay.dal.dao.mapper.PayOrderMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.model.dto.BillGroupCreateDateSumAmountModel;
import org.xxpay.dal.dao.model.dto.PayOrderGroupCreateDateSumAmountModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by thb on 19/5/16.
 */
@Component
public class StatisticsBillService {

    @Autowired
    private BillMapper billMapper;

    public List<BillGroupCreateDateSumAmountModel> groupCreateDateByInfo(int offset, int limit, Bill bill, Date dateFrom, Date dateTo) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, bill);
        example.setOffset(offset);
        example.setLimit(limit);
        if (dateFrom != null && dateTo != null) {
            criteria.andCreateTimeBetween(dateFrom, dateTo);
        }
        return billMapper.groupCreateDateSumAmountByExample(example);
    }

    public int countGroupCreateDateByInfo(Bill bill, Date dateFrom, Date dateTo) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, bill);
        if (dateFrom != null && dateTo != null) {
            criteria.andCreateTimeBetween(dateFrom, dateTo);
        }
        return billMapper.countGroupCreateDateSumAmountByExample(example);
    }

    void setCriteria(BillExample.Criteria criteria, Bill info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (StringUtils.isNotBlank(info.getAgentMchId())) criteria.andAgentMchIdEqualTo(info.getAgentMchId());
            if (StringUtils.isNoneBlank(info.getPayChannelId()))
                criteria.andPayChannelIdEqualTo(info.getPayChannelId());
            if (info.getState() != null && info.getState() != -99) criteria.andStateEqualTo(info.getState());
        }
    }

}
