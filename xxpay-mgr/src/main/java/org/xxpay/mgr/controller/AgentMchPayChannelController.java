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
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.AgentMchPayChannel;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.*;
import org.xxpay.mgr.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agent_mch_pay_channel")
public class AgentMchPayChannelController {

    private final static MyLog _log = MyLog.getLog(AgentMchPayChannelController.class);

    @Autowired
    private AgentMchPayChannelService agentMchPayChannelService;

    //    @Autowired
//    private MchInfoService mchInfoService;
    @Autowired
    private AgentService agentService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(String mchId, ModelMap model) {
        model.put("mchId", mchId);
        return "agent_mch_pay_channel/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, String mchId, ModelMap model) {
        AgentMchPayChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = agentMchPayChannelService.select(id);
        }
        if (item == null) {
            item = new AgentMchPayChannel();
        } else {
            mchId = item.getMchId();
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getAgentMchCommissionRate() != null)
            object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(item.getAgentMchCommissionRate() + ""));
        model.put("item", object);
        if (StringUtils.isNoneBlank(mchId)) {
            Agent mchInfo = agentService.select(mchId);
            model.put("agent", mchInfo);
        }
        List<PayChannel> payChannels = payChannelService.getAllEnableList();
        model.put("payChannels", payChannels);

        return "agent_mch_pay_channel/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute AgentMchPayChannel info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = agentMchPayChannelService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<AgentMchPayChannel> list = agentMchPayChannelService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            List<String> mchIds = new ArrayList<>();
            List<String> payChannelIds = new ArrayList<>();
            for (AgentMchPayChannel mp : list) {
                mchIds.add(mp.getMchId());
                payChannelIds.add(mp.getPayChannelId());
            }

            List<Agent> agents = agentService.select(mchIds);
            List<PayChannel> payChannels = payChannelService.select(payChannelIds);

            for (AgentMchPayChannel pc : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(pc);
                object.put("createTime", DateUtil.date2Str(pc.getCreateTime()));
                if (pc.getAgentMchCommissionRate() != null)
                    object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(pc.getAgentMchCommissionRate() + ""));
                Optional<Agent> agent = agents.stream().filter(t -> t.getId().equals(pc.getMchId())).findFirst();
                if (agent.isPresent()) {
                    object.put("agent", agent.get());
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
            Agent agent = agentService.select(mchId);
            Assert.notNull(agent, "代理信息未找到！mchId: " + mchId);
            Assert.isTrue(MchState.ENABLE.getCode().equals(agent.getState()), "该商户已停用！");
            String payChannelId = po.getString("payChannelId");
            Assert.isTrue(StringUtils.isNoneEmpty(payChannelId), "通空道id为！");
            AgentMchPayChannel info = new AgentMchPayChannel();
            info.setMchId(mchId);
//            Byte mchType = mchInfo.getType();
//            Assert.isTrue(MchType.MERCHANT.getCode().equals(mchType), "商户类型错误！请选择代理商户！");
            info.setPayChannelId(payChannelId);
            info.setAgentMchCommissionRate(Double.parseDouble(PercentUtils.convertPercent2Decimal(po.getString("agentMchCommissionRate"))));
            info.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            info.setRemark(po.getString("remark"));
            info.setCreateBy(po.getString("createBy"));
            info.setUpdateBy(po.getString("updateBy"));
            String id = po.getString("id");
            int result;
            if (id == null) {
                // 添加
                info.setCreateBy(sessionUtil.getLoginInfo().getUserId());
                result = agentMchPayChannelService.add(info);
            } else {
                // 修改
                info.setId(id);
                result = agentMchPayChannelService.update(info);
            }
            SimpleResult simpleResult = SimpleResult.buildSucRes(null);
            _log.info("保存代理通道记录,返回:{}", result);
            return simpleResult;
        } catch (IllegalArgumentException ex) {
            _log.info("保存代理通道失败，详情：{}", ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }

    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        AgentMchPayChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = agentMchPayChannelService.select(id);
        }
        if (item == null) {
            item = new AgentMchPayChannel();
            model.put("item", item);
            return "mch_passageway/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getAgentMchCommissionRate() != null)
            object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(item.getAgentMchCommissionRate() + ""));
        model.put("item", object);
        String mchId = item.getMchId();
        if (StringUtils.isNoneBlank(mchId)) {
            Agent agent = agentService.select(mchId);
            model.put("agent", agent);
        }
        String payChannelId = item.getPayChannelId();
        PayChannel payChannel = payChannelService.select(payChannelId);
        JSONObject payChannelObject = (JSONObject) JSON.toJSON(payChannel);

        if (payChannel.getMaxTransactionAmount() != null)
            payChannelObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
        if (payChannel.getMinTransactionAmount() != null)
            payChannelObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
        if (payChannel.getThirdDeductionRate() != null)
            payChannelObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));

        model.put("payChannel", payChannelObject);
        return "agent_mch_pay_channel/view";
    }
}