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
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.ReserveAmount;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.AgentService;
import org.xxpay.mgr.service.ReserveAmountService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reserve_amount")
public class ReserveAmountController {

    private final static MyLog _log = MyLog.getLog(ReserveAmountController.class);

    @Autowired
    private ReserveAmountService reserveAmountService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        Long balance = reserveAmountService.sumAmount();
        String strBalance = null;
        if (balance != null) {
            strBalance = AmountUtil.convertCent2Dollar(balance + "");
        }
        model.put("balance", strBalance);
        return "reserve_amount/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        ReserveAmount item = null;
        if (StringUtils.isNotBlank(id)) {
            item = reserveAmountService.select(id);
        }
        if (item == null) {
            item = new ReserveAmount();
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getAmount() != null) {
            object.put("amount", AmountUtil.convertCent2Dollar(item.getAmount() + ""));
        }
        if (item.getBalance() != null) {
            object.put("balance", AmountUtil.convertCent2Dollar(item.getBalance() + ""));
        }
        model.put("item", object);
        return "reserve_amount/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute ReserveAmount info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = reserveAmountService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<ReserveAmount> userList = reserveAmountService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (ReserveAmount mi : userList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(mi);
                object.put("createTime", DateUtil.date2Str(mi.getCreateTime()));
                object.put("amount", AmountUtil.convertCent2Dollar(mi.getAmount() + ""));
                object.put("balance", AmountUtil.convertCent2Dollar(mi.getBalance() + ""));
                if (mi.getFlowType() != null)
                    object.put("flowType", AmountFlowType.getInstanceByCode(mi.getFlowType()).getDescription());
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult save(@RequestParam String params) {
        try {
            JSONObject po = JSONObject.parseObject(params);
            ReserveAmount info = new ReserveAmount();
            info.setBalance(0L);
            info.setFlowType(po.getByte("flowType"));
            Assert.notNull(info.getFlowType(), "资金变动类型不能为空！");
            String strAmount = po.getString("amount");
            Assert.isTrue(StringUtils.isNoneBlank(strAmount), "金额不能为空！");
            long amount = Long.parseLong(AmountUtil.convertDollar2Cent(strAmount));
            amount = Math.abs(amount);
            if (info.getFlowType().equals(AmountFlowType.OUT.getCode())) {
                amount = -amount;
            }
            info.setAmount(amount);
            String remark = po.getString("remark");
            Assert.isTrue(StringUtils.isNoneBlank(remark), "备注信息不能为空！");
            info.setRemark(remark);
            int updateRows;
            String id = po.getString("id");
            if (StringUtils.isNoneBlank(id)) {
                info.setId(id);
                updateRows = reserveAmountService.update(info);
            } else {
                info.setId(UUID.randomUUID().toString());
                updateRows = reserveAmountService.add(info);
            }
            _log.info("保存平台备付金记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存平台备付金信息成功！");
            return result;
        } catch (IllegalArgumentException ex) {
            _log.info("保存平台备付金信息记录失败,详情:{}", ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        ReserveAmount item = null;
        if (StringUtils.isNotBlank(id)) {
            item = reserveAmountService.select(id);
        }
        if (item == null) {
            item = new ReserveAmount();
        }
        model.put("item", item);
        return "reserve_amount/view";
    }
}
