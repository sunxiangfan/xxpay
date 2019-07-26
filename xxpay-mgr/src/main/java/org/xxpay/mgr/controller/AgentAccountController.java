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
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.DepositAmount;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.dto.DepositGroupMchIdSumAmount;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.AgentService;
import org.xxpay.mgr.service.AmountFlowService;
import org.xxpay.mgr.service.AgentService;
import org.xxpay.mgr.service.DepositAmountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agent_account")
public class AgentAccountController {

    private final static MyLog _log = MyLog.getLog(AgentAccountController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private DepositAmountService depositAmountService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "agent_account/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String mchId, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        model.put("item", item);
        return "agent_account/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute Agent mchInfo, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = agentService.count(mchInfo);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<Agent> list = agentService.getList((page - 1) * limit, limit, mchInfo);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            List<String> mchIds = new ArrayList<>();
            for (Agent mp : list) {
                mchIds.add(mp.getId());
            }
            DepositAmount depositAmountSearch = new DepositAmount();
            depositAmountSearch.setState((byte) 0);
            List<DepositGroupMchIdSumAmount> depositGroupMchIdSumAmounts = depositAmountService.groupMchIdSumAmountByExample(depositAmountSearch, mchIds);

            for (Agent item : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(item);
                object.put("createTime", DateUtil.date2Str(item.getCreateTime()));
                if (item.getBalance() != null)
                    object.put("balance", AmountUtil.convertCent2Dollar(item.getBalance() + ""));
                if (item.getLockAmount() != null)
                    object.put("lockAmount", AmountUtil.convertCent2Dollar(item.getLockAmount() + ""));
                if (item.getCashingAmount() != null)
                    object.put("cashingAmount", AmountUtil.convertCent2Dollar(item.getCashingAmount() + ""));
                Long depositAmount=0L;
                Optional<DepositGroupMchIdSumAmount> depositGroupMchIdSumAmount = depositGroupMchIdSumAmounts.stream().filter(t -> t.getMchId().equals(item.getId())).findFirst();
                if (depositGroupMchIdSumAmount.isPresent()) {
                    depositAmount=depositGroupMchIdSumAmount.get().getSumAmount();
                }
                object.put("depositAmount", AmountUtil.convertCent2Dollar(depositAmount+ ""));
                Long enableCashAmount= BigDecimalUtils.sub(item.getBalance().longValue(),depositAmount.longValue());
                object.put("enableCashAmount",AmountUtil.convertCent2Dollar(enableCashAmount+""));
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping("/lock_amount.html")
    public String lockAmountInput(String mchId, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getBalance() != null) object.put("balance", AmountUtil.convertCent2Dollar(item.getBalance() + ""));
        if (item.getLockAmount() != null)
            object.put("lockAmount", AmountUtil.convertCent2Dollar(item.getLockAmount() + ""));
        model.put("item", object);
        return "agent_account/lock_amount";
    }

    @RequestMapping(value = "/lock_amount", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult lockAmount(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String mchId = po.getString("mchId");
            Assert.isTrue(StringUtils.isNoneBlank(mchId), "商户号不能为空！");
            String strAmount = po.getString("amount");
            Assert.isTrue(StringUtils.isNoneBlank(strAmount), "冻结金额不能为空！");
            Long amount = Long.parseLong(AmountUtil.convertDollar2Cent(strAmount));
            Assert.isTrue(amount.compareTo(0L) > 0, "冻结金额必须大于0！");

            String remark = po.getString("remark");
            Assert.isTrue(StringUtils.isNotBlank(remark), "冻结原因不能为空！");
            agentService.lockAmount(mchId, amount, remark);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            result = SimpleResult.buildFailRes("保存失败！详情：" + ex.getMessage());
        }
        return result;
    }


    @RequestMapping("/unlock_amount.html")
    public String unlockAmountInput(String mchId, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getBalance() != null) object.put("balance", AmountUtil.convertCent2Dollar(item.getBalance() + ""));
        if (item.getLockAmount() != null)
            object.put("lockAmount", AmountUtil.convertCent2Dollar(item.getLockAmount() + ""));
        model.put("item", object);
        return "agent_account/unlock_amount";
    }

    @RequestMapping(value = "/unlock_amount", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult unlockAmount(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String mchId = po.getString("mchId");
            Assert.isTrue(StringUtils.isNoneBlank(mchId), "商户号不能为空！");
            String strAmount = po.getString("amount");
            Assert.isTrue(StringUtils.isNoneBlank(strAmount), "解冻金额不能为空！");
            Long amount = Long.parseLong(AmountUtil.convertDollar2Cent(strAmount));
            Assert.isTrue(amount.compareTo(0L) > 0, "解冻金额必须大于0！");

            String remark = po.getString("remark");
            Assert.isTrue(StringUtils.isNotBlank(remark), "解冻原因不能为空！");
            agentService.unlockAmount(mchId, amount, remark);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            result = SimpleResult.buildFailRes("保存失败！详情：" + ex.getMessage());
        }
        return result;
    }

    @RequestMapping("/view.html")
    public String viewInput(String mchId, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        model.put("item", item);
        return "agent_account/view";
    }

    /**
     * 手工补入账页面
     * @param mchId
     * @param model
     * @return
     */
    @RequestMapping("/adjust_increase_amount.html")
    public String increaseAmountInput(String mchId, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getBalance() != null) object.put("balance", AmountUtil.convertCent2Dollar(item.getBalance() + ""));
        if (item.getLockAmount() != null)
            object.put("lockAmount", AmountUtil.convertCent2Dollar(item.getLockAmount() + ""));
        model.put("item", object);
        return "agent_account/adjust_increase_amount";
    }

    /**
     * 手工补入账提交
     * @param params
     * @return
     */
    @RequestMapping(value = "/adjust_increase_amount", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult adjustIncreaseAmount(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String mchId = po.getString("mchId");
            Assert.isTrue(StringUtils.isNoneBlank(mchId), "商户号不能为空！");
            String strAmount = po.getString("amount");
            Assert.isTrue(StringUtils.isNoneBlank(strAmount), "金额不能为空！");
            Long amount = Long.parseLong(AmountUtil.convertDollar2Cent(strAmount));
            Assert.isTrue(amount.compareTo(0L) > 0, "金额必须大于0！");

            String remark = po.getString("remark");
            Assert.isTrue(StringUtils.isNotBlank(remark), "原因不能为空！");
            agentService.adjustIncreaseAmount(mchId, amount, remark);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            result = SimpleResult.buildFailRes("保存失败！详情：" + ex.getMessage());
        }
        return result;
    }

    /**
     * 手工补出账页面
     * @param mchId
     * @param model
     * @return
     */
    @RequestMapping("/adjust_decrease_amount.html")
    public String decreaseAmountInput(String mchId, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getBalance() != null) object.put("balance", AmountUtil.convertCent2Dollar(item.getBalance() + ""));
        if (item.getLockAmount() != null)
            object.put("lockAmount", AmountUtil.convertCent2Dollar(item.getLockAmount() + ""));
        model.put("item", object);
        return "agent_account/adjust_decrease_amount";
    }

    /**
     * 手工补出账提交
     * @param params
     * @return
     */
    @RequestMapping(value = "/adjust_decrease_amount", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult adjustDecreaseAmount(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String mchId = po.getString("mchId");
            Assert.isTrue(StringUtils.isNoneBlank(mchId), "商户号不能为空！");
            String strAmount = po.getString("amount");
            Assert.isTrue(StringUtils.isNoneBlank(strAmount), "金额不能为空！");
            Long amount = Long.parseLong(AmountUtil.convertDollar2Cent(strAmount));
            Assert.isTrue(amount.compareTo(0L) > 0, "金额必须大于0！");

            String remark = po.getString("remark");
            Assert.isTrue(StringUtils.isNotBlank(remark), "原因不能为空！");
            agentService.adjustDecreaseAmount(mchId, amount, remark);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            result = SimpleResult.buildFailRes("保存失败！详情：" + ex.getMessage());
        }
        return result;
    }
}