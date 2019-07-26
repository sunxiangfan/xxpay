package org.xxpay.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xxpay.dal.dao.mapper.PayOrderMapper;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.model.PayOrderExample;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tanghaibo on 19/3/31.
 */
@Service
public class PayOrderDomainService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    public PayOrder selectPayOrder(String payOrderId) {
        return payOrderMapper.selectByPrimaryKey(payOrderId);
    }

    public List<PayOrder> getPayOrderList(int offset, int limit, PayOrder payOrder) {
        PayOrderExample example = new PayOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return payOrderMapper.selectByExample(example);
    }

    public Integer count(PayOrder payOrder) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return payOrderMapper.countByExample(example);
    }

    public Integer count(PayOrder payOrder,List<Byte> statusIn,boolean limitToday) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        criteria.andStatusIn(statusIn);
        if(limitToday){
            Calendar calendar=Calendar.getInstance();
            Date end=calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            Date start=calendar.getTime();
            criteria.andCreateTimeBetween(start,end);
        }
        return payOrderMapper.countByExample(example);
    }

    public Long sumAmount(PayOrder payOrder,List<Byte> statusIn,boolean limitToday) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        criteria.andStatusIn(statusIn);
        if(limitToday){
            Calendar calendar=Calendar.getInstance();
            Date end=calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            Date start=calendar.getTime();
            criteria.andCreateTimeBetween(start,end);
        }
        return payOrderMapper.sumAmountByExample(example);
    }

    public Long sumMchActualAmount(PayOrder payOrder,List<Byte> statusIn,boolean limitToday) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        criteria.andStatusIn(statusIn);
        if(limitToday){
            Calendar calendar=Calendar.getInstance();
            Date end=calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            Date start=calendar.getTime();
            criteria.andCreateTimeBetween(start,end);
        }
        return payOrderMapper.sumMchActualAmountByExample(example);
    }
    void setCriteria(PayOrderExample.Criteria criteria, PayOrder payOrder) {
        if(payOrder != null) {
            if(StringUtils.isNotBlank(payOrder.getMchId())) criteria.andMchIdEqualTo(payOrder.getMchId());
            if(StringUtils.isNotBlank(payOrder.getId())) criteria.andIdEqualTo(payOrder.getId());
            if(StringUtils.isNotBlank(payOrder.getMchOrderNo())) criteria.andMchOrderNoEqualTo(payOrder.getMchOrderNo());
            if(StringUtils.isNotBlank(payOrder.getChannelOrderNo())) criteria.andChannelOrderNoEqualTo(payOrder.getChannelOrderNo());
            if(payOrder.getStatus() != null && payOrder.getStatus() != -99) criteria.andStatusEqualTo(payOrder.getStatus());
        }
    }


}
