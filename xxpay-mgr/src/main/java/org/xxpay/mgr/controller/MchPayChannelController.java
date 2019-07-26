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
import org.xxpay.common.enumm.MchState;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.*;
import org.xxpay.mgr.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mch_pay_channel")
public class MchPayChannelController {

    private final static MyLog _log = MyLog.getLog(MchPayChannelController.class);

    @Autowired
    private MchPayChannelService mchPayChannelService;

    @Autowired
    private AgentMchPayChannelService agentMchPayChannelService;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(String mchId, String mchType, ModelMap model) {
        model.put("mchId", mchId);
        model.put("mchType",mchType);
        return "mch_pay_channel/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, String mchId, ModelMap model) {
        MchPayChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchPayChannelService.select(id);
        }
        if (item == null) {
            item = new MchPayChannel();
        } else {
            mchId = item.getMchId();
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getPlatformDeductionRate() != null)
            object.put("platformDeductionRate", PercentUtils.convertDecimal2Percent(item.getPlatformDeductionRate() + ""));
        if (item.getAgentMchCommissionRate() != null)
            object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(item.getAgentMchCommissionRate() + ""));
        if (item.getD0Rate() != null)
            object.put("d0Rate", PercentUtils.convertDecimal2Percent(item.getD0Rate() + ""));
        model.put("item", object);
        if (StringUtils.isNoneBlank(mchId)) {
            MchInfo mchInfo = mchInfoService.select(mchId);
            model.put("mchInfo", mchInfo);
        }
        List<PayChannel> payChannels = payChannelService.getAllEnableList();
        model.put("payChannels", payChannels);

        return "mch_pay_channel/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchPayChannel info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = mchPayChannelService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchPayChannel> list = mchPayChannelService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            List<String> mchIds = new ArrayList<>();
            List<String> payChannelIds = new ArrayList<>();
            for (MchPayChannel mp : list) {
                mchIds.add(mp.getMchId());
                payChannelIds.add(mp.getPayChannelId());
            }

            List<MchInfo> mchInfos = mchInfoService.select(mchIds);
            List<PayChannel> payChannels = payChannelService.select(payChannelIds);

            for (MchPayChannel pc : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(pc);
                object.put("createTime", DateUtil.date2Str(pc.getCreateTime()));
                if (pc.getPlatformDeductionRate() != null)
                    object.put("platformDeductionRate", PercentUtils.convertDecimal2Percent(pc.getPlatformDeductionRate() + ""));
                if (pc.getAgentMchCommissionRate() != null)
                    object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(pc.getAgentMchCommissionRate() + ""));
                if (pc.getD0Rate() != null)
                    object.put("d0Rate", PercentUtils.convertDecimal2Percent(pc.getD0Rate() + ""));
                Optional<MchInfo> mchInfo = mchInfos.stream().filter(t -> t.getId().equals(pc.getMchId())).findFirst();
                if (mchInfo.isPresent()) {
                    object.put("mchInfo", mchInfo);
                }
                Optional<PayChannel> passagewayOptional = payChannels.stream().filter(t -> t.getId().equals(pc.getPayChannelId())).findFirst();
                if (passagewayOptional.isPresent()) {
                    PayChannel payChannel = passagewayOptional.get();
                    JSONObject passagewayObject = (JSONObject) JSON.toJSON(payChannel);

                    if (payChannel.getMaxTransactionAmount() != null)
                        passagewayObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
                    if (payChannel.getMinTransactionAmount() != null)
                        passagewayObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
                    if (payChannel.getThirdDeductionRate() != null)
                        passagewayObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));

                    object.put("payChannel", passagewayObject);
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


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult save(@RequestParam String params) {
        try {
            JSONObject po = JSONObject.parseObject(params);
            String mchId = po.getString("mchId");
            Assert.isTrue(StringUtils.isNoneEmpty(mchId), "商户id为空！");
            MchInfo mchInfo = mchInfoService.select(mchId);
            Assert.notNull(mchInfo, "商户信息未找到！mchId: " + mchId);
            Assert.isTrue(MchState.ENABLE.getCode().equals(mchInfo.getState()), "该商户已停用！");
            String payChannelId = po.getString("payChannelId");
            Assert.isTrue(StringUtils.isNoneEmpty(payChannelId), "通道id为空！");
            MchPayChannel info = new MchPayChannel();
            info.setMchId(mchId);
            //子商户添加通道需要保证父商户有该通道
            String agentId = mchInfo.getAgentId();
            Assert.isTrue(StringUtils.isNoneBlank(agentId), "子商户的父级商户号不能为空！");
            AgentMchPayChannel searchParentChannel=new AgentMchPayChannel();
            searchParentChannel.setMchId(agentId);
            searchParentChannel.setPayChannelId(payChannelId);
            int parentChannelCount = agentMchPayChannelService.count(searchParentChannel);
            Assert.isTrue(parentChannelCount == 1, "请先为父级商户号添加该通道！父级商户号：" + agentId);
            info.setMchType(MchType.MERCHANT.getCode());
            info.setPayChannelId(payChannelId);
            info.setAgentMchCommissionRate(0.0);
            info.setPlatformDeductionRate( Double.parseDouble(PercentUtils.convertPercent2Decimal(po.getString("platformDeductionRate"))));
            info.setD0Rate( Double.parseDouble(PercentUtils.convertPercent2Decimal(po.getString("d0Rate"))));
            info.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            info.setRemark(po.getString("remark"));
            info.setCreateBy(po.getString("createBy"));
            info.setUpdateBy(po.getString("updateBy"));
            String id = po.getString("id");
            int result;
            if (id == null) {
                // 添加
                info.setCreateBy(sessionUtil.getLoginInfo().getUserId());
                result = mchPayChannelService.add(info);
            } else {
                // 修改
                info.setId(id);
                result = mchPayChannelService.update(info);
            }
            SimpleResult simpleResult = SimpleResult.buildSucRes(null);
            _log.info("保存商户通道记录,返回:{}", result);
            return simpleResult;
        } catch (IllegalArgumentException ex) {
            _log.info("保存商户通道失败，详情：{}", ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }

    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        MchPayChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchPayChannelService.select(id);
        }
        if (item == null) {
            item = new MchPayChannel();
            model.put("item", item);
            return "mch_passageway/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getPlatformDeductionRate() != null)
            object.put("platformDeductionRate", PercentUtils.convertDecimal2Percent(item.getPlatformDeductionRate() + ""));
        if (item.getAgentMchCommissionRate() != null)
            object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(item.getAgentMchCommissionRate() + ""));
        model.put("item", object);
        String mchId = item.getMchId();
        if (StringUtils.isNoneBlank(mchId)) {
            MchInfo mchInfo = mchInfoService.select(mchId);
            model.put("mchInfo", mchInfo);
        }
        String payChannelId = item.getPayChannelId();
        PayChannel payChannel = payChannelService.select(payChannelId);
        JSONObject passagewayObject = (JSONObject) JSON.toJSON(payChannel);

        if (payChannel.getMaxTransactionAmount() != null)
            passagewayObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
        if (payChannel.getMinTransactionAmount() != null)
            passagewayObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
        if (payChannel.getThirdDeductionRate() != null)
            passagewayObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));

        model.put("payChannel", passagewayObject);
        return "mch_pay_channel/view";
    }
}