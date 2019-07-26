package org.xxpay.web.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PayDigestUtil;
import org.xxpay.common.util.RSA;
import org.xxpay.web.controller.PayOrderController;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Charsets.UTF_8;

@Service
public class PayDsService {

    private final MyLog _log = MyLog.getLog(PayDsService.class);
    private static final String MERCH_ID = "";
    private static final String FAST_PAY_CODE = "";
    private static final String MERCH_PRIVATE_KEY_TEXT = "";



    public Map<String, String> buildRequestData(JSONObject payOrder) throws NoSuchAlgorithmException {

        Map<String, String> bizMap = buildBizData(payOrder);
        Map<String, String> params = buildPayParams(null, bizMap);

        return params;

    }

    // 构建API参数。
    Map<String, String> buildPayParams(Map<String, String> customer, Map<String, String> bizMap) {
        String logPrefix = "【ds支付统一下单】";
        if (customer == null) customer = ImmutableMap.of();
        Map<String, String> params = new TreeMap<String, String>();
        params.put("version", "1.1");
        // 平台分配给商户的商户号
        params.put("merch_id", MERCH_ID);
        // 快捷
        params.put("biz_code", FAST_PAY_CODE);
        // 下单的时间戳：yyyyMMddHHmmss
        params.put("state", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));


        // 将业务参数生成JSON字符串
        String srcBizData = JSON.toJSONString(bizMap);

        // 先放入原业务 JSON 串，待签名后再加密
        params.put("biz_data", srcBizData);
        // 使用商户私钥对业务参数进行签名
        // 生成待签名的字符串
        String content = toQueryString(params);
//        System.out.printf("生成待签名的内容字符串(sign_content)：%s%n", content);
//        System.out.println("NOTICE: 待签名的内容字符串，不要做任何的处理，包括 UrlEncode等");
        PrivateKey privateKey = RSA.toPrivateKey(MERCH_PRIVATE_KEY_TEXT);
        String sign = rsaSign(content, privateKey);

        params.put("sign", sign);
        _log.info("{}对待签名的内容字符串(sign_content:{})进行签名(sign:{})：%s%n", logPrefix, content, sign);

        // 除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
//        String encryptedBizData = AES.encryptToBase64(srcBizData, AES_KEY);
        String b64BizData = Base64.encodeBase64URLSafeString(srcBizData.getBytes(UTF_8));
        // 使用密文替换原业务参数数据
        params.put("biz_data", b64BizData);
//        System.out.printf("原始业务参数(biz_data)进行 base64 编码：%s%n", b64BizData);
        return params;
    }

    String rsaSign(String content, PrivateKey privateKey) {
        return RSA.signBase64(content, privateKey);
    }

    private String toQueryString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder(32);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    Map<String, String> buildBizData(JSONObject payOrder) {
        Map<String, String> bizMap = new TreeMap<String, String>();
        String payOrderId = payOrder.getString("id");
        // 商户生成的订单号
        bizMap.put("out_order_no", payOrderId);
        // 交易金额，单位"分"
//        bizMap.put("amount", payOrder.getString("amount"));
        bizMap.put("amount", payOrder.getString("payAmount"));
//        bizMap.put("bank_code", payBankCode);
        // 商户前端跳转地址
        String pay_callbackurl = "http://localhost:3030/notify/pay/front_url/" + payOrderId + ".htm";//回调地址
        bizMap.put("front_url", pay_callbackurl);
        // 商户服务端回调通知地址
        String notifyUrl = "http://localhost:3030/notify/pay/dsPayNotifyRes.htm";//通知地址
        bizMap.put("notify_url", notifyUrl);
        // 商品名称（标题）
        bizMap.put("subject", payOrder.getString("subject"));
        // 订单或商品的描述
        bizMap.put("body", payOrder.getString("body"));
        // 终端用户IP （可选参数）
        bizMap.put("terminal_ip", payOrder.getString("clientIp"));
        return bizMap;
    }
}
