package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.BillMapper;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tanghaibo on 19/3/10.
 */
@Component
public class BillService {

    @Autowired
    private BillMapper billMapper;


    public Bill select(String id) {
        return billMapper.selectByPrimaryKey(id);
    }

    public List<Bill> getList(int offset, int limit, Bill info) {
        BillExample example = new BillExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return billMapper.selectByExample(example);
    }

    public Integer count(Bill info) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return billMapper.countByExample(example);
    }

    public Long sumPlatformCommission(Bill info, boolean limitToday) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        if (limitToday) {
            Calendar calendar = Calendar.getInstance();
            Date end = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();
            criteria.andCreateTimeBetween(start, end);
        }
        return billMapper.sumPlatformCommissionByExample(example);
    }

    void setCriteria(BillExample.Criteria criteria, Bill info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (StringUtils.isNotBlank(info.getAgentMchId())) criteria.andAgentMchIdEqualTo(info.getAgentMchId());
            if (info.getState() != null && info.getState() != -99) criteria.andStateEqualTo(info.getState());
        }
    }

}
