package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.dal.dao.mapper.PlatformWithdrawMapper;
import org.xxpay.dal.dao.model.PlatformWithdraw;
import org.xxpay.dal.dao.model.PlatformWithdrawExample;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PlatformWithdrawService {

    @Autowired
    private PlatformWithdrawMapper platformWithdrawMapper;


    public List<PlatformWithdraw> getList(int offset, int limit, PlatformWithdraw info) {
        PlatformWithdrawExample example = new PlatformWithdrawExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PlatformWithdrawExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return platformWithdrawMapper.selectByExample(example);
    }

    public Integer count(PlatformWithdraw info) {
        PlatformWithdrawExample example = new PlatformWithdrawExample();
        PlatformWithdrawExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return platformWithdrawMapper.countByExample(example);
    }
    public Long sumApplyAmount(PlatformWithdraw info,boolean limitToday) {
        PlatformWithdrawExample example = new PlatformWithdrawExample();
        PlatformWithdrawExample.Criteria criteria = example.createCriteria();
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
        return platformWithdrawMapper.sumApplyAmountByExample(example);
    }
    public Long sumThirdDeduction(PlatformWithdraw info,boolean limitToday) {
        PlatformWithdrawExample example = new PlatformWithdrawExample();
        PlatformWithdrawExample.Criteria criteria = example.createCriteria();
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
        return platformWithdrawMapper.sumThirdDeductionByExample(example);
    }

    void setCriteria(PlatformWithdrawExample.Criteria criteria, PlatformWithdraw info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
        }
    }



}
