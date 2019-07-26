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
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.HttpClient;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.DepositAmount;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.merchant.service.DepositAmountService;
import org.xxpay.merchant.service.PayChannelService;
import org.xxpay.merchant.service.PayOrderService;
import org.xxpay.merchant.util.SessionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/deposit_amount")
public class DepositAmountController {

    private final static MyLog _log = MyLog.getLog(DepositAmountController.class);

    @Autowired
    private DepositAmountService depositAmountService;

    @Autowired
    private SessionUtil sessionUtil;


    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "deposit_amount/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute DepositAmount info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        String loginSubMchId=sessionUtil.getLoginInfo().getMchId();
        info.setMchId(loginSubMchId);
        int count = depositAmountService.count(info);
        if(count <= 0) return JSON.toJSONString(pageModel);
        List<DepositAmount> list = depositAmountService.getList((page-1)*limit, limit, info);
        if(!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            for(DepositAmount po : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if(po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if(po.getPlanUnlockTime() != null) object.put("planUnlockTime", DateUtil.date2Str(po.getPlanUnlockTime()));
                if(po.getUnlockTime() != null) object.put("unlockTime", DateUtil.date2Str(po.getUnlockTime()));
                if(po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount()+""));
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }


}