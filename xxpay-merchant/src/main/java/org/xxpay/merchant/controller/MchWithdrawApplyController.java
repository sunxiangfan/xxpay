package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.enumm.CashOrderCashStateConverter;
import org.xxpay.common.enumm.CashOrderStateConverter;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.dal.dao.model.dto.CashGroupSumAmountModel;
import org.xxpay.dal.dao.model.dto.CashSumAmountModel;
import org.xxpay.merchant.service.MchInfoService;
import org.xxpay.merchant.service.MchWithdrawApplyService;
import org.xxpay.merchant.util.SessionUtil;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.plugin.PageModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        String loginMchId = sessionUtil.getLoginInfo().getMchId();
        MchWithdrawApply search = new MchWithdrawApply();
        search.setMchId(loginMchId);
        List<CashSumAmountModel> todayList = mchWithdrawApplyService.sumAmountByExample(search, true);
        CashSumAmountModel todayCash = null;
        if (todayList.size() <= 0 || todayList.get(0) == null) {
            todayCash = new CashSumAmountModel();
        } else {
            todayCash = todayList.get(0);
        }
        JSONObject todayObject = (JSONObject) JSON.toJSON(todayCash);
        if (todayCash.getSuccessAmount() != null)
            todayObject.put("successAmount", AmountUtil.convertCent2Dollar(todayCash.getSuccessAmount() + ""));
        if (todayCash.getTodoAmount() != null)
            todayObject.put("todoAmount", AmountUtil.convertCent2Dollar(todayCash.getTodoAmount() + ""));

        model.put("todayCash", todayObject);

        List<CashSumAmountModel> totalList = mchWithdrawApplyService.sumAmountByExample(search, false);
        CashSumAmountModel totalCash = null;
        if (totalList.size() <= 0 || totalList.get(0) == null) {
            totalCash = new CashSumAmountModel();
        } else {
            totalCash = totalList.get(0);
        }
        JSONObject totalObject = (JSONObject) JSON.toJSON(totalCash);
        if (totalCash.getSuccessAmount() != null)
            totalObject.put("successAmount", AmountUtil.convertCent2Dollar(totalCash.getSuccessAmount() + ""));
        if (totalCash.getTodoAmount() != null)
            totalObject.put("todoAmount", AmountUtil.convertCent2Dollar(totalCash.getTodoAmount() + ""));

        model.put("totalCash", totalObject);
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
    public String list(@ModelAttribute MchWithdrawApply info, Integer page, Integer limit, String dateFrom, String dateTo) throws ParseException {
        String loginMchId = sessionUtil.getLoginInfo().getMchId();
        info.setMchId(loginMchId);
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
        int count = mchWithdrawApplyService.count(info, dateFromValue, dateToValue);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchWithdrawApply> userList = mchWithdrawApplyService.getList((page - 1) * limit, limit, info, dateFromValue, dateToValue);
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
                if (mi.getCashSuccTime() != null)
                    object.put("cashSuccTime", DateUtil.date2Str(mi.getCashSuccTime()));
                if (mi.getNumber() != null)
                    object.put("number", "\t" + mi.getNumber());
                if (mi.getMchOrderNo() != null)
                    object.put("mchOrderNo", "\t" + mi.getMchOrderNo());
                if (mi.getIdCard() != null)
                    object.put("idCard", "\t" + mi.getIdCard());
                if (mi.getState() != null)
                    object.put("stateLabel", CashOrderStateConverter.convert2Label(mi.getState().byteValue()));
                if (mi.getCashState() != null)
                    object.put("cashStateLabel", CashOrderCashStateConverter.convert2Label(mi.getCashState().byteValue()));
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
            String loginMchId = sessionUtil.getLoginInfo().getMchId();
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
