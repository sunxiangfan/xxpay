package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.model.SimpleResultT;
import org.xxpay.mgr.service.MchInfoService;
import org.xxpay.mgr.service.MchWithdrawApplyService;
import org.xxpay.mgr.util.SessionUtil;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.plugin.PageModel;

import java.util.List;

@Controller
@RequestMapping("/mch_withdraw_audit")
public class MchWithdrawAuditController {

    private final static MyLog _log = MyLog.getLog(MchWithdrawAuditController.class);

    @Autowired
    private MchWithdrawApplyService mchWithdrawApplyService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MchInfoService mchInfoService;

    @RequestMapping("/list.html")
    public String listInput(String mchType, ModelMap model) {
        model.put("mchType", mchType);
        return "mch_withdraw_audit/list";
    }

    /**
     * 查询待审批提醒
     * @return
     */
    @RequestMapping("/todo_count")
    @ResponseBody
    public SimpleResultT todoCount() {
        MchWithdrawApply todoStateSearch = new MchWithdrawApply();
        todoStateSearch.setState((byte) 0);
        int todoStateCount = mchWithdrawApplyService.count(todoStateSearch);

        MchWithdrawApply todoCashStateSearch = new MchWithdrawApply();
        todoCashStateSearch.setState((byte) 1);
        todoCashStateSearch.setCashState((byte) 0);
        int todoCashStateCount = mchWithdrawApplyService.count(todoCashStateSearch);
        int totalTodoCount = todoStateCount + todoCashStateCount;
        String msg = "";
        if (todoStateCount > 0) {
            msg += "有" + todoStateCount + "条提现申请未审批！";
        }
        if (todoCashStateCount > 0) {
            msg += "有" + todoCashStateCount + "条代付申请未提交！";
        }
        if (totalTodoCount > 0) {
            msg += "请及时处理！";
        }
        SimpleResultT<Integer> result = SimpleResultT.buildSucRes(msg, totalTodoCount);
        return result;
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchWithdrawApply info, Integer page, Integer limit) {
        info.setState((byte) 0);
        PageModel pageModel = new PageModel();
        int count = mchWithdrawApplyService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchWithdrawApply> userList = mchWithdrawApplyService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (MchWithdrawApply mi : userList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(mi);
                object.put("createTime", DateUtil.date2Str(mi.getCreateTime()));
                if (mi.getAuditTime() != null) object.put("auditTime", DateUtil.date2Str(mi.getAuditTime()));
                object.put("createTime", DateUtil.date2Str(mi.getCreateTime()));
                if (mi.getApplyAmount() != null)
                    object.put("applyAmount", AmountUtil.convertCent2Dollar(mi.getApplyAmount() + ""));
                if (mi.getActualAmount() != null)
                    object.put("actualAmount", AmountUtil.convertCent2Dollar(mi.getActualAmount() + ""));
                if (mi.getPlatformDeduction() != null)
                    object.put("platformDeduction", AmountUtil.convertCent2Dollar(mi.getPlatformDeduction() + ""));
                if (mi.getThirdDeduction() != null)
                    object.put("thirdDeduction", AmountUtil.convertCent2Dollar(mi.getThirdDeduction() + ""));

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
        MchWithdrawApply item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchWithdrawApplyService.select(id);
        }
        if (item == null) item = new MchWithdrawApply();
        model.put("item", item);
        return "mch_withdraw_audit/view";
    }


    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult pass(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String id = po.getString("id");
            int updateRows = mchWithdrawApplyService.audit(id, true, sessionUtil.getLoginInfo().getLoginAccount());
            _log.info("通过提现申请记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("操作成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("通过提现申请通过失败，详情：{}", ex.getMessage());
            result = SimpleResult.buildSucRes("操作失败！详情：" + ex.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult refuse(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String id = po.getString("id");
            int updateRows = mchWithdrawApplyService.audit(id, false, sessionUtil.getLoginInfo().getLoginAccount());
            _log.info("拒绝提现申请记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("操作成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("拒绝提现申请失败，详情：{}", ex.getMessage());
            result = SimpleResult.buildFailRes("操作失败！详情：" + ex.getMessage());
        }
        return result;
    }
}
