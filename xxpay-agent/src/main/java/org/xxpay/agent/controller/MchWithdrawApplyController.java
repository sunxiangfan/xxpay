package org.xxpay.agent.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.agent.service.MchWithdrawApplyService;
import org.xxpay.agent.service.MchInfoService;
import org.xxpay.agent.util.SessionUtil;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.plugin.PageModel;

import java.util.List;

@Controller
@RequestMapping("/mch_withdraw_apply")
public class MchWithdrawApplyController {

    private final static MyLog _log = MyLog.getLog(MchWithdrawApplyController.class);

    @Autowired
    private MchWithdrawApplyService mchWithdrawApplyService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MchInfoService mchInfoService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        String loginMchId = sessionUtil.getLoginInfo().getAgentId();
        Long balance = mchInfoService.selectBalanceById(loginMchId);
        if (balance == null) {
            model.put("balance", 0);
            model.put("effectiveBalance", 0);
        } else {
            String balanceDollor = AmountUtil.convertCent2Dollar(balance + "");
            model.put("balance", balanceDollor);
            model.put("effectiveBalance", balanceDollor);
        }
        return "mch_withdraw_apply/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        MchWithdrawApply item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchWithdrawApplyService.select(id);
        }
        if (item == null) item = new MchWithdrawApply();
        model.put("item", item);
        return "mch_withdraw_apply/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchWithdrawApply info, Integer page, Integer limit) {
        String loginMchId = sessionUtil.getLoginInfo().getAgentId();
        info.setMchId(loginMchId);

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
        SimpleResult result = null;
        try {
            String loginMchId = sessionUtil.getLoginInfo().getAgentId();
            JSONObject po = JSONObject.parseObject(params);
            Long applyAmount = Long.parseLong(AmountUtil.convertDollar2Cent(po.getString("applyAmount")));
            String mchBankCardId = po.getString("mchBankCardId");
            int updateRows = mchWithdrawApplyService.apply(loginMchId, mchBankCardId, applyAmount);
            _log.info("保存提现申请记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("提现申请成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("保存提现申请记录失败,返回:{}", ex.getMessage());
            result = SimpleResult.buildFailRes("提现失败！详情：" + ex.getMessage());
        }
        return result;
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        MchWithdrawApply item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchWithdrawApplyService.select(id);
        }
        if (item == null) item = new MchWithdrawApply();
        model.put("item", item);
        return "mch_withdraw_apply/view";
    }
}
