package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.service.service.MchPayChannelService;
import org.xxpay.service.service.PayChannelService;

/**
 * @Description: 商户支付渠道接口
 * @author tanghaibo
 * @date 2019-02-26
 * @version V1.0
 */
@RestController
public class MchPayChannelServiceController {

    private final MyLog _log = MyLog.getLog(MchPayChannelServiceController.class);

    @Autowired
    private MchPayChannelService mchPayChannelService;

    @RequestMapping(value = "/mch_pay_channel/select")
    public String selectMchPayChannel(@RequestParam String jsonParam) {
        _log.info("selectMchPayChannel << {}", jsonParam);
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
        MchPayChannel mchPayChannel = mchPayChannelService.selectMchPayChannel(payChannelId, mchId);
        if (mchPayChannel == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("result", JSON.toJSON(mchPayChannel));
        _log.info("selectMchPayChannel >> {}", retObj);
        return retObj.toJSONString();
    }

}
