package org.xxpay.service.controller;

import ch.qos.logback.core.joran.spi.XMLUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.model.SimpleResultT;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.tengjing.TengjingProperties;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayOrderService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Charsets.UTF_8;

@RestController
public class PayChannel4TengjingController {

    private final MyLog _log = MyLog.getLog(PayChannel4TengjingController.class);
    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private TengjingProperties tengjingProperties;

    @Autowired
    private MchInfoService mchInfoService;

    /**
     * 发起Tengjing支付(线下转账)
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/pay/channel/tingjing/transfer")
    public String doTransferReq(@RequestParam String jsonParam, HttpServletResponse response) {
        String logPrefix = "【新农行支付统一下单】";
        try {
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            String payOrderId = paramObj.getString("payOrderId");
            PayOrder payOrder = payOrderService.selectPayOrder(payOrderId);
            String accNo = paramObj.getString("acc_no");
            String mchId = payOrder.getMchId();
//            String channelId = payOrder.getPayChannelId();
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            Map<String, String> payInfo = doPay(payOrder, accNo);
//            String url = "http://pay.tengjingshop.com/pay-api/recharge";
//            SimpleResultT<String> resultT = null;
            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            int result = payOrderService.updateStatus4Ing(payOrderId, null);
            _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);
            map.put("payParams", payInfo);
            map.put("payUrl", tengjingProperties.getTransferPayUrl());
            return XXPayUtil.makeRetData(map, resKey);
//            try (Response response1 = HttpClientOkHttp.formPost(url, params)) {
//                resultT = printPage(response1);
//            }
//            if (resultT.getCode() != 0) {
//                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0111));
//            }
//            String html = resultT.getData();
//            if (StringUtils.isEmpty(html)) {
//                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0111));
//            }
//            try {
//                JSONObject jsonObject = JSON.parseObject(html);
//                String ret_code = jsonObject.getString("ret_code");
//                String ret_msg = jsonObject.getString("ret_msg");
//                _log.info("{} >>> 下单失败。三方返回：" + html);
//                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "ret_code:" + ret_code + ",ret_msg:" + ret_msg, PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0111));
//            } catch (JSONException ex) {
//
//            }
//            _log.info("{} >>> 下单成功", logPrefix);
//            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
//            map.put("payOrderId", payOrderId);
////                map.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
//            int result = payOrderService.updateStatus4Ing(payOrderId, null);
//            _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);
////            html = URLEncoder.encode(html, "utf-8");
//            map.put("html", html);
//            return XXPayUtil.makeRetData(map, resKey);
        } catch (Exception e) {
            _log.error(e, "{}异常", logPrefix);
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }

    private Map<String, String> doPay(PayOrder payOrder, String accNo) {
        // RSA 签名方式
        Map<String, String> p = new HashMap<String, String>();
        p.put("merch_id", tengjingProperties.getMchId());
        p.put("biz_code", tengjingProperties.getTransferBizCode());

        Map<String, String> bizMap = buildBizData(payOrder, accNo);
        Map<String, String> params = buildPayParams(p, bizMap);

        return params;

    }

    // 构建API参数。
    Map<String, String> buildPayParams(Map<String, String> customer, Map<String, String> bizMap) {
        String logPrefix = "【新农行支付统一下单】";
        if (customer == null) customer = ImmutableMap.of();
        String customMerchId = customer.get("merch_id");
        String customBizCode = customer.get("biz_code");
        Map<String, String> params = new TreeMap<String, String>();
        params.put("version", "1.1");
        // 平台分配给商户的商户号
        params.put("merch_id", customMerchId);
        // 支付下单
        params.put("biz_code", customBizCode);
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
        PrivateKey privateKey = tengjingProperties.getMchPrivateKey();
        String sign = rsaSign(content, privateKey);

        params.put("sign", sign);
        System.out.printf("{}对待签名的内容字符串(sign_content:{})进行签名(sign:{})：%s%n", logPrefix, content, sign);

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

    Map<String, String> buildBizData(PayOrder payOrder, String accNo) {
        Map<String, String> bizMap = new TreeMap<String, String>();
        // 商户生成的订单号
        bizMap.put("out_order_no", payOrder.getId());
        // 交易金额，单位"分"
        bizMap.put("amount", payOrder.getAmount() + "");
        // 商户前端跳转地址
        bizMap.put("front_url", "http://api.test.net/front.htm");
        // 商户服务端回调通知地址
        String notifyUrl = tengjingProperties.getNotifyUrl();
        bizMap.put("notify_url", notifyUrl);
        // 商品名称（标题）
        bizMap.put("subject", payOrder.getSubject());
        // 订单或商品的描述
        bizMap.put("body", payOrder.getBody());
        // 付款银行帐户（银行卡号）
        bizMap.put("acc_no", accNo);
        // 终端用户IP （可选参数）
        bizMap.put("terminal_ip", payOrder.getClientIp());
        return bizMap;
    }

}
