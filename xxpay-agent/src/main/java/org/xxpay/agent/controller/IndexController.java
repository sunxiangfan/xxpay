package org.xxpay.agent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xxpay.agent.service.BillService;
import org.xxpay.agent.service.MchInfoService;
import org.xxpay.agent.service.PayOrderService;
import org.xxpay.agent.util.SessionUtil;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.dal.dao.model.Bill;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayOrder;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private BillService billService;

    @RequestMapping("/index.html")
    public String index(ModelMap model) {
        return "index/index";
    }

    @RequestMapping("/main.html")
    public String main(ModelMap model) {
        String agentId=sessionUtil.getLoginInfo().getAgentId();
        //子商户数
        MchInfo mchCondition = new MchInfo();
        mchCondition.setAgentId(agentId);
        int mchCount = mchInfoService.count(mchCondition);
        model.put("mchCount", mchCount);

        //今日交易额
        PayOrder payOrderCondition=new PayOrder();
        payOrderCondition.setAgentMchId(agentId);
        List<Byte> successStatus = new ArrayList<>();
        successStatus.add(PayConstant.PAY_STATUS_SUCCESS);
        successStatus.add(PayConstant.PAY_STATUS_COMPLETE);

        Long todayAmount = payOrderService.sumAmount(payOrderCondition, successStatus, true);
        todayAmount = todayAmount == null ? 0 : todayAmount;
        model.put("todayAmount", AmountUtil.convertCent2Dollar(todayAmount + ""));
        Long totalAmount = payOrderService.sumAmount(payOrderCondition, successStatus, false);
        totalAmount = totalAmount == null ? 0 : totalAmount;
        model.put("totalAmount", AmountUtil.convertCent2Dollar(totalAmount + ""));

        //今日订单数，总订单数
        int todayCount = payOrderService.count(payOrderCondition, successStatus, true);
        model.put("todayCount", todayCount);
        int totalCount = payOrderService.count(payOrderCondition, successStatus, false);
        model.put("totalCount", totalCount);

        //今日分润，总分润
        Bill billCondition=new Bill();
        billCondition.setAgentMchId(agentId);
        Long todayCommission = billService.sumAgentCommission(billCondition, true);
        todayCommission = todayCommission == null ? 0 : todayCommission;
        model.put("todayCommission", AmountUtil.convertCent2Dollar(todayCommission + ""));
        Long totalCommission = billService.sumAgentCommission(billCondition, false);
        totalCommission = totalCommission == null ? 0 : totalCommission;
        model.put("totalCommission", AmountUtil.convertCent2Dollar(totalCommission + ""));
        return "index/main";
    }
}
