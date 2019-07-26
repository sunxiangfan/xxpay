package org.xxpay.agent.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.agent.service.AgentMchPayChannelService;
import org.xxpay.agent.service.AgentService;
import org.xxpay.agent.service.MchInfoService;
import org.xxpay.agent.service.PayChannelService;
import org.xxpay.agent.util.SessionUtil;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.AgentMchPayChannel;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.plugin.PageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agent_mch_pay_channel")
public class AgentMchPayChannelController {

    private final static MyLog _log = MyLog.getLog(AgentMchPayChannelController.class);

    @Autowired
    private AgentMchPayChannelService agentMchPayChannelService;

    @Autowired
    private MchInfoService mchInfoService;

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

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute AgentMchPayChannel info, Integer page, Integer limit) {
        String loginMchId=sessionUtil.getLoginInfo().getAgentId();
        info.setMchId(loginMchId);
        PageModel pageModel = new PageModel();
        int count = agentMchPayChannelService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<AgentMchPayChannel> list = agentMchPayChannelService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            List<String> agentIds = new ArrayList<>();
            List<String> payChannelIds = new ArrayList<>();
            for (AgentMchPayChannel mp : list) {
                agentIds.add(mp.getMchId());
                payChannelIds.add(mp.getPayChannelId());
            }

            List<Agent> mchInfos = agentService.select(agentIds);
            List<PayChannel> payChannels = payChannelService.select(payChannelIds);

            for (AgentMchPayChannel pc : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(pc);
                object.put("createTime", DateUtil.date2Str(pc.getCreateTime()));
                if (pc.getAgentMchCommissionRate() != null)
                    object.put("agentMchCommissionRate", PercentUtils.convertDecimal2Percent(pc.getAgentMchCommissionRate() + ""));
                Optional<Agent> agent = mchInfos.stream().filter(t -> t.getId().equals(pc.getMchId())).findFirst();
                if (agent.isPresent()) {
                    object.put("agent", agent.get());
                }
                Optional<PayChannel> passagewayOptional = payChannels.stream().filter(t -> t.getId().equals(pc.getPayChannelId())).findFirst();
                if (passagewayOptional.isPresent()) {
                    PayChannel payChannel = passagewayOptional.get();
                    payChannel.cleanBackgroundInfo();
                    JSONObject payChannelObject = (JSONObject) JSON.toJSON(payChannel);

                    if (payChannel.getMaxTransactionAmount() != null)
                        payChannelObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
                    if (payChannel.getMinTransactionAmount() != null)
                        payChannelObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
                    if (payChannel.getThirdDeductionRate() != null)
                        payChannelObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));

                    object.put("payChannel", payChannelObject);
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
            MchInfo mchInfo = mchInfoService.select(mchId);
            model.put("mchInfo", mchInfo);
        }
        String payChannelId = item.getPayChannelId();
        PayChannel payChannel = payChannelService.select(payChannelId);
        payChannel.cleanBackgroundInfo();
        JSONObject passagewayObject = (JSONObject) JSON.toJSON(payChannel);

        if (payChannel.getMaxTransactionAmount() != null)
            passagewayObject.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMaxTransactionAmount() + ""));
        if (payChannel.getMinTransactionAmount() != null)
            passagewayObject.put("minTransactionAmount", AmountUtil.convertCent2Dollar(payChannel.getMinTransactionAmount() + ""));
        if (payChannel.getThirdDeductionRate() != null)
            passagewayObject.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(payChannel.getThirdDeductionRate() + ""));

        model.put("payChannel", passagewayObject);
        return "agent_mch_pay_channel/view";
    }
}