package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.AgentService;
import org.xxpay.mgr.service.MchInfoService;
import org.xxpay.mgr.service.PayChannelService;
import org.xxpay.mgr.service.PayOrderService;

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
    private MchInfoService mchInfoService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private AgentService agentService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        List<PayChannel> payChannels = payChannelService.getAllList();
        model.put("payChannels", payChannels);
        return "pay_order/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayOrder payOrder, Integer page, Integer limit, String dateFrom, String dateTo) throws ParseException {
        PageModel pageModel = new PageModel();
        Date dateFromValue = null, dateToValue = null;
        if (StringUtils.isNoneBlank(dateFrom) && StringUtils.isNoneBlank(dateTo)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFromValue = simpleDateFormat.parse(dateFrom);
            dateToValue = simpleDateFormat.parse(dateTo);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateToValue);
            calendar.add(Calendar.DATE, 1);
            dateToValue = calendar.getTime();
        }

        int count = payOrderService.count(payOrder, dateFromValue, dateToValue);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<PayOrder> list = payOrderService.getPayOrderList((page - 1) * limit, limit, payOrder, dateFromValue, dateToValue);
        if (!CollectionUtils.isEmpty(list)) {
            List<String> mchIds = new ArrayList<>();
            List<String> payChannelIds = new ArrayList<>();
            List<String> agentIds = new ArrayList<>();
            for (PayOrder mp : list) {
                mchIds.add(mp.getMchId());
                payChannelIds.add(mp.getPayChannelId());
                agentIds.add(mp.getAgentMchId());
            }

            List<MchInfo> mchInfos = mchInfoService.select(mchIds);
            List<PayChannel> payChannels = payChannelService.select(payChannelIds);
            List<Agent> agents = agentService.select(agentIds);
            JSONArray array = new JSONArray();
            for (PayOrder po : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if (po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if (po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount() + ""));
                if (po.getPayAmount() != null) object.put("payAmount", AmountUtil.convertCent2Dollar(po.getPayAmount() + ""));
                if (po.getSubMchActualAmount() != null)
                    object.put("subMchActualAmount", AmountUtil.convertCent2Dollar(po.getSubMchActualAmount() + ""));
                if (po.getMchOrderNo() != null) object.put("mchOrderNo", "\t" + po.getMchOrderNo());
                Optional<MchInfo> mchInfo = mchInfos.stream().filter(t -> t.getId().equals(po.getMchId())).findFirst();
                if (mchInfo.isPresent()) {
                    object.put("mchInfo", mchInfo);
                }
                Optional<Agent> agent = agents.stream().filter(t -> t.getId().equals(po.getAgentMchId())).findFirst();
                if (agent.isPresent()) {
                    object.put("agent", agent);
                }
                Optional<PayChannel> passagewayOptional = payChannels.stream().filter(t -> t.getId().equals(po.getPayChannelId())).findFirst();
                if (passagewayOptional.isPresent()) {
                    PayChannel payChannel = passagewayOptional.get();
                    JSONObject passagewayObject = (JSONObject) JSON.toJSON(payChannel);

                    if (payChannel.getMaxTransactionAmount() != null)
                        passagewayObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
                    if (payChannel.getMinTransactionAmount() != null)
                        passagewayObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
                    if (payChannel.getThirdDeductionRate() != null)
                        passagewayObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));
                    object.put("payChannel", passagewayObject);
                }
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel, SerializerFeature.DisableCircularReferenceDetect);
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