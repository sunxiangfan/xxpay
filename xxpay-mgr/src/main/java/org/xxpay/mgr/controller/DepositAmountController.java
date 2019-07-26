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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.DepositAmount;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.DepositAmountService;
import org.xxpay.mgr.util.SessionUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public String list(@ModelAttribute DepositAmount info, Integer page, Integer limit, String dateFrom, String dateTo) throws ParseException {
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
        PageModel pageModel = new PageModel();
        int count = depositAmountService.count(info, dateFromValue, dateToValue);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<DepositAmount> list = depositAmountService.getList((page - 1) * limit, limit, info, dateFromValue, dateToValue);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            for (DepositAmount po : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if (po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if (po.getPlanUnlockTime() != null)
                    object.put("planUnlockTime", DateUtil.date2Str(po.getPlanUnlockTime()));
                if (po.getUnlockTime() != null) object.put("unlockTime", DateUtil.date2Str(po.getUnlockTime()));
                if (po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount() + ""));
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping("/unlock")
    @ResponseBody
    public SimpleResult unlock(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            List<String> ids=(List<String>) po.get("ids");
            Assert.isTrue(!CollectionUtils.isEmpty(ids),"缺少保证金记录。");
            int updateRows = depositAmountService.unlock(ids);
            _log.info("解冻保证金记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("解冻保证金成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("解冻保证金记录失败,返回:{}", ex.getMessage());
            result = SimpleResult.buildFailRes("解冻保证金失败！详情：" + ex.getMessage());
        }
        return result;
    }

}