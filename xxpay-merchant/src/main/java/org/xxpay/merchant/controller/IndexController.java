package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.model.dto.PayOrderGroupCreateDateSumAmountModel;
import org.xxpay.merchant.service.MchInfoService;
import org.xxpay.merchant.service.PayOrderService;
import org.xxpay.merchant.util.SessionUtil;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class IndexController {

    private final static MyLog _log = MyLog.getLog(IndexController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private MchInfoService mchInfoService;

    @RequestMapping("/index.html")
    public String index(ModelMap model) {
        return "index/index";
    }

    @RequestMapping("/main.html")
    public String main(ModelMap model) {
        String mchId = sessionUtil.getLoginInfo().getMchId();
        //今日交易额
        List<Byte> successStatus = new ArrayList<>();
        successStatus.add(PayConstant.PAY_STATUS_SUCCESS);
        successStatus.add(PayConstant.PAY_STATUS_COMPLETE);
        PayOrder payOrderSearch = new PayOrder();
        payOrderSearch.setMchId(mchId);

        //今日交易额
        Long todayAmount = payOrderService.sumAmount(payOrderSearch, successStatus, true);
        todayAmount = todayAmount == null ? 0 : todayAmount;
        model.put("todayAmount", AmountUtil.convertCent2Dollar(todayAmount + ""));
        Long totalAmount = payOrderService.sumAmount(payOrderSearch, successStatus, false);
        totalAmount = totalAmount == null ? 0 : totalAmount;
        model.put("totalAmount", AmountUtil.convertCent2Dollar(totalAmount + ""));

        //今日订单数，总订单数
        int todayCount = payOrderService.count(payOrderSearch, successStatus, true);
        model.put("todayCount", todayCount);
        int totalCount = payOrderService.count(payOrderSearch, successStatus, false);
        model.put("totalCount", totalCount);


        List<Byte> notSuccessStatus = new ArrayList<>();
        notSuccessStatus.add(PayConstant.PAY_STATUS_INIT);
        notSuccessStatus.add(PayConstant.PAY_STATUS_PAYING);
        int todayNotSuccessCount = payOrderService.count(payOrderSearch, notSuccessStatus, true);
        model.put("todayNotSuccessCount", todayNotSuccessCount);
        int totalNotSuccessCount = payOrderService.count(payOrderSearch, notSuccessStatus, false);
        model.put("totalNotSuccessCount", totalNotSuccessCount);
        double todayNotSuccessRate = 0;
        if (todayNotSuccessCount > 0) {
            todayNotSuccessRate = new BigDecimal((float) todayNotSuccessCount / (todayNotSuccessCount + todayCount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        double totalNotSuccessRate = 0;
        if (totalNotSuccessCount > 0) {
            totalNotSuccessRate = new BigDecimal((float) totalNotSuccessCount / (totalNotSuccessCount + totalCount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        model.put("todayNotSuccessRate", PercentUtils.convertDecimal2Percent(todayNotSuccessRate + ""));
        model.put("totalNotSuccessRate", PercentUtils.convertDecimal2Percent(totalNotSuccessRate + ""));
        return "index/main";
    }

    @RequestMapping("/change_password.html")
    public String changePasswordInput() {
        return "index/change_password";
    }

    @RequestMapping("/change_password")
    @ResponseBody
    public SimpleResult changePasswod(String oldPassword, String newPassword, String repeatPassword) {
        try {
            Assert.notNull(oldPassword, "原始密码不能为空！");
            Assert.notNull(newPassword, "新密码不能为空！");
            Assert.notNull(repeatPassword, "重复新密码不能为空！");
            Assert.isTrue(newPassword.length()>=6,"为了您的账号资金安全，新密码长度不能少于6位");
            Assert.isTrue(newPassword.equals(repeatPassword), "新密码和重复密码不一致！");
            String mchId = sessionUtil.getLoginInfo().getMchId();
            mchInfoService.changePassword(mchId, oldPassword, newPassword);
            _log.info("修改密码成功！mchId: " + mchId);
            return  SimpleResult.buildSucRes("修改密码成功！");
        } catch (IllegalArgumentException e) {
            return  SimpleResult.buildFailRes("修改密码失败！详情："+e.getMessage());
        } catch (Exception e) {
            _log.error("修改密码出错。", e);
            return SimpleResult.buildFailRes("修改密码失败。请稍等再试。");
        }
    }

    @RequestMapping("/query_pay_order_statistics")
    @ResponseBody
    public String queryPayOrderStatistics(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date end=calendar.getTime();
        calendar.add(Calendar.DATE,-7);
        Date start=calendar.getTime();
        PayOrder payOrderCondition=new PayOrder();
        payOrderCondition.setMchId(sessionUtil.getLoginInfo().getMchId());
        List<PayOrderGroupCreateDateSumAmountModel> totalPayOrderInfos= payOrderService.groupCreateDateSumAmount(payOrderCondition,null,start,end);
        List<Byte> successStatus=new ArrayList<>();
        successStatus.add(PayConstant.PAY_STATUS_SUCCESS);
        successStatus.add(PayConstant.PAY_STATUS_COMPLETE);
        List<PayOrderGroupCreateDateSumAmountModel> successPayOrderInfos=payOrderService.groupCreateDateSumAmount(payOrderCondition,successStatus,start,end);
        calendar.setTime(end);
        calendar.add(Calendar.DATE,-1);
        end=calendar.getTime();
        List<Date> dates= DateUtil.getDateInStartEnd(start,end);
        JSONObject result=new JSONObject();
        JSONArray xAxisData=new JSONArray();
        JSONObject seriesData=new JSONObject();
        JSONArray totalPayOrderInfoArr=new JSONArray();
        JSONArray successPayOrderInfoArr=new JSONArray();
        for (Date date: dates){
            xAxisData.add(DateUtil.date2Str(date,DateUtil.FORMAT_YYYY_MM_DD));
            Optional<PayOrderGroupCreateDateSumAmountModel> info= totalPayOrderInfos.stream().filter((item)->item.getCreateDate().equals(date)).findFirst();
            if(info.isPresent()){
                totalPayOrderInfoArr.add(AmountUtil.convertCent2Dollar(info.get().getAmount()+""));
            }
            else{
                totalPayOrderInfoArr.add("0");
            }
            info= successPayOrderInfos.stream().filter((item)->item.getCreateDate().equals(date)).findFirst();
            if(info.isPresent()){
                successPayOrderInfoArr.add(AmountUtil.convertCent2Dollar(info.get().getAmount()+""));
            }
            else{
                successPayOrderInfoArr.add("0");
            }
        }

        result.put("xAxis.data",xAxisData);
        seriesData.put("total",totalPayOrderInfoArr);
        seriesData.put("success",successPayOrderInfoArr);
        result.put("series.data",seriesData);
        return  result.toJSONString();
    }
}
