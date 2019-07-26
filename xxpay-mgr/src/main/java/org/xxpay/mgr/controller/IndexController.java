package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.dto.CashGroupSumAmountModel;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.model.dto.CashSumAmountModel;
import org.xxpay.dal.dao.model.dto.PayOrderGroupCreateDateSumAmountModel;
import org.xxpay.mgr.service.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class IndexController {
    @RequestMapping("/index.html")
    public String index(ModelMap model) {
        return "index/index";
    }

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private BillService billService;

    @Autowired
    private MchWithdrawHistoryService mchWithdrawHistoryService;

    @Autowired
    private PlatformWithdrawService platformWithdrawService;

    @RequestMapping("/main.html")
    public String main(ModelMap model) {
        //代理商户数
        Agent agentCondition = new Agent();
        int agentCount = agentService.count(agentCondition);
        model.put("agentCount", agentCount);
        //子商户数
        MchInfo mchCondition = new MchInfo();
        int mchCount = mchInfoService.count(mchCondition);
        model.put("mchCount", mchCount);
        //今日交易额
        List<Byte> successStatus = new ArrayList<>();
        successStatus.add(PayConstant.PAY_STATUS_SUCCESS);
        successStatus.add(PayConstant.PAY_STATUS_COMPLETE);

        Long todayAmount = payOrderService.sumAmount(null, successStatus, true);
        todayAmount = todayAmount == null ? 0 : todayAmount;
        model.put("todayAmount", AmountUtil.convertCent2Dollar(todayAmount + ""));
        Long totalAmount = payOrderService.sumAmount(null, successStatus, false);
        totalAmount = totalAmount == null ? 0 : totalAmount;
        model.put("totalAmount", AmountUtil.convertCent2Dollar(totalAmount + ""));

        //今日订单数，总订单数
        int todayCount = payOrderService.count(null, successStatus, true);
        model.put("todayCount", todayCount);
        int totalCount = payOrderService.count(null, successStatus, false);
        model.put("totalCount", totalCount);


        List<Byte> notSuccessStatus = new ArrayList<>();
        notSuccessStatus.add(PayConstant.PAY_STATUS_INIT);
        notSuccessStatus.add(PayConstant.PAY_STATUS_PAYING);
        int todayNotSuccessCount = payOrderService.count(null, notSuccessStatus, true);
        model.put("todayNotSuccessCount", todayNotSuccessCount);
        int totalNotSuccessCount = payOrderService.count(null, notSuccessStatus, false);
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

        //支付分润
        Long todayPayCommission = billService.sumPlatformCommission(null, true);
        todayPayCommission = todayPayCommission == null ? 0 : todayPayCommission;
        model.put("todayPayCommission", AmountUtil.convertCent2Dollar(todayPayCommission + ""));
        Long totalPayCommission = billService.sumPlatformCommission(null, false);
        totalPayCommission = totalPayCommission == null ? 0 : totalPayCommission;
        model.put("totalPayCommission", AmountUtil.convertCent2Dollar(totalPayCommission + ""));

        //代付分润
        List<Byte> cashAuditedStates = new ArrayList<>();
        cashAuditedStates.add((byte) 1);
        Long todayCashCommission = mchWithdrawHistoryService.sumCommissionAmount(null, cashAuditedStates, true);
        todayCashCommission = todayCashCommission == null ? 0 : todayCashCommission;
        model.put("todayCashCommission", AmountUtil.convertCent2Dollar(todayCashCommission + ""));

        Long totalCashCommission = mchWithdrawHistoryService.sumCommissionAmount(null, cashAuditedStates, false);
        totalCashCommission = totalCashCommission == null ? 0 : totalCashCommission;
        model.put("totalCashCommission", AmountUtil.convertCent2Dollar(totalCashCommission + ""));

        //总分润 ( 支付分润 + 代付分润 )
        Long todayCommission = BigDecimalUtils.add(todayPayCommission.longValue(), todayCashCommission.longValue());
        model.put("todayCommission", AmountUtil.convertCent2Dollar(todayCommission + ""));
        Long totalCommission = BigDecimalUtils.add(totalPayCommission.longValue(), totalCashCommission.longValue());
        model.put("totalCommission", AmountUtil.convertCent2Dollar(totalCommission + ""));

        //平台提现金额
        Long totalApplyAmount = platformWithdrawService.sumApplyAmount(null, false);
        totalApplyAmount = totalApplyAmount == null ? 0 : totalApplyAmount;
        model.put("platformWithdraw", AmountUtil.convertCent2Dollar(totalApplyAmount+""));


        Long platformWithdrawTotalThirdDeduction = platformWithdrawService.sumThirdDeduction(null, false);
        platformWithdrawTotalThirdDeduction = platformWithdrawTotalThirdDeduction == null ? 0 : platformWithdrawTotalThirdDeduction;
        model.put("platformWithdrawTotalThirdDeduction", AmountUtil.convertCent2Dollar(platformWithdrawTotalThirdDeduction+""));

        //平台剩余提现金额
        Long surplusPlatformCommission=BigDecimalUtils.sub(totalCommission.longValue(),totalApplyAmount.longValue());
        surplusPlatformCommission=BigDecimalUtils.sub(surplusPlatformCommission.longValue(),platformWithdrawTotalThirdDeduction.longValue());
        model.put("surplusPlatformCommission", AmountUtil.convertCent2Dollar(surplusPlatformCommission+""));


        //商户提现统计
        //今日商户提现统计
        MchWithdrawApply search = new MchWithdrawApply();
        List<CashSumAmountModel> todayList = mchWithdrawHistoryService.sumAmountByExample(search, true);
        CashSumAmountModel todayCash = null;
        if (todayList.size() <= 0 || todayList.get(0) == null) {
            todayCash = new CashSumAmountModel();
        } else {
            todayCash = todayList.get(0);
        }
        JSONObject todayObject = (JSONObject) JSON.toJSON(todayCash);
        if (todayCash.getSuccessAmount() != null)
            todayObject.put("successAmount", AmountUtil.convertCent2Dollar(todayCash.getSuccessAmount() + ""));
        if (todayCash.getTodoAmount() != null)
            todayObject.put("todoAmount", AmountUtil.convertCent2Dollar(todayCash.getTodoAmount() + ""));
        model.put("todayCash", todayObject);

        //总商户提现统计
        List<CashSumAmountModel> totalList = mchWithdrawHistoryService.sumAmountByExample(search, false);
        CashSumAmountModel totalCash = null;
        if (totalList.size() <= 0 || totalList.get(0) == null) {
            totalCash = new CashSumAmountModel();
        } else {
            totalCash = totalList.get(0);
        }
        JSONObject totalObject = (JSONObject) JSON.toJSON(totalCash);
        if (totalCash.getSuccessAmount() != null)
            totalObject.put("successAmount", AmountUtil.convertCent2Dollar(totalCash.getSuccessAmount() + ""));
        if (totalCash.getTodoAmount() != null)
            totalObject.put("todoAmount", AmountUtil.convertCent2Dollar(totalCash.getTodoAmount() + ""));
        model.put("totalCash", totalObject);

        return "index/main";
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
        List<PayOrderGroupCreateDateSumAmountModel> totalPayOrderInfos= payOrderService.groupCreateDateSumAmount(null,null,start,end);
        List<Byte> successStatus=new ArrayList<>();
        successStatus.add(PayConstant.PAY_STATUS_SUCCESS);
        successStatus.add(PayConstant.PAY_STATUS_COMPLETE);
        List<PayOrderGroupCreateDateSumAmountModel> successPayOrderInfos=payOrderService.groupCreateDateSumAmount(null,successStatus,start,end);
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
