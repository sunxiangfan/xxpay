package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itrus.util.sign.RSAWithSoftware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.minfu.MinFuConfig;
import org.xxpay.service.channel.minfu.MinFuUtil;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Description: 支付渠道接口:敏付
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannel4MinFuController {

    private final MyLog _log = MyLog.getLog(PayChannel4MinFuController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MinFuConfig minFuConfig;

    @Autowired
    private MchInfoService mchInfoService;



    /**
     * 敏付网银支付接口（B2C）
     * 文档：https://merchants.minfupay.cn/merchantUserLogin
     *
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/minfupay")
    public String doMinFuPayB2CReq(@RequestParam String jsonParam) {
        try {
            String logPrefix = "【敏付网银支付下单】";
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
            String payOrderId = payOrder.getId();
            String mchId = payOrder.getMchId();
            String channelId = payOrder.getPayChannelId();
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            PayChannel payChannel = payChannelService.selectPayChannelById(channelId);
            MinFuConfig minFuConfig1 = minFuConfig.getMinFuPayConfig(payChannel.getParam());
            String payType = payChannel.getPayType();
            try {
                Map<String, Object> requestParam = minFuPayBuildUnifiedOrderRequest(payOrder, minFuConfig1);
                //Response r=HttpClientOkHttp.formPost(minFuConfig.getUrl(),requestParam);
                /*String response=HttpClientUtilNew.doPost(minFuConfig.getUrl(),requestParam);
                System.out.println(response);*/
                requestParam.put("payOrderId", payOrder.getId());
                int result = payOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);
                switch (payType) {
                    case PayConstant.PAY_TYPE_GATEWAY: {
                        requestParam.put("payUrl", minFuConfig.getUrl());    // 网关支付链接地址
                        break;
                    }
                }
                return JSONObject.toJSONString(requestParam);
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("{}敏付请求失败：message={}", logPrefix, e.getMessage());
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, null);
                return XXPayUtil.makeRetData(map, resKey);
            }
        }catch (Exception e){
            _log.error(e, "支付统一下单异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }




    /**
     * 构建敏付（网银下单）请求数据
     * @param payOrder
     * @param  minFuConfig
     * @return
     */
    Map<String,Object> minFuPayBuildUnifiedOrderRequest(PayOrder payOrder, MinFuConfig minFuConfig) throws Exception {
        Map<String,Object> requestParam=new HashMap<>();
        requestParam.put("sign",minFuSign(payOrder,minFuConfig,requestParam));
        requestParam.put("merchant_code",minFuConfig.getMchId());
        requestParam.put("sign_type","RSA-S");////TODO 签名定义
        requestParam.put("product_name",payOrder.getSubject());
        requestParam.put("return_url",payOrder.getFrontUrl());
        return requestParam;
    }

    private String minFuSign(PayOrder payOrder,MinFuConfig minFuConfig,Map<String,Object> requestParam) throws Exception {
        /** 数据签名
         签名规则定义如下：
         （1）参数列表中，除去sign_type、sign两个参数外，其它所有非空的参数都要参与签名，值为空的参数不用参与签名；
         （2）签名参数排序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，组成规则如下：
         参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n		*/

        StringBuffer signSrc= new StringBuffer();
        signSrc.append("input_charset=").append("UTF-8").append("&");
        requestParam.put("input_charset","UTF-8");

        signSrc.append("interface_version=").append("V3.0").append("&");
        requestParam.put("interface_version","V3.0");

        signSrc.append("merchant_code=").append(minFuConfig.getMchId()).append("&");
        requestParam.put("merchant_code",minFuConfig.getMchId());
        signSrc.append("notify_url=").append(minFuConfig.getMinFuNotifyUrl()).append("&");
        requestParam.put("notify_url",minFuConfig.getMinFuNotifyUrl());
        signSrc.append("order_amount=").append(payOrder.getAmount()/100).append("&");
        requestParam.put("order_amount",(payOrder.getAmount()/100)+"");//以元为单位
        signSrc.append("order_no=").append(payOrder.getId()).append("&");
        requestParam.put("order_no",payOrder.getId());//交易订单号
        signSrc.append("order_time=").append(payOrder.getCreateTime()==null?DateUtil.date2Str(new Date()):DateUtil.date2Str(payOrder.getCreateTime())).append("&");
        requestParam.put("order_time", payOrder.getCreateTime()==null?DateUtil.date2Str(new Date()):DateUtil.date2Str(payOrder.getCreateTime()));//订单时间
        requestParam.put("pay_type","b2c");
        signSrc.append("pay_type=").append("b2c").append("&");
        requestParam.put("product_name",payOrder.getSubject());
        signSrc.append("product_name=").append(payOrder.getSubject()).append("&");
        requestParam.put("redo_flag","1");
        signSrc.append("redo_flag=").append("1").append("&");

        if (null != payOrder.getFrontUrl() && !"".equals(payOrder.getFrontUrl())) {
            signSrc.append("return_url=").append(payOrder.getFrontUrl()).append("&");
            requestParam.put("return_url",payOrder.getFrontUrl());
        }
        signSrc.append("service_type=").append("direct_pay");
        requestParam.put("service_type","direct_pay");
        String signInfo = signSrc.toString();
        String sign = "" ;
        if("RSA-S".equals("RSA-S")){ // sign_type = "RSA-S"
            //String merchant_private_key ="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANilQBp9xg6KdgB+6jVnIFnMl34jR3sGguvDjAgTLTqYd/FhkSbNl24rbRKDjZ4jqDKxFFUbFlqMd0YuSzhE+Utb1jNcBROTyIn/2O0cCmN0tPFaSgL/ywYhXSjT1FlAWuFbBV+bggj8CLDUpTGm31BofJA/qmg9Kn/wW2aF8QjNAgMBAAECgYEAyALgiNSfeqM4SELjxcPc6SrqngjCIIGlczbI3FegBR3odlBmatWaPZsYCuSrZVl0GsDDjcMBQz21jHSG+38qS0WTxWrMgw/k88ygbfDXWEZQd1v8Em7CDIFN5rZ7InS2GZsDDl5HhBHFKp6eoGug+Xo7Z5O8GokYaGKCdOuVcUECQQD9NRF05NTp0BzGxfVkcWmJeYI23vH+No8nPed4OZSA2gNtpz7mZ2NE7lw05skznf4bVxWTanlynorYD32fejfdAkEA2wjzKggsxfiy4FkPpq9Q04FooQt+W8efD4EOWuYMOVNoOAJYmjzE8YY2XUkaEZ80NHnCJcEZ/UtSX4OqL/f9sQJBANcazTCr8cCL/tZSd8yTmF+krR1mOtiGiwiAS3LUH7dy/jSaPxJHRIrbn9OFN+o0zxl02qx4aKIZ08QHLOZdUrUCQQDagA4a8va/Mv42QYIUdLV7mI+of8+4fOWW0NZiJTUyhprjrKt4iYCps4pN+tuvkpLAemoLwZtMi7QLpkvC+G+xAkAtOkom2PugToM5QiM4MS7puinFV89SEsVQuGvHyyJ/iH8O6igd5XrH1AR7kL879onPTvARTz+Ai7bHq3mUD3Wr";
            sign = RSAWithSoftware.signByPrivateKey(signInfo, minFuConfig.getMchKey());	// 签名   signInfo签名参数排序，  merchant_private_key商户私钥
            System.out.println("RSA-S签名参数排序：" + signInfo.length() + " -->" + signInfo);
            System.out.println("RSA-S签名：" + sign.length() + " -->" + sign + "\n");
        }
        return sign;
    }


    /**
     * 敏付快捷支付接口（快捷短信获取）
     * 文档：https://merchants.minfupay.cn/merchantUserLogin
     *
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/kj/message")
    public String doMinFuPayMessageReq(@RequestParam String jsonParam) {
        try {
            String logPrefix = "【敏付快捷短信验证码获取】";
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
            String mchId = payOrder.getMchId();
            String channelId = payOrder.getPayChannelId();
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            PayChannel payChannel = payChannelService.selectPayChannelById(channelId);
            MinFuConfig minFuConfig1 = minFuConfig.getMinFuPayConfig(payChannel.getParam());
            String payType = payChannel.getPayType();
            //Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            Map<String, Object> map=new HashMap<>();
            try {
                Map<String,Object> requestParam=buildMessageReq(payOrder, minFuConfig1);
                //requestParam.put("payOrderId", payOrder.getId());
                int result = payOrderService.updateStatus4Ing(payOrder.getId(), null);
                _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrder.getId(), result);
                switch (payType) {
                    case PayConstant.PAY_TYPE_GATEWAY: {
                        map.put("payUrl", minFuConfig.getUrl());    // 网关支付链接地址
                        break;
                    }
                }
                switch (payType) {
                    case PayConstant.PAY_TYPE_FAST_PAY: {
                        String resp=HttpClientUtilNew.doPost(minFuConfig1.getMfExpressUrl(),requestParam);
                        Map<String,Object> response=MinFuUtil.parseXml("response",resp);  response.get("is_success");
                        if ("T".equals("T")){
                            requestParam.put("body",payOrder.getBody());
                            requestParam.put("payAmount",payOrder.getAmount());
                            requestParam.put("sms_trade_no",response.get("sms_trade_no"));//
                            map.put("payParams", requestParam);
                        }else {//response.get("error_msg").toString()
                            map=XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, response.get("error_msg").toString() , PayConstant.RETURN_VALUE_FAIL, null);
                        }
                        break;
                    }
                }
                //return XXPayUtil.makeRetData(map, resKey);
                return JSONObject.toJSONString(requestParam);
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("{}敏付快捷支付请求失败：message={}", logPrefix, e.getMessage());
                return XXPayUtil.makeRetData(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, "0111", "调用微信支付失败," + e.getMessage() + ":" + e.getMessage()), resKey);
            }
        }catch (Exception e){
            _log.error(e, "支付统一下单异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }
    private Map<String, Object> buildMessageReq(PayOrder payOrder,MinFuConfig minFuConfig) throws Exception {
        Map<String,Object> requestParam=new HashMap<>();
        messageSign(payOrder,minFuConfig,requestParam);
        requestParam.put("sign_type","RSA-S");////TODO 签名定义
        return requestParam;
    }
    private void messageSign(PayOrder payOrder,MinFuConfig minFuConfig,Map<String,Object> requestParam) throws Exception {
        StringBuffer signSrc= new StringBuffer();
        requestParam.put("bank_code",payOrder.getBankCode());
        requestParam.put("card_type","0");
        requestParam.put("input_charset","UTF-8");
        requestParam.put("interface_version","V3.0");
        requestParam.put("merchant_code",minFuConfig.getMchId());
        requestParam.put("order_amount",(payOrder.getAmount()/100)+"");
        requestParam.put("mobile",payOrder.getMobile());
        requestParam.put("order_no",payOrder.getId());
        requestParam.put("send_type","0");
        requestParam.put("service_type","sign_pay_sms_code");
        requestParam.put("sms_type","1");
        requestParam.put("encrypt_info",(RSAWithSoftware.encryptByPublicKey("622908213034910912"+"|"+"汪慧"+"|"+"412702199401214115",minFuConfig.getMfPubKey())));
        signSrc.append("bank_code=").append(payOrder.getBankCode()).append("&");
        signSrc.append("card_type=").append("0").append("&");
        signSrc.append("encrypt_info=").append(requestParam.get("encrypt_info")).append("&");
        signSrc.append("input_charset=").append("UTF-8").append("&");
        signSrc.append("interface_version=").append("V3.0").append("&");
        signSrc.append("merchant_code=").append(minFuConfig.getMchId()).append("&");
        signSrc.append("mobile=").append(payOrder.getMobile()).append("&");
        signSrc.append("order_amount=").append(requestParam.get("order_amount")).append("&");
        signSrc.append("order_no=").append(payOrder.getId()).append("&");
        signSrc.append("send_type=").append(requestParam.get("send_type")).append("&");
        signSrc.append("service_type=").append("sign_pay_sms_code").append("&");
        signSrc.append("sms_type=").append(requestParam.get("sms_type"));
        String signInfo =signSrc.toString();
        String sign = "" ;
        if("RSA-S".equals("RSA-S")) {
            sign = RSAWithSoftware.signByPrivateKey(signInfo,minFuConfig.getMchKey()) ;  // 商家签名（签名后报文发往dinpay）
            System.out.println("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
            System.out.println("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
        }
        requestParam.put("sign",sign);
    }


    /**
     * 敏付快捷支付接口（快捷确认付款）
     * 文档：https://merchants.minfupay.cn/merchantUserLogin
     *
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/kj/verify")
    public String doMinFuPayMessageVerifyReq(@RequestParam String jsonParam) {
        try {
            String logPrefix = "【快捷确认付款】";
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            Map payOrder = paramObj.getObject("payOrder", Map.class);
            PayOrder payOrder1 = new PayOrder();
            payOrder1.setId(payOrder.get("order_no").toString());
            payOrder1.setMchId("10002");
            payOrder1.setBankCode(payOrder.get("bank_code").toString());
            payOrder1.setAmount(Long.parseLong(payOrder.get("order_amount").toString()));
            payOrder1.setMobile(payOrder.get("mobile").toString());
            String mchId = payOrder1.getMchId();
            PayChannel mchPayChannel = payChannelService.selectMchPayChannelByMchIdAndPayType(mchId, "FAST_PAY");
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            MinFuConfig minFuConfig1 = minFuConfig.getMinFuPayConfig(mchPayChannel.getParam());
            String payType = mchPayChannel.getPayType();
            //Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            Map<String, Object> map=new HashMap<>();
            try {
                Map<String,Object> requestParam=buildMessageVerifyReq(payOrder1, minFuConfig1,payOrder);
                //requestParam.put("payOrderId", payOrder.getId());
                //int result = payOrderService.updateStatus4Ing(payOrder.getId(), null);
                //_log.info("更新第三方支付订单号:payOrderId={},result={}", payOrder.getId(), result);
                String resp=HttpClientUtilNew.doPost(minFuConfig1.getMfExpressUrl(),requestParam);
                Map<String,Object> response=MinFuUtil.parseXml("response",resp); /*response.get("is_success")*/ ;
                if ("T".equals(response.get("is_success"))){
                    map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, response.get("error_msg").toString(), PayConstant.RETURN_VALUE_SUCCESS, null);
                }else {
                    map=XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL,"" , PayConstant.RETURN_VALUE_FAIL, null);
                }
                //return XXPayUtil.makeRetData(map, resKey);
                return JSONObject.toJSONString(map);
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("{}快捷确认付款请求失败：message={}", logPrefix, e.getMessage());
                return XXPayUtil.makeRetData(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, "0111", "调用微信支付失败," + e.getMessage() + ":" + e.getMessage()), resKey);
            }
        }catch (Exception e){
            _log.error(e, "支付统一下单异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }


    private Map<String, Object> buildMessageVerifyReq(PayOrder payOrder,MinFuConfig minFuConfig,Map payOrder1) throws Exception {
        Map<String,Object> requestParam=new HashMap<>();
        messageVerifySign(payOrder,minFuConfig,requestParam,payOrder1);
        requestParam.put("sign_type","RSA-S");
        return requestParam;
    }
    private void messageVerifySign(PayOrder payOrder,MinFuConfig minFuConfig,Map<String,Object> requestParam,Map paramObj) throws Exception {
        StringBuffer signSrc= new StringBuffer();
        requestParam.put("bank_code",payOrder.getBankCode());
        requestParam.put("card_type","0");
        requestParam.put("input_charset","UTF-8");
        requestParam.put("interface_version","V3.0");
        requestParam.put("merchant_code",minFuConfig.getMchId());
        requestParam.put("order_amount",(payOrder.getAmount())+"");
        requestParam.put("mobile",payOrder.getMobile());
        requestParam.put("order_no",payOrder.getId());
        requestParam.put("send_type","0");
        requestParam.put("sms_code",paramObj.get("code"));
        requestParam.put("sms_trade_no",paramObj.get("sms_trade_no"));
        requestParam.put("service_type","express_sign_pay");
        requestParam.put("order_time",DateUtil.date2Str(new Date()));
        requestParam.put("notify_url",minFuConfig.getMinFuNotifyUrl());
        requestParam.put("encrypt_info",paramObj.get("encrypt_info"));
        requestParam.put("product_name",paramObj.get("body"));
        signSrc.append("bank_code=").append(payOrder.getBankCode()).append("&");
        signSrc.append("card_type=").append("0").append("&");
        signSrc.append("encrypt_info=").append(requestParam.get("encrypt_info")).append("&");
        signSrc.append("input_charset=").append("UTF-8").append("&");
        signSrc.append("interface_version=").append("V3.0").append("&");
        signSrc.append("merchant_code=").append(minFuConfig.getMchId()).append("&");
        signSrc.append("mobile=").append(payOrder.getMobile()).append("&");
        signSrc.append("notify_url=").append(minFuConfig.getMinFuNotifyUrl()).append("&");
        signSrc.append("order_amount=").append(requestParam.get("order_amount")).append("&");
        signSrc.append("order_no=").append(payOrder.getId()).append("&");
        signSrc.append("order_time=").append(requestParam.get("order_time")).append("&");
        signSrc.append("product_name=").append(requestParam.get("product_name")).append("&");
        signSrc.append("service_type=").append(requestParam.get("service_type")).append("&");
        signSrc.append("sms_code=").append(paramObj.get("code")).append("&");
        signSrc.append("sms_trade_no=").append(paramObj.get("sms_trade_no"));
        String signInfo =signSrc.toString();
        String sign = "" ;
        if("RSA-S".equals("RSA-S")) {
            sign = RSAWithSoftware.signByPrivateKey(signInfo,minFuConfig.getMchKey()) ;  // 商家签名（签名后报文发往dinpay）
            System.out.println("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
            System.out.println("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
        }
        requestParam.put("sign",sign);
    }



    /**
     * 敏付企业转账接口（企业转账）
     * 文档：https://merchants.minfupay.cn/merchantUserLogin
     *
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/mf/transfer")
    public String doMinFuTransferReq(@RequestParam String jsonParam) {
        try {
            String logPrefix = "【企业转账】";
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
            String mchId = payOrder.getMchId();
            PayChannel mchPayChannel = payChannelService.selectMchPayChannelByMchIdAndPayType(mchId, "TRANSFER");
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            MinFuConfig minFuConfig1 = minFuConfig.getMinFuPayConfig(mchPayChannel.getParam());
            String payType = mchPayChannel.getPayType();
            //Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            Map<String, Object> map=new HashMap<>();
            try {
                Map<String,Object> requestParam=buildDoMinFuTransferReq(payOrder, minFuConfig1);
                //requestParam.put("payOrderId", payOrder.getId());
                //int result = payOrderService.updateStatus4Ing(payOrder.getId(), null);
                //_log.info("更新第三方支付订单号:payOrderId={},result={}", payOrder.getId(), result);
                String resp=HttpClientUtilNew.doPost(minFuConfig1.getMfTransferUrl(),requestParam);
                Map<String,Object> response=MinFuUtil.parseXml("minfupay",resp); /*response.get("is_success")*/ ;
                if ("0".equals(response.get("result_code"))){
                    //正常返回
                    map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, response.get("recv_info").toString(), PayConstant.RETURN_VALUE_SUCCESS, null);
                }else {
                    map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, response.get("recv_info").toString(), PayConstant.RETURN_VALUE_FAIL, null);
                }
                //return XXPayUtil.makeRetData(map, resKey);
                return JSONObject.toJSONString(map);
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("{}企业转账请求失败：message={}", logPrefix, e.getMessage());
                return XXPayUtil.makeRetData(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, "0111", "调用微信支付失败," + e.getMessage() + ":" + e.getMessage()), resKey);
            }
        }catch (Exception e){
            _log.error(e, "企业转账异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }
    private Map<String, Object> buildDoMinFuTransferReq(PayOrder payOrder,MinFuConfig minFuConfig) throws Exception {
        Map<String,Object> requestParam=new HashMap<>();
        transferVerifySign(payOrder,minFuConfig,requestParam);
        requestParam.put("sign_type","RSA-S");
        return requestParam;
    }

    private void transferVerifySign(PayOrder payOrder,MinFuConfig minFuConfig,Map<String,Object> requestParam) throws Exception {
        StringBuffer signSrc = new StringBuffer();
        requestParam.put("interface_version", "V3.1.0");
        requestParam.put("mer_transfer_no", "MF"+payOrder.getId());
        requestParam.put("merchant_no", minFuConfig.getMchId());
        requestParam.put("tran_code", "DMTI");
        requestParam.put("recv_bank_code", payOrder.getBankCode());
        requestParam.put("recv_accno", payOrder.getCardNo());
        requestParam.put("recv_name", payOrder.getBankName());
        requestParam.put("recv_province", payOrder.getProvinceCode());
        requestParam.put("recv_city", payOrder.getBankCity());
        requestParam.put("tran_amount", "2.00");
        requestParam.put("tran_fee_type", "0");
        requestParam.put("tran_type", "0");
        signSrc.append("interface_version=").append("V3.1.0").append("&");
        signSrc.append("mer_transfer_no=").append(requestParam.get("mer_transfer_no")).append("&");
        signSrc.append("merchant_no=").append(minFuConfig.getMchId()).append("&");
        signSrc.append("recv_accno=").append(payOrder.getCardNo()).append("&");
        signSrc.append("recv_bank_code=").append(payOrder.getBankCode()).append("&");
        signSrc.append("recv_city=").append(payOrder.getBankCity()).append("&");
        signSrc.append("recv_name=").append(payOrder.getBankName()).append("&");
        signSrc.append("recv_province=").append(payOrder.getProvinceCode()).append("&");
        signSrc.append("tran_amount=").append("2.00").append("&");
        signSrc.append("tran_code=").append("DMTI").append("&");
        signSrc.append("tran_fee_type=").append("0").append("&"); //0：从转账金额中扣除 1：从账户余额中扣除
        signSrc.append("tran_type=").append("0");//0普通转账
        String signInfo = signSrc.toString();
        String sign = "" ;
        if("RSA-S".equals("RSA-S")) {
            sign = RSAWithSoftware.signByPrivateKey(signInfo,minFuConfig.getMchKey()) ;  // 商家签名（签名后报文发往dinpay）
            _log.info("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
            _log.info("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
        }
        requestParam.put("sign_info",sign);
    }





    public static void main(String[]args) throws Exception {
        System.out.println(RSAWithSoftware.encryptByPublicKey("6217710204724413"+"|"+"汪慧"+"|"+"412702199401214115","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTyvc6wq0i/JmuCYffvY6osN6qxL+Q3ph8GY8ofqbqblgm3mT8q1PaT/ti+oa5eyM1tp8iLPW+L0VskFxUnDEG0Amer0viSFrwqY83y9qa/8BcnillWQCviBjkDqu1NHeYgc9XqyZ10e850uLUhu+WAYnqT7zIQTVbmFCcUOyBSQIDAQAB"));
        //Map<String,Object> re=MinFuUtil.parseXml("response","<?xml version=\"1.0\" encoding=\"UTF-8\" ?><minfupay><response><sign>TO5KENnPfmxJ6De4LMJW9bLf52b1iS0yLnQgrf9BdjRZ6VpccbLnyP/88y3JEbOPjzfLpYWXC2DLoL6/mK1TkhndvfRp+wOnxqY5P9JGXJkvaQLB7eUjJrUioV+a+Sk7+hnu/XxOfy6F+/6NZAOqOjT64+k9z6r99wkiIple0qs=</sign><sms_trade_no>MF1000496605</sms_trade_no><is_success>T</is_success><sign_type>RSA-S</sign_type><merchant_code>100001005118</merchant_code><order_no>P00201906201025560001</order_no></response></minfupay>");
        //System.out.println(re.get("is_success"));
        /*String merchant_private_key="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANRZiyyNczIPAe9G7szCJbiqoKKAl0VY2a4Fnw4n7/LEy4x3qzsGQRXXvBXRATXmja6IoJvXtFwUl3tO\n" +
                "qNK2bgnzyvpchG40QzLTBvVM9KUc9C2Kd7+88dZ5+oxFhSJaXSbxuvF3Whc3Kwi7\n" +
                "J3Da7JXt0jF/kwjuCw00bVXeRgx5AgMBAAECgYBQYolN10oZVBcOZlK1znULIARp\n" +
                "H7aj+MuGlfgtGq85voezaAW49VgkFaPMum7z4ZAW1v+9nv1WsjDZ69/6zDaA+vCL\n" +
                "EEtms90TGpzSYaLxD0dTzNp+MKc2YBI4RLv8Af+7gIcGT4LJdGNhNkicM0YK4pZY\n" +
                "QmHtzsxeiUYJnvcNFQJBAPucdLWoQTsZJfXX+EEwubHJpvH6K8zES0NHRipjqD/S\n" +
                "f5dEmFU93Nx8XlgnzkL8qNa/XL6yrDo/uMpEpK9uSp8CQQDYDcUijBIRwZ+8NzPL\n" +
                "AD8ktIIoHYo/TxRo9QroREENDr1u2M1Gyu+/SuriBni15o4T/wU+lBqc7QcRgQB2\n" +
                "aunnAkBe/m/V0cNm85SwX6ybuDCzneWIwCHfOlEMApHMGToBdpCzZLN6o1OKbu4T\n" +
                "rFukUM3R3ge++6goibxTqVzf2u0TAkArmFu25NygiJlauSjSRXJ4TZQV1CAZNjPA\n" +
                "GfHhfCREJj1DKKwDJN/LkapjgTrKOYNhtYuxXOEh2RIcDhOx0VDxAkA7nhLux8+H\n" +
                "QQfFvUBjOe6FtOYug58osc4+dwe86+Pc3CkYZg5WNJ6+vGM4wvwxP+xYG8QzJYfx\n" +
                "yu4/K/wCYhV0";
        String merchant_publicr_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUWYssjXMyDwHvRu7MwiW4qqCi\n" +
                "gJdFWNmuBZ8OJ+/yxMuMd6s7BkEV17wV0QE15o2uiKCb17RcFJd7TqjStm4J88r6\n" +
                "XIRuNEMy0wb1TPSlHPQtine/vPHWefqMRYUiWl0m8brxd1oXNysIuydw2uyV7dIx\n" +
                "f5MI7gsNNG1V3kYMeQIDAQAB";
        StringBuffer signSrc= new StringBuffer();
        Map<String ,Object>req=new HashMap<>();
        req.put("input_charset","UTF-8");
        req.put("sign_type","RSA-S");
        req.put("interface_version","V3.0");
        req.put("merchant_code","100001005118");
        req.put("merchant_sign_id","8e8062fce7a47afb");
        req.put("mobile","13651969037");
        req.put("order_amount","0.01");
        req.put("order_no","212124511D");
        req.put("send_type","0");
        req.put("service_type","sign_pay_sms_code");
        req.put("sms_type","1");
        signSrc.append("input_charset=").append("UTF-8").append("&");
        signSrc.append("interface_version=").append("V3.0").append("&");
        signSrc.append("merchant_code=").append("100001005118").append("&");
        signSrc.append("merchant_sign_id=").append("8e8062fce7a47afb").append("&");
        signSrc.append("mobile=").append("13651969037").append("&");
        signSrc.append("order_amount=").append("0.01").append("&");
        signSrc.append("order_no=").append("212124511D").append("&");
        signSrc.append("send_type=").append("0").append("&");
        signSrc.append("service_type=").append("sign_pay_sms_code").append("&");
        signSrc.append("sms_type=").append("1");
        String sign=RSAWithSoftware.signByPrivateKey(signSrc.toString(),merchant_private_key);
        req.put("sign",sign);
        Boolean ddddd=RSAWithSoftware.validateSignByPublicKey(signSrc.toString(),merchant_publicr_key,sign);
        System.out.println(ddddd);
        String d=HttpClientUtilNew.doPost("https://api.minfupay.cn/gateway/api/express",req);
        System.out.println(d);*/
    }

}
