package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.Xq316Config;
import org.xxpay.service.channel.blzqzbs.BlzqzbsConfig;
import org.xxpay.service.channel.wechat.WxPayProperties;
import org.xxpay.service.channel.wechat.WxPayUtil;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description: 网关
 * @date 2019-3-25
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannel4Xq316Controller {

    private final MyLog _log = MyLog.getLog(PayChannel4Xq316Controller.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MchInfoService mchInfoService;

    @Resource
    private WxPayProperties wxPayProperties;

    @Resource
    private Xq316Config config;

    /**
     * 发起微信支付(统一下单)
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/pay/channel/xq316")
    public String doWxPayReq(@RequestParam String jsonParam) {
        try {
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
            String logPrefix = "【Xq316支付统一下单】";
            String mchId = payOrder.getMchId();
            String channelId = payOrder.getPayChannelId();
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            PayChannel payChannel = payChannelService.selectPayChannelById(channelId);
            String payOrderId = payOrder.getId();
            String payType = payChannel.getPayType();
            try {
                Map<String, Object> retMap = unifiedOrder(payOrder, payChannel);

                _log.info("{} >>> 下单成功", logPrefix);
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
                map.put("payOrderId", payOrderId);
                int result = payOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);

                switch (payType) {
                    case PayConstant.PAY_TYPE_GATEWAY: {
                        map.put("payUrl", retMap.get("payurl"));    // 网关支付链接地址
                        break;
                    }
                }
                return XXPayUtil.makeRetData(map, resKey);
            } catch (IllegalArgumentException e) {
                _log.info("{}下单返回失败", logPrefix);
                _log.info(e.getMessage());
                return XXPayUtil.makeRetData(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, "0111", "调用支付失败," + e.getMessage()), resKey);

            }
        } catch (Exception e) {
            _log.error(e, "支付统一下单异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }

    Map<String, Object> unifiedOrder(PayOrder payOrder, PayChannel payChannel) throws IOException {
        String fxid = config.getMchId();
        String apiUrl = "http://xq316.com/Pay";
        String fxkey = config.getKey();
        String payOrderId = payOrder.getId();

        String fxnotifyurl = config.getNotifyUrl();
        String fxbackurl = config.getNotifyUrl();
        String fxattch = "";
        String fxdesc = payOrder.getBody();
        String fxfee = "10";
        String fxpay = "bank";
        String fxddh = payOrderId; //订单号
        String fxip = payOrder.getClientIp();

        //订单签名
        String fxsign = PayDigestUtil.md5(fxid + fxddh + fxfee + fxnotifyurl + fxkey, "utf-8");
        fxsign = fxsign.toLowerCase();

        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("fxid", fxid);
        reqMap.put("fxddh", fxddh);
        reqMap.put("fxfee", fxfee);
        reqMap.put("fxpay", fxpay);
        reqMap.put("fxnotifyurl", fxnotifyurl);
        reqMap.put("fxbackurl", fxbackurl);
        reqMap.put("fxattch", fxattch);
        reqMap.put("fxdesc", fxdesc);
        reqMap.put("fxip", fxip);
        reqMap.put("fxsign", fxsign);

        // 支付请求返回结果
        String result = null;
        try (Response res1 = HttpClientOkHttp.formPost(apiUrl, reqMap)) {
            result = res1.body().string();
        }
        JSONObject retMap = JSON.parseObject(result);
        Assert.isTrue(retMap.getString("status").equals("1"), "下单失败！详情 :error:" + retMap.get("error"));
        return retMap;

    }

}
