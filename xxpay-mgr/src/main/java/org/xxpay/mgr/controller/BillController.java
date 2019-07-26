package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.Bill;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.BillService;
import org.xxpay.mgr.service.MchInfoService;

import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {

    private final static MyLog _log = MyLog.getLog(BillController.class);

    @Autowired
    private BillService billService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "bill/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute Bill mchInfo, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = billService.count(mchInfo);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<Bill> mchInfoList = billService.getList((page - 1) * limit, limit, mchInfo);
        if (!CollectionUtils.isEmpty(mchInfoList)) {
            JSONArray array = new JSONArray();
            for (Bill item : mchInfoList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(item);
                object.put("createTime", DateUtil.date2Str(item.getCreateTime()));
                if(item.getAmount() !=null)
                    object.put("amount",AmountUtil.convertCent2Dollar(item.getAmount()+""));
                if(item.getPlatformActualAmount() !=null)
                    object.put("platformActualAmount",AmountUtil.convertCent2Dollar(item.getPlatformActualAmount()+""));
                if(item.getMchActualAmount() !=null)
                    object.put("mchActualAmount",AmountUtil.convertCent2Dollar(item.getMchActualAmount()+""));
                if(item.getThirdDeductionRate() !=null)
                    object.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(item.getThirdDeductionRate()+""));
                if(item.getThirdDeduction() !=null)
                    object.put("thirdDeduction",AmountUtil.convertCent2Dollar(item.getThirdDeduction()+""));
                if(item.getAgentMchCommissionRate()!=null)
                    object.put("agentMchCommissionRate",PercentUtils.convertDecimal2Percent(item.getAgentMchCommissionRate()+""));
                if(item.getAgentMchCommission() !=null)
                    object.put("agentMchCommission",AmountUtil.convertCent2Dollar(item.getAgentMchCommission()+""));
                if(item.getPlatformDeductionRate()!=null)
                    object.put("platformDeductionRate",PercentUtils.convertDecimal2Percent(item.getPlatformDeductionRate()+""));
                if(item.getPlatformDeduction()!=null)
                    object.put("platformDeduction",AmountUtil.convertCent2Dollar(item.getPlatformDeduction()+""));
                if(item.getPlatformCommissionRate()!=null)
                    object.put("platformCommissionRate",PercentUtils.convertDecimal2Percent(item.getPlatformCommissionRate()+""));
                if(item.getPlatformCommission()!=null)
                    object.put("platformCommission",AmountUtil.convertCent2Dollar(item.getPlatformCommission()+""));

                object.put("payOrderTime", DateUtil.date2Str(item.getPayOrderTime()));
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
    public String viewInput(String mchId, ModelMap model) {
        Bill item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = billService.select(mchId);
        }
        if (item == null) item = new Bill();
        model.put("item", item);
        return "bill/view";
    }


}