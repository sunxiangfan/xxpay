/*
package org.xxpay.service.controller;

import static com.google.common.base.Charsets.UTF_8;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONException;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.HttpClientOkHttp;
import org.xxpay.common.util.MD5Util;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.RSA;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.fuyou.FuyouProperties;
import org.xxpay.service.channel.tengjing.TengjingProperties;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayOrderService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;

@RestController
public class PayChannel4FuYouController {

    private final MyLog _log = MyLog.getLog(PayChannel4FuYouController.class);
    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private FuyouProperties fuyouProperties;

    @Autowired
    private MchInfoService mchInfoService;

    */
/**
     * 富有代付渠道
     *
     * @param
     * @return
     *//*

    @RequestMapping(value = "/pay/channel/fuyou/transfer")
    public String doTransferReq(@RequestParam String jsonParam, HttpServletResponse response) {
        String logPrefix = "【富友代付下单接口】";
        try {
        	//获取基本参数
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            String payOrderId = paramObj.getString("payOrderId");
            PayOrder payOrder = payOrderService.selectPayOrder(payOrderId);
            String accNo = paramObj.getString("acc_no");
            String mchId = payOrder.getMchId();
            
            //获取富友提供配置好的密钥
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            
            //构建请求数据
            Map<String, String> payInfo = doPay(payOrder, accNo);
//            String url = "http://pay.tengjingshop.com/pay-api/recharge";
//            SimpleResultT<String> resultT = null;
            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            int result = payOrderService.updateStatus4Ing(payOrderId, null);
            _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);
            map.put("payParams", payInfo);
            map.put("payUrl", tengjingProperties.getTransferPayUrl());
            try (Response response1 = HttpClientOkHttp.formPost(url, params)) {
                resultT = printPage(response1);
            }
            if (resultT.getCode() != 0) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0111));
            }
            String html = resultT.getData();
            if (StringUtils.isEmpty(html)) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0111));
            }
            try {
                JSONObject jsonObject = JSON.parseObject(html);
                String ret_code = jsonObject.getString("ret_code");
                String ret_msg = jsonObject.getString("ret_msg");
                _log.info("{} >>> 下单失败。三方返回：" + html);
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "ret_code:" + ret_code + ",ret_msg:" + ret_msg, PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0111));
            } catch (JSONException ex) {

            }
            _log.info("{} >>> 下单成功", logPrefix);
            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            map.put("payOrderId", payOrderId);
//                map.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
            int result = payOrderService.updateStatus4Ing(payOrderId, null);
            _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);
//            html = URLEncoder.encode(html, "utf-8");
            map.put("html", html);
            return XXPayUtil.makeRetData(map, resKey);
        } catch (Exception e) {
            _log.error(e, "{}异常", logPrefix);
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }

    private Map<String, String> doPay(PayOrder payOrder, String accNo) {
        Map<String, String> p = new HashMap<String, String>();
        //商户号
        p.put("merid", fuyouProperties.getMchId());
        //请求类型  代付：payforreq 代收：sincomeforreq 
        p.put("reqtype", fuyouProperties.getMerchPrivateKeyText());
        //请求参数
        String xml = buildBizData(payOrder);
        p.put("xml ", xml);
        //校验值 
        String mac = buildPayParams(p);
        p.put("mac ", mac);

        return p;

    }

    // 构建API参数。
    Map<String, String> buildPayParams(Map<String, String> customer) {
        String logPrefix = "【新农行支付统一下单】";
        if (customer == null) customer = ImmutableMap.of();
        String customMerchId = customer.get("merch_id");
        String customBizCode = customer.get("biz_code");
        Map<String, String> params = new TreeMap<String, String>();
        
        String macSource = customer.get("merid")+"|"+fuyouProperties.getPlatPublicKeyText()+"|"+customer.get("payforreq")+"|"+customer.get("xml");
    	System.out.println("加密前" + macSource);
    	String mac = MD5Util.encode(macSource, "UTF-8").toUpperCase();
    	System.out.println(mac);
        String loginUrl = "https://fht-test.fuiou.com/fuMer/req.do";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("merid", "0002900F0345142"));
        params.add(new BasicNameValuePair("reqtype", "payforreq"));
        params.add(new BasicNameValuePair("xml", xml));
        params.add(new BasicNameValuePair("mac", mac));
             
        requestPost(loginUrl,params);
        
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

    String buildBizData(PayOrder payOrder) {
    	
    	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"+
    			"<payforreq>"+
    			"<ver>2.00</ver>"+
    			"<merdt>"+payOrder.get+"</merdt>"+
    			"<orderno>"+payOrder.getId()+"</orderno>"+
    			"<bankno>0102</bankno>"+
    			"<cityno>2900</cityno>"+
    			//"<branchnm>中国银行股份有限公司北京西单支行</branchnm>"+
    			"<accntno>6212261904006115311</accntno>"+
    			"<accntnm>似曾相识</accntnm>"+
    			"<amt>10000</amt>"+//金额 以分为单位  不能有小数点，1块钱 100   1分钱 1
    			//"<txncd>05</txncd>"+
    			//"<projectid>0002900F0345178_20160121_0222</projectid>"+
    			//"<txncd></txncd>"+
    			//"<projectid></projectid>"+
    			"</payforreq>";
        return xml;
        
    }

}
*/
