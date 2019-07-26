package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.merchant.service.MchInfoService;
import org.xxpay.merchant.service.MchPayChannelService;
import org.xxpay.merchant.service.PayChannelService;
import org.xxpay.merchant.util.SessionUtil;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.plugin.PageModel;

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
    private MchInfoService mchInfoService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(String mchId, ModelMap model) {
        model.put("mchId", mchId);
        return "mch_pay_channel/list";
    }


    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchPayChannel info, Integer page, Integer limit) {
        String loginMchId=sessionUtil.getLoginInfo().getMchId();
        info.setMchId(loginMchId);
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
                if(pc.getD0Rate() !=null)
                    object.put("d0Rate",PercentUtils.convertDecimal2Percent(pc.getD0Rate()+""));
                Optional<MchInfo> mchInfo = mchInfos.stream().filter(t -> t.getId().equals(pc.getMchId())).findFirst();
                if (mchInfo.isPresent()) {
                    object.put("mchInfo", mchInfo);
                }
                Optional<PayChannel> passagewayOptional = payChannels.stream().filter(t -> t.getId().equals(pc.getPayChannelId())).findFirst();
                if (passagewayOptional.isPresent()) {
                    PayChannel payChannel = passagewayOptional.get();
                    payChannel.cleanBackgroundInfo();
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
        payChannel.cleanBackgroundInfo();
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