//package org.xxpay.service.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
//import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
//import com.github.binarywang.wxpay.config.WxPayConfig;
//import com.github.binarywang.wxpay.constant.WxPayConstants;
//import com.github.binarywang.wxpay.exception.WxPayException;
//import com.github.binarywang.wxpay.service.WxPayService;
//import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
//import com.github.binarywang.wxpay.util.SignUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.xxpay.common.constant.PayConstant;
//import org.xxpay.common.constant.PayEnum;
//import org.xxpay.common.util.MyBase64;
//import org.xxpay.common.util.MyLog;
//import org.xxpay.common.util.PayDigestUtil;
//import org.xxpay.common.util.XXPayUtil;
//import org.xxpay.dal.dao.model.MchInfo;
//import org.xxpay.dal.dao.model.PayChannel;
//import org.xxpay.dal.dao.model.PayOrder;
//import org.xxpay.service.channel.blzqzbs.BlzqzbsConfig;
//import org.xxpay.service.channel.wechat.WxPayProperties;
//import org.xxpay.service.channel.wechat.WxPayUtil;
//import org.xxpay.service.service.MchInfoService;
//import org.xxpay.service.service.PayChannelService;
//import org.xxpay.service.service.PayOrderService;
//
//import javax.annotation.Resource;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author tanghaibo
// * @version V1.0
// * @Description: 支付渠道接口:
// * @date 2019-3-25
// * @Copyright: www.xxpay.org
// */
//@RestController
//public class PayChannel4BlzqzbsController {
//
//    private final MyLog _log = MyLog.getLog(PayChannel4BlzqzbsController.class);
//
//    @Autowired
//    private PayOrderService payOrderService;
//
//    @Autowired
//    private PayChannelService payChannelService;
//
//    @Autowired
//    private MchInfoService mchInfoService;
//
//    @Resource
//    private WxPayProperties wxPayProperties;
//
//    @Resource
//    private BlzqzbsConfig blzqzbsConfig;
//
//    /**
//     * 发起微信支付(统一下单)
//     *
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "/pay/channel/wx")
//    public String doWxPayReq(@RequestParam String jsonParam) {
//        try {
//            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
//            PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
//            String tradeType = paramObj.getString("tradeType");
//            String logPrefix = "【微信支付统一下单】";
//            String mchId = payOrder.getMchId();
//            String channelId = payOrder.getPayChannelId();
//            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
//            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
//            if ("".equals(resKey))
//                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
//            PayChannel payChannel = payChannelService.selectPayChannelById(channelId);
//
//            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(payChannel.getParam(), tradeType, wxPayProperties.getCertRootPath(), wxPayProperties.getNotifyUrl());
//            WxPayService wxPayService = new WxPayServiceImpl();
//            wxPayService.setConfig(wxPayConfig);
//            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = buildUnifiedOrderRequest(payOrder, wxPayConfig);
//            String payOrderId = payOrder.getId();
//            WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
//            try {
//                wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
//                _log.info("{} >>> 下单成功", logPrefix);
//                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
//                map.put("payOrderId", payOrderId);
//                map.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
//                int result = payOrderService.updateStatus4Ing(payOrderId, wxPayUnifiedOrderResult.getPrepayId());
//                _log.info("更新第三方支付订单号:payOrderId={},prepayId={},result={}", payOrderId, wxPayUnifiedOrderResult.getPrepayId(), result);
//                switch (tradeType) {
//                    case PayConstant.WxConstant.TRADE_TYPE_NATIVE: {
//                        map.put("codeUrl", wxPayUnifiedOrderResult.getCodeURL());   // 二维码支付链接
//                        break;
//                    }
//                    case PayConstant.WxConstant.TRADE_TYPE_APP: {
//                        Map<String, String> payInfo = new HashMap<>();
//                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//                        String nonceStr = String.valueOf(System.currentTimeMillis());
//                        // APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
//                        String appId = wxPayConfig.getAppId();
//                        Map<String, String> configMap = new HashMap<>();
//                        // 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
//                        String partnerId = wxPayConfig.getMchId();
//                        configMap.put("prepayid", wxPayUnifiedOrderResult.getPrepayId());
//                        configMap.put("partnerid", partnerId);
//                        String packageValue = "Sign=WXPay";
//                        configMap.put("package", packageValue);
//                        configMap.put("timestamp", timestamp);
//                        configMap.put("noncestr", nonceStr);
//                        configMap.put("appid", appId);
//                        // 此map用于客户端与微信服务器交互
//                        payInfo.put("sign", SignUtils.createSign(configMap, wxPayConfig.getMchKey(), null));
//                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
//                        payInfo.put("partnerId", partnerId);
//                        payInfo.put("appId", appId);
//                        payInfo.put("packageValue", packageValue);
//                        payInfo.put("timeStamp", timestamp);
//                        payInfo.put("nonceStr", nonceStr);
//                        map.put("payParams", payInfo);
//                        break;
//                    }
//                    case PayConstant.WxConstant.TRADE_TYPE_JSPAI: {
//                        Map<String, String> payInfo = new HashMap<>();
//                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//                        String nonceStr = String.valueOf(System.currentTimeMillis());
//                        payInfo.put("appId", wxPayUnifiedOrderResult.getAppid());
//                        // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
//                        payInfo.put("timeStamp", timestamp);
//                        payInfo.put("nonceStr", nonceStr);
//                        payInfo.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
//                        payInfo.put("signType", WxPayConstants.SignType.MD5);
//                        payInfo.put("paySign", SignUtils.createSign(payInfo, wxPayConfig.getMchKey(), null));
//                        map.put("payParams", payInfo);
//                        break;
//                    }
//                    case PayConstant.WxConstant.TRADE_TYPE_MWEB: {
//                        map.put("payUrl", wxPayUnifiedOrderResult.getMwebUrl());    // h5支付链接地址
//                        break;
//                    }
//                }
//                return XXPayUtil.makeRetData(map, resKey);
//            } catch (WxPayException e) {
//                _log.error(e, "下单失败");
//                //出现业务错误
//                _log.info("{}下单返回失败", logPrefix);
//                _log.info("err_code:{}", e.getErrCode());
//                _log.info("err_code_des:{}", e.getErrCodeDes());
//                return XXPayUtil.makeRetData(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, "0111", "调用微信支付失败," + e.getErrCode() + ":" + e.getErrCodeDes()), resKey);
//            }
//        } catch (Exception e) {
//            _log.error(e, "微信支付统一下单异常");
//            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
//        }
//    }
//
//    Map<String, Object> unifiedOrder(PayOrder payOrder, PayChannel payChannel,String tradeType) throws UnsupportedEncodingException {
//        String productId=null;
//        String payChannelCode=payChannel.getCode();
//        if(payChannelCode.equals(PayConstant.PAY_CHANNEL_BLZQZBS_FAST_PAY)){
//            productId="8001";
//        }
//        long amount=payOrder.getAmount().longValue();
//        String payOrderId = payOrder.getId();
//        String notifyUrl=blzqzbsConfig.getNotifyUrl();
//        String signKey=blzqzbsConfig.getKey();
//        String url = "http://www.blzqzbs.com:3020/api/pay/create_order";
//
//        JSONObject paramMap = new JSONObject();
//        paramMap.put("mchId", "20000004");                       // 商户ID
//        paramMap.put("appId", "b3babc8cde584366b6402114ed6bbef1");                       // 商户ID
//        paramMap.put("productId", "8001");                       // 商户ID
//        paramMap.put("mchOrderNo", payOrderId);           // 商户订单号
////        paramMap.put("payType", "TRANSFER");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
//        paramMap.put("amount", amount);                          // 支付金额,单位分
//        paramMap.put("currency", "cny");                    // 币种, cny-人民币
//        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
//        paramMap.put("device", "WEB");                      // 设备
//        paramMap.put("subject", "iphone");
//        paramMap.put("body", "iphone x");
//        paramMap.put("returnUrl", notifyUrl);         // 回调URL
//        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
//        paramMap.put("param1", "");                         // 扩展参数1
//        paramMap.put("param2", "");                         // 扩展参数2
//        paramMap.put("extra", "");  // 附加参数
//
//        String reqSign = PayDigestUtil.getSign(paramMap, signKey);
//        paramMap.put("sign", reqSign);   // 签名
//        String reqData = "?params=" + URLEncoder.encode(paramMap.toJSONString(), "utf-8");
//        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
//        String result = XXPayUtil.call4Post(url + reqData);
//        System.out.println("请求支付中心下单接口,响应数据:" + result);
//        Map retMap = JSON.parseObject(result);
//        if ("SUCCESS".equals(retMap.get("retCode"))) {
//            // 验签
//            String checkSign = PayDigestUtil.getSign(retMap, signKey, "sign", "payParams");
//            String retSign = (String) retMap.get("sign");
//            if (checkSign.equals(retSign)) {
//                System.out.println("=========支付中心下单验签成功=========");
//            } else {
//                System.err.println("=========支付中心下单验签失败=========");
//                return null;
//            }
//        }
//        return retMap;
//
//    }
//
//}
