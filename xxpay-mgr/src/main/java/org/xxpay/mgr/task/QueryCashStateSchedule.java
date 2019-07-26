package org.xxpay.mgr.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.mgr.service.CashStateQueryService;

import java.util.Date;

@Component
public class QueryCashStateSchedule {

    @Autowired
    private CashStateQueryService cashStateQueryService;
    /**
     * 每5分钟执行一次
     */
    @Scheduled(fixedDelay=300000)
    public void timer1(){
        System.out.println(new Date());
        cashStateQueryService.query();
    }

}
