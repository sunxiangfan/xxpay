package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.enumm.PayOrderStatusConverter;
import org.xxpay.common.enumm.PayTypeConverter;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.merchant.service.PayChannelService;
import org.xxpay.merchant.service.PayOrderService;
import org.xxpay.merchant.util.SessionUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/pay_order")
public class PayOrderController {

    private final static MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private PayChannelService payChannelService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "pay_order/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayOrder payOrder, Integer page, Integer limit,String dateFrom,String dateTo) throws ParseException {
        PageModel pageModel = new PageModel();
        String loginSubMchId = sessionUtil.getLoginInfo().getMchId();
        payOrder.setMchId(loginSubMchId);
        Date dateFromValue=null,dateToValue=null;
        if(StringUtils.isNoneBlank(dateFrom) && StringUtils.isNoneBlank(dateTo)){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            dateFromValue=simpleDateFormat.parse(dateFrom);
            dateToValue=simpleDateFormat.parse(dateTo);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(dateToValue);
            calendar.add(Calendar.DATE,1);
            dateToValue=calendar.getTime();
        }

        int count = payOrderService.count(payOrder,dateFromValue,dateToValue);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<PayOrder> list = payOrderService.getPayOrderList((page - 1) * limit, limit, payOrder,dateFromValue,dateToValue);
        if (!CollectionUtils.isEmpty(list)) {
            List<String> payChannelIds = new ArrayList<>();
            List<String> agentIds = new ArrayList<>();
            for (PayOrder mp : list) {
                payChannelIds.add(mp.getPayChannelId());
                agentIds.add(mp.getAgentMchId());
            }

            List<PayChannel> payChannels = payChannelService.select(payChannelIds);
            JSONArray array = new JSONArray();
            for (PayOrder po : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if (po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if (po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount() + ""));
                if (po.getSubMchActualAmount() != null)
                    object.put("subMchActualAmount", AmountUtil.convertCent2Dollar(po.getSubMchActualAmount() + ""));
                if (po.getMchOrderNo() != null)
                    object.put("mchOrderNo", "\t" + po.getMchOrderNo());//处理Excel导出时变成科学计数法的bug
                if (po.getStatus() != null)
                    object.put("statusLabel", PayOrderStatusConverter.convert2Label(po.getStatus().byteValue()));
                Optional<PayChannel> optionalPayChannel = payChannels.stream().filter(t -> t.getId().equals(po.getPayChannelId())).findFirst();
                if (optionalPayChannel.isPresent()) {
                    PayChannel payChannel = optionalPayChannel.get();
//                    JSONObject payChannelObject = (JSONObject) JSON.toJSON(payChannel);
                    JSONObject payChannelObject = new JSONObject();
                    payChannelObject.put("payType", payChannel.getPayType());
                    payChannelObject.put("payTypeLabel", PayTypeConverter.convert2Label(payChannel.getPayType()));

//                    if (payChannel.getMaxTransactionAmount() != null)
//                        payChannelObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
//                    if (payChannel.getMinTransactionAmount() != null)
//                        payChannelObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
//                    if (payChannel.getThirdDeductionRate() != null)
//                        payChannelObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));

                    object.put("payChannel", payChannelObject);
                }
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        PayOrder item = null;
        if (StringUtils.isNotBlank(id)) {
            item = payOrderService.selectPayOrder(id);
        }
        if (item == null) {
            item = new PayOrder();
            model.put("item", item);
            return "pay_order/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getPaySuccTime() != null)
            object.put("paySuccTime", DateUtil.date2Str(new Date(item.getPaySuccTime())));
        if (item.getLastNotifyTime() != null)
            object.put("lastNotifyTime", DateUtil.date2Str(new Date(item.getLastNotifyTime())));
        if (item.getExpireTime() != null) object.put("expireTime", DateUtil.date2Str(new Date(item.getExpireTime())));
        if (item.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(item.getAmount() + ""));
        if (item.getSubMchActualAmount() != null)
            object.put("subMchActualAmount", AmountUtil.convertCent2Dollar(item.getSubMchActualAmount() + ""));
        model.put("item", object);
        return "pay_order/view";
    }

    @RequestMapping("/renotify")
    @ResponseBody
    public String renotify(String id) {
        String result = HttpClient.callHttpPost("http://localhost:3030/notify/pay/renotify_mch.htm?payOrderId=" + id);
        return result;
    }

}