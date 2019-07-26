package org.xxpay.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.service.BillService;
import org.xxpay.service.service.PayOrderService;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private BillService billService;
    @Autowired
    private PayOrderService payOrderService;

    @RequestMapping("/bill/{payOrderId}")
    public  void testBill(@PathVariable("payOrderId") String payOrderId){

        //PayOrder payOrder=payOrderService.selectPayOrder(payOrderId);
        //billService.computeAmountFlowById(payOrder);
    }
}
