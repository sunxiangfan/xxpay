package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.*;
import org.xxpay.mgr.util.SessionUtil;

import java.util.*;

@Controller
@RequestMapping("/mch_withdraw_history")
public class MchWithdrawHistoryController {

    private final static MyLog _log = MyLog.getLog(MchWithdrawHistoryController.class);

    @Autowired
    private MchWithdrawHistoryService mchWithdrawHistoryService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private CashChannelService cashChannelService;

    @Autowired
    private CashApplyChannelService cashApplyChannelService;

    @RequestMapping("/list.html")
    public String listInput(String mchType, ModelMap model) {
        List<CashChannel> cashChannels = cashChannelService.getAllEnableList();
        model.put("cashChannels", cashChannels);

        model.put("mchType", mchType);
        return "mch_withdraw_history/list";
    }


    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchWithdrawApply info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = mchWithdrawHistoryService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchWithdrawApply> list = mchWithdrawHistoryService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            Set<String> mchIds = new HashSet<String>();
            Set<String> cashChannelIds = new HashSet<>();
            for (MchWithdrawApply po : list) {
                mchIds.add(po.getMchId());
                cashChannelIds.add(po.getCashChannelId());
            }
            List<MchInfo> mchInfos = mchInfoService.select(new ArrayList<>(mchIds));
            List<CashChannel> cashChannels = cashChannelService.select(new ArrayList<>(cashChannelIds));
            JSONArray array = new JSONArray();
            for (MchWithdrawApply mi : list) {
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

                Optional<MchInfo> optionalMchInfo = mchInfos.stream().filter(t -> t.getId().equals(mi.getMchId())).findFirst();
                if (optionalMchInfo.isPresent()) {
                    object.put("mchInfo", optionalMchInfo.get());
                }
                Optional<CashChannel> optionalCashChannel = cashChannels.stream().filter(t -> t.getId().equals(mi.getCashChannelId())).findFirst();
                if (optionalCashChannel.isPresent()) {
                    object.put("cashChannel", optionalCashChannel.get());
                }
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 申请代付
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/apply_cash.html")
    public String applyCashInput(String id, ModelMap model) {
        MchWithdrawApply item = mchWithdrawHistoryService.select(id);
        model.put("id", id);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(item);

        if (item.getApplyAmount() != null)
            jsonObject.put("applyAmount", AmountUtil.convertCent2Dollar(item.getApplyAmount() + ""));
        if (item.getActualAmount() != null)
            jsonObject.put("actualAmount", AmountUtil.convertCent2Dollar(item.getActualAmount() + ""));
        model.put("item", jsonObject);
        List<CashChannel> cashChannels = cashChannelService.getAllEnableList();
        model.put("cashChannels", cashChannels);
        return "mch_withdraw_history/apply_cash";
    }

    @RequestMapping("/apply_cash")
    @ResponseBody
    public SimpleResult applyCash(@RequestParam String params) {
        try {
            JSONObject po = JSONObject.parseObject(params);
            String id = po.getString("id");
            String channelId = po.getString("channelId");
            MchWithdrawApply item = mchWithdrawHistoryService.select(id);
            Assert.notNull(item, "未找到该提现申请记录！");
            String bankUnionCode = po.getString("bankUnionCode");
            item.setBankUnionCode(bankUnionCode);
            CashChannel cashChannel = cashChannelService.select(channelId);
            cashApplyChannelService.apply(item, cashChannel);
            SimpleResult result = SimpleResult.buildSucRes("成功");
            return result;
        } catch (IllegalArgumentException e) {
            SimpleResult result = SimpleResult.buildFailRes("提交失败。详情：" + e.getMessage());
            return result;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
            SimpleResult result = SimpleResult.buildFailRes("提交出错。");
            return result;

        }
    }

    /**
     * 标记为已打款
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/mark_cashed.html")
    public String markCashedInput(String id, ModelMap model) {
        MchWithdrawApply item = mchWithdrawHistoryService.select(id);
        model.put("id", id);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(item);

        if (item.getApplyAmount() != null)
            jsonObject.put("applyAmount", AmountUtil.convertCent2Dollar(item.getApplyAmount() + ""));
        if (item.getActualAmount() != null)
            jsonObject.put("actualAmount", AmountUtil.convertCent2Dollar(item.getActualAmount() + ""));
        model.put("item", jsonObject);
        List<CashChannel> cashChannels = cashChannelService.getAllEnableList();
        model.put("cashChannels", cashChannels);
        return "mch_withdraw_history/mark_cashed";
    }

    @RequestMapping("/mark_cashed")
    @ResponseBody
    public SimpleResult markCashed(@RequestParam String params) {
        try {
            JSONObject po = JSONObject.parseObject(params);
            String id = po.getString("id");
            String channelId = po.getString("channelId");
            MchWithdrawApply item = mchWithdrawHistoryService.select(id);
            Assert.notNull(item, "未找到该提现申请记录！");
            String bankUnionCode = po.getString("bankUnionCode");
            item.setBankUnionCode(bankUnionCode);
            CashChannel cashChannel = cashChannelService.select(channelId);
            cashApplyChannelService.markCashed(item, cashChannel);
            SimpleResult result = SimpleResult.buildSucRes("成功");
            return result;
        } catch (IllegalArgumentException e) {
            SimpleResult result = SimpleResult.buildFailRes("提交失败。详情：" + e.getMessage());
            return result;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
            SimpleResult result = SimpleResult.buildFailRes("提交出错。");
            return result;

        }
    }
}
