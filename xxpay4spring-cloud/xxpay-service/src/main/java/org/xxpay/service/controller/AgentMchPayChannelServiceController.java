package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.AgentMchPayChannel;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.service.service.AgentMchPayChannelService;
import org.xxpay.service.service.MchPayChannelService;

/**
 * @Description: 代理商户支付渠道接口
 * @author tanghaibo
 * @date 2019-02-26
 * @version V1.0
 */
@RestController
public class AgentMchPayChannelServiceController {

    private final MyLog _log = MyLog.getLog(AgentMchPayChannelServiceController.class);

    @Autowired
    private AgentMchPayChannelService agentMchPayChannelService;

    @RequestMapping(value = "/agent_mch_pay_channel/select")
    public String selectAgentMchPayChannel(@RequestParam String jsonParam) {
        _log.info("selectAgentMchPayChannel << {}", jsonParam);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "0000");
        if (StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        String payChannelId = paramObj.getString("payChannelId");
        String mchId = paramObj.getString("mchId");
        AgentMchPayChannel agentMchPayChannel = agentMchPayChannelService.selectAgentMchPayChannel(payChannelId, mchId);
        if (agentMchPayChannel == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("result", JSON.toJSON(agentMchPayChannel));
        _log.info("selectAgentMchPayChannel >> {}", retObj);
        return retObj.toJSONString();
    }


}
