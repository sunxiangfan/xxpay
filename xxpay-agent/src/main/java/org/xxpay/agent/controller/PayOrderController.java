package org.xxpay.agent.controller;

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
import org.xxpay.agent.service.PayOrderService;
import org.xxpay.agent.util.SessionUtil;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.plugin.PageModel;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pay_order")
public class PayOrderController {

    private final static MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "pay_order/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayOrder payOrder, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        String loginAgentId=sessionUtil.getLoginInfo().getAgentId();
        payOrder.setAgentMchId(loginAgentId);
        int count = payOrderService.count(payOrder);
        if(count <= 0) return JSON.toJSONString(pageModel);
        List<PayOrder> payOrderList = payOrderService.getPayOrderList((page-1)*limit, limit, payOrder);
        if(!CollectionUtils.isEmpty(payOrderList)) {
            JSONArray array = new JSONArray();
            for(PayOrder po : payOrderList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if(po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if(po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount()+""));
                if(po.getSubMchActualAmount() != null) object.put("subMchActualAmount", AmountUtil.convertCent2Dollar(po.getSubMchActualAmount()+""));
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
        if(StringUtils.isNotBlank(id)) {
            item = payOrderService.selectPayOrder(id);
        }
        if(item == null) {
            item = new PayOrder();
            model.put("item", item);
            return "pay_order/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if(item.getPaySuccTime() != null) object.put("paySuccTime", DateUtil.date2Str(new Date(item.getPaySuccTime())));
        if(item.getLastNotifyTime() != null) object.put("lastNotifyTime", DateUtil.date2Str(new Date(item.getLastNotifyTime())));
        if(item.getExpireTime() != null) object.put("expireTime", DateUtil.date2Str(new Date(item.getExpireTime())));
        if(item.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(item.getAmount()+""));
        if(item.getSubMchActualAmount() != null) object.put("subMchActualAmount", AmountUtil.convertCent2Dollar(item.getSubMchActualAmount()+""));
        model.put("item", object);
        return "pay_order/view";
    }


}