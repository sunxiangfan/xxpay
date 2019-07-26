package org.xxpay.web.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description: 网关
 * @date 2019-3-25
 * @Copyright: www.xxpay.org
 */
@Service
public class PayChannel4HuitongService {

    private final MyLog _log = MyLog.getLog(PayChannel4HuitongService.class);

    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS).build();

    public static final MediaType FORM_MEDIA
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static final String logPrefix = "【汇通支付统一下单】";

    private static final String PAY_URL = "";
    private static final String MCH_ID = "";
    private static final String KEY = "";
    private static final String NOTIFY_URL = "http://localhost:3030/notify/pay/huitongPayNotifyRes.htm";

    /**
     * 发起微信支付(统一下单)
     *
     * @param
     * @return
     */
    public Map<String, Object> buildRequestData(JSONObject payOrder) {
        try {
            String payOrderId = payOrder.getString("id");
            String payType = payOrder.getString("payType");
            try {
                Map<String, Object> retMap = unifiedOrder(payOrder);
                _log.info("{} >>> 下单成功", logPrefix);
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
                map.put("payOrderId", payOrderId);
                String type=(String)retMap.get("type");
                switch (type) {
                    case "payUrl": {
                        map.put("type", retMap.get("type"));
                        map.put("payUrl", retMap.get("payUrl"));
                        break;
                    }
                    default: {
                        Map<String, Object> map1 = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "不支持的支付类型", PayConstant.RETURN_VALUE_FAIL, null);
                        return map1;
                    }
                }
                return map;
            } catch (IllegalArgumentException e) {
                _log.info("{}下单返回失败", logPrefix);
                _log.info(e.getMessage());
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, null);
                return map;
            }
        } catch (Exception e) {
            _log.error(e, "支付统一下单异常");
            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, null);
            return map;
        }
    }

    Map<String, Object> unifiedOrder(JSONObject payOrder) throws IOException, IllegalAccessException {
        String apiUrl = PAY_URL;
        String payOrderId = payOrder.getString("id");
        String payType = payOrder.getString("payType");
        Assert.isTrue(payType.equals(PayConstant.PAY_TYPE_ALIPAY_WAP), "无效的支付类型：" + payType);

        String pay_callbackurl = "http://localhost:3030/notify/pay/front_url/" + payOrderId + ".htm";//回调地址

//        Map<String, Object> paramMap = new HashMap<>();
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", MCH_ID);                               // 商户ID
        paramMap.put("appId", "6f9306e057ff4fa28cd83ae6abc1ebcd");                               // 应用ID
        paramMap.put("mchOrderNo", payOrderId);     // 商户订单号
        paramMap.put("productId", "8007");                       // 支付产品
        paramMap.put("amount", "" + payOrder.getLong("amount"));                                // 支付金额,单位分
        paramMap.put("currency", "cny");                            // 币种, cny-人民币
        paramMap.put("clientIp", payOrder.getString("clientIp"));                 // 用户地址, 真实的
        paramMap.put("device", "WEB");                              // 设备
        paramMap.put("subject", payOrder.getString("subject"));
        paramMap.put("body", payOrder.getString("body"));
        paramMap.put("returnUrl", pay_callbackurl);
        paramMap.put("notifyUrl", NOTIFY_URL);                       // 回调URL
        paramMap.put("param1", "");                                 // 扩展参数1
        paramMap.put("param2", "");                                 // 扩展参数2
//        paramMap.put("extra", "{\"a\":\"b\"}");  // 附加参数

        String reqSign = PayDigestUtil.getSign(paramMap, KEY);
        paramMap.put("sign", reqSign);                              // 签名

        _log.info("上游下单请求数据：{}", paramMap);
        // 支付请求返回结果
        String result = null;
//        String paramsString = "";
//        for (String key : paramMap.keySet()) {
//            paramsString += key + "=" + URLEncoder.encode(paramMap.get(key), "utf-8") + "&";
//        }
//        paramsString = paramsString.substring(0, paramsString.length() - 1);

        String reqData = "params=" + URLEncoder.encode(paramMap.toJSONString(), "utf-8");
        _log.info("请求支付中心下单接口,请求数据:" + reqData);
//        String url = payUrl + "/pay/create_order?";
        StringBuffer sb = new StringBuffer();
        sb.append(apiUrl).append("?").append(reqData);

        RequestBody body = RequestBody.create(FORM_MEDIA, "");
        Request request = new Request.Builder()
                .url(sb.toString())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            result = response.body().string();
            if (!response.isSuccessful()) {
                _log.error("通知商户失败。status:{} , body: {}", response.code(), result);
            }
            sb.append(result);
        }
        _log.info("上游下单返回数据：{}" + result);

        Map retMap = JSON.parseObject(result);

        if ("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, KEY, "sign");
            String retSign = (String) retMap.get("sign");
            if (checkSign.equals(retSign)) {
                _log.info("=========支付中心下单验签成功=========");
//                _log.info();
            } else {
                _log.info("=========支付中心下单验签失败=========");
                return null;
            }
        }
        return retMap;

    }

}
