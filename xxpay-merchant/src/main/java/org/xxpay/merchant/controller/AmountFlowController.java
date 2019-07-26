package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.AmountFlow;
import org.xxpay.dal.dao.model.DepositAmount;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.merchant.service.AmountFlowService;
import org.xxpay.merchant.service.DepositAmountService;
import org.xxpay.merchant.util.SessionUtil;

import java.util.List;

@Controller
@RequestMapping("/amount_flow")
public class AmountFlowController {

    private final static MyLog _log = MyLog.getLog(AmountFlowController.class);

    @Autowired
    private AmountFlowService amountFlowService;

    @Autowired
    private SessionUtil sessionUtil;


    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "amount_flow/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute AmountFlow info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        String loginSubMchId = sessionUtil.getLoginInfo().getMchId();
        info.setMchId(loginSubMchId);
        int count = amountFlowService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<AmountFlow> list = amountFlowService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            for (AmountFlow po : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if (po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if (po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount() + ""));
                if (po.getPreBalance() != null)
                    object.put("preBalance", AmountUtil.convertCent2Dollar(po.getPreBalance() + ""));
                if (po.getAmountType() != null)
                    object.put("amountType", AmountType.getInstanceByCode(po.getAmountType()).getDescription());
                if(po.getFlowType() !=null)
                    object.put("flowType", AmountFlowType.getInstanceByCode(po.getFlowType().byteValue()).getDescription());
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