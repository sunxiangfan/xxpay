package org.xxpay.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.enumm.MchState;
import org.xxpay.common.enumm.PayChannelState;
import org.xxpay.common.util.*;
import org.xxpay.web.service.*;
import org.xxpay.web.service.channel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 支付订单, 包括:统一下单,订单查询,补单等接口
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@Controller
public class PayOrderController {

    private final MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    @Autowired
    private PayChannelServiceClient payChannelServiceClient;

    @Autowired
    private MchPayChannelServiceClient mchPayChannelServiceClient;

    @Autowired
    private AgentMchPayChannelServiceClient agentMchPayChannelServiceClient;

    @Autowired
    private PayTypeServiceClient payTypeServiceClient;

    @Autowired
    private PayO9nService payO9nService;

    @Autowired
    private PayO9n2Service payO9n2Service;

    @Autowired
    private PayDsService payDsService;

    @Autowired
    private PayChannel4MzwyqService payChannel4MzwyqService;

    @Autowired
    private PayChannel4HuitongService payChannel4HuitongService;

    @RequestMapping(value = "api/pay/testKj")
    public Object testKj(@RequestParam Map<String, String> params) {
        return payOrderServiceClient.doMinFuMessageVerifyPayReq(getJsonParam("payOrder", params));
    }

    @RequestMapping(value = "api/pay/alipay")
    public Object alipay(@RequestParam Map<String, String> params) {
        return payOrderServiceClient.doALiPayReq(getJsonParam("payOrder", params));
    }

    //测试敏付企业转账
    @RequestMapping(value = "api/pay/transfer")
    public Object testtransfer() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", new Date().getTime());
        map.put("bankCode", "GDB");
        map.put("cardNo", "6214623521001915597");
        map.put("bankName", "王魁圆");
        map.put("provinceCode", "31");
        map.put("bankCity", "2900");
        map.put("amount", "2");
        map.put("mchId", "10007");
        return payOrderServiceClient.doMinFuTransferPayReq(getJsonParam("payOrder", map));
    }

    /**
     * zhifu QUEREN 接口:
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "api/pay/smsconfirm")
    public Object smsconfirm(@RequestBody Map<String, String> params, HttpServletResponse response, ModelMap model) {
        _log.info("###### smsconfirm ######" + JSON.toJSONString(params));
        String resp = payOrderServiceClient.nmg_api_quick_payment_smsconfirm(JSON.toJSONString(params));
        String retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, resp, null, null));
        return writeResponse(response, retMsg);
    }

    @RequestMapping("api/pay/query_order")
    public String query_order(@RequestBody Map<String, Object> params, HttpServletResponse response, ModelMap model) {
        _log.info("query_order:" + JSON.toJSONString(params));
        String resp = payOrderServiceClient.query_order(JSON.toJSONString(params));
        String retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, resp, null, null));
        return writeResponse(response, retMsg);
    }

    /**
     * 统一下单接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "api/pay/create_order")
    public Object payOrder(@RequestParam Map<String, String> params, HttpServletResponse response, ModelMap model) {
        _log.info("###### 开始接收商户统一下单请求 ######");
        String logPrefix = "【商户统一下单】";
        ServiceInstance instance = client.getLocalServiceInstance();
        _log.info("{}/pay/create_order, host:{}, service_id:{}, params:{}", logPrefix, instance.getHost(), instance.getServiceId(), params);
        String retMsg = null;
        try {
            JSONObject po = (JSONObject) JSON.toJSON(params);
            String payType = po.getString("payType");
            JSONObject payOrder = null;
            // 验证参数有效性
            Object object = validateParams(po);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
                return writeResponse(response, retMsg);
            }
            if (object instanceof JSONObject) payOrder = (JSONObject) object;
            if (payOrder == null) {
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心下单失败", null, null));
                return writeResponse(response, retMsg);
            }
            String result = payOrderServiceClient.createPayOrder(payOrder.toJSONString());
            _log.info("{}创建支付订单,结果:{}", logPrefix, result);
            if (StringUtils.isEmpty(result)) {
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建支付订单失败", null, null));
                return writeResponse(response, retMsg);
            }
            JSONObject resObj = JSON.parseObject(result);
            if (resObj == null || !"1".equals(resObj.getString("result"))) {
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建支付订单失败", null, null));
                return writeResponse(response, retMsg);
            }
//            String payChannelCode = payOrder.getString("payChannelCode");
            String payChannelName = payOrder.getString("payChannelName");
            switch (payChannelName) {
                case PayConstant.PAY_CHANNEL_WX_APP:
                    return payOrderServiceClient.doWxPayReq(getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_APP, payOrder}));
                case PayConstant.PAY_CHANNEL_WX_JSAPI:
                    return payOrderServiceClient.doWxPayReq(getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_JSPAI, payOrder}));
                case PayConstant.PAY_CHANNEL_WX_NATIVE:
                    return payOrderServiceClient.doWxPayReq(getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_NATIVE, payOrder}));
                case PayConstant.PAY_CHANNEL_WX_MWEB:
                    return payOrderServiceClient.doWxPayReq(getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_MWEB, payOrder}));
                case PayConstant.PAY_CHANNEL_ALIPAY_MOBILE:
                    return payOrderServiceClient.doAliPayMobileReq(getJsonParam("payOrder", payOrder));
                case PayConstant.PAY_CHANNEL_ALIPAY_PC:
                    return payOrderServiceClient.doAliPayPcReq(getJsonParam("payOrder", payOrder));
//                case PayConstant.PAY_CHANNEL_ALIPAY_WAP:
//                    return payOrderServiceClient.doAliPayWapReq(getJsonParam("payOrder", payOrder));
                case PayConstant.PAY_CHANNEL_ALIPAY_QR:
                    return payOrderServiceClient.doAliPayQrReq(getJsonParam("payOrder", payOrder));
                case PayConstant.CHANNEL_NAME_TENGJING:
                    model.put("payOrderId", payOrder.getString("id"));
                    model.put("mchOrderNo", payOrder.getString("mchOrderNo"));
                    return "tengjing/index";
                case PayConstant.CHANNEL_NAME_O9N: {
                    Map map = payO9nService.buildRequestData(payOrder);
                    model.put("method", "post");
                    model.put("action", "http://47.75.195.14/Pay_Index.html");
                    model.put("payParams", map);
                    return "payForm";
                }
                case PayConstant.CHANNEL_NAME_O9N2: {
                    Map map = payO9n2Service.buildRequestData(payOrder);
                    model.put("method", "post");
                    model.put("action", "http://47.75.195.14/Pay_Index.html");
                    model.put("payParams", map);
                    return "payForm";
                }
                case PayConstant.CHANNEL_NAME_XQ316:
                    String res1 = payOrderServiceClient.doXq316Req(getJsonParam("payOrder", payOrder));
                    JSONObject resObj1 = JSON.parseObject(res1);
                    String payUrl = resObj1.getString("payUrl");
                    if (StringUtils.isNotBlank(payUrl)) {
                        model.put("action", payUrl);
                        return "redirect:" + payUrl;
                    } else {
                        retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, resObj1.getString(PayConstant.RETURN_PARAM_RETMSG), null, null));
                        return writeResponse(response, retMsg);
                    }
                case PayConstant.CHANNEL_NAME_THREE10000: {
                    String res2 = payOrderServiceClient.doThree10000Req(getJsonParam(new String[]{"payOrder"}, new Object[]{payOrder}));
                    JSONObject resultObj = JSON.parseObject(res2);
                    String retCode = resultObj.getString(PayConstant.RESULT_PARAM_RESCODE);
                    if (retCode.equals(PayConstant.RETURN_VALUE_SUCCESS)) {
                        String type = resultObj.getString("type");
                        String data = resultObj.getString("data");
                        if ("1".equals(type)) {
                            response.getWriter().write(data);
                            return null;
                        } else if ("2".equals(type)) {
                            model.put("action", data);
                            model.put("method", "GET");
                            return "payForm";
                        } else {
                            return "error";
                        }
                    } else {
                        model.put("retMsg", resultObj.getString(PayConstant.RESULT_PARAM_ERRCODEDES));
                        return "error";
                    }
                }
                case PayConstant.CHANNEL_NAME_DS: {
                    Map map = payDsService.buildRequestData(payOrder);
                    model.put("method", "post");
                    model.put("action", "http://47.52.62.240/pay-api/gateway");
                    model.put("payParams", map);
                    return "payForm";
                }
                case PayConstant.CHANNEL_NAME_MZWYQ: {
                    Map map = payChannel4MzwyqService.buildRequestData(payOrder);
                    String retCode = (String) map.get(PayConstant.RESULT_PARAM_RESCODE);
                    if (retCode.equals(PayConstant.RETURN_VALUE_SUCCESS)) {
                        Integer type = (Integer) map.get("type");
                        String data = (String) map.get("data");
                        if (type.equals(1)) {
                            response.getWriter().write(data);
                            return null;
                        } else if (type.equals(2)) {
                            model.put("action", data);
                            model.put("method", "GET");
                            return "payForm";
                        } else {
                            return "error";
                        }
                    } else {
                        model.put("retMsg", (String) map.get(PayConstant.RESULT_PARAM_ERRCODEDES));
                        return "error";
                    }
                }
                case PayConstant.CHANNEL_NAME_HUITONG: {
                    Map map = payChannel4HuitongService.buildRequestData(payOrder);
                    String retCode = (String) map.get(PayConstant.RESULT_PARAM_RESCODE);
                    if (retCode.equals(PayConstant.RETURN_VALUE_SUCCESS)) {
                        String type = (String) map.get("type");
                        if (type.equals("payUrl")) {
                            String payUrl1 = (String) map.get("payUrl");
                            model.put("action", payUrl1);
                            model.put("method", "GET");
                            return "payForm";
                        } else {
                            return "error";
                        }
                    } else {
                        model.put("retMsg", (String) map.get(PayConstant.RESULT_PARAM_ERRCODEDES));
                        return "error";
                    }
                }
                case PayConstant.CHANNEL_NAME_MF: {
                    String resp = payOrderServiceClient.doMinFuPayReq(getJsonParam("payOrder", payOrder));
                    model.put("method", "post");
                    Map object1 = JSONObject.parseObject(resp, Map.class);
                    model.put("action", object1.get("payUrl"));
                    model.put("payParams", object1);
                    return "payForm";
                }
                case PayConstant.CHANNEL_NAME_CJ_GATEWAY: {
                    String resp = payOrderServiceClient.nmg_ebank_pay(getJsonParam("payOrder", payOrder));
                    model.put("method", "post");
                    Map object1 = JSONObject.parseObject(resp, Map.class);
                    System.out.println(object1);
                    model.put("action", "https://pay.chanpay.com/mag-unify/gateway/receiveOrder.do?");
                    model.put("payParams", object1);
                    return "payForm";
                }
                case PayConstant.CHANNEL_NAME_CJ_FAST_PAY: {
                    String resp = payOrderServiceClient.nmg_zft_api_quick_payment(getJsonParam("payOrder", payOrder));
                    retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, resp, null, null));
                    return writeResponse(response, retMsg);
                }
                case PayConstant.CHANNEL_NAME_CJ_FAST_PAY_WAP: {
                    String resp = payOrderServiceClient.nmg_quick_onekeypay(getJsonParam("payOrder", payOrder));
                    model.put("method", "post");
                    Map object1 = JSONObject.parseObject(resp, Map.class);
                    System.out.println(object1);
                    model.put("action", "https://pay.chanpay.com/mag-unify/gateway/receiveOrder.do?");
                    model.put("payParams", object1);
                    return "payForm";
                }
                case PayConstant.CHANNEL_NAME_MF_FAST_PAY: {
                    String resp = payOrderServiceClient.doMinFuMessagePayReq(getJsonParam("payOrder", payOrder));
                    model.put("method", "post");
                    Map object1 = JSONObject.parseObject(resp, Map.class);
                    model.put("action", "http://localhost:3010/api/pay/testKj");
                    model.put("payParams", object1);
                    System.out.println(JSONObject.toJSONString(model));
                    return "payFormNew";
                }
                case PayConstant.PAY_TYPE_ALIPAY_WAP: {
                    String resp = payOrderServiceClient.doALiPayReq(getJsonParam("payOrder", payOrder));
                    model.put("method", "post");
                    model.put("action", resp);
                    System.out.println(resp);
                    return "payFormNew";
                }
                default:
                    retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "不支持的支付渠道类型[payType=" + payType + "]", null, null));
                    return writeResponse(response, retMsg);
            }
        } catch (Exception e) {
            _log.error(e, "");
            retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, null));
            return writeResponse(response, retMsg);
        }

    }

    private String writeResponse(HttpServletResponse response, String msg) {
        try {
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            try (PrintWriter printWriter = response.getWriter()) {
                printWriter.write(msg);
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     *
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        try {
            // 支付参数
            String mchId = params.getString("mchId");                // 商户ID
            String mchOrderNo = params.getString("mchOrderNo");    // 商户订单号
            String payType = params.getString("payType");           // 支付方式
            String strAmount = params.getString("amount");            // 支付金额（单位分）
            String currency = params.getString("currency");         // 币种
            String clientIp = params.getString("clientIp");            // 客户端IP
            String device = params.getString("device");            // 设备
            String extra = params.getString("extra");                // 特定渠道发起时额外参数
            String param1 = params.getString("param1");            // 扩展参数1
            String param2 = params.getString("param2");            // 扩展参数2
            String frontUrl = params.getString("frontUrl");        // 支付回跳URL
            String notifyUrl = params.getString("notifyUrl");        // 支付结果异步通知URL
            String sign = params.getString("sign");                // 签名
            String subject = params.getString("subject");            // 商品主题
            String body = params.getString("body");                    // 商品描述信息
            String BkAcctNo = params.getString("BkAcctNo");//银行卡号
            String IDNo = params.getString("IDNo");//证件号
            String CstmrNm = params.getString("CstmrNm");//持卡人姓名
            String MobNo = params.getString("MobNo");//银行预留手机号


            // 验证请求参数有效性（必选项）
            Assert.isTrue(StringUtils.isNotBlank(mchId), "request params[mchId] error.");
            Assert.isTrue(StringUtils.isNotBlank(mchOrderNo), "request params[mchOrderNo] error.");
            Assert.isTrue(StringUtils.isNotBlank(payType), "request params[payType] error.");
            getAndTestPayType(payType);
            Assert.isTrue(NumberUtils.isNumber(strAmount), "request params[amount] error.");
            Long amount = Long.parseLong(strAmount);
            Assert.isTrue(amount.compareTo(0L) > 0, "request params[amount] error.");
            Assert.isTrue(StringUtils.isNotBlank(currency), "request params[currency] error.");
            Assert.isTrue(StringUtils.isNotBlank(subject), "request params[subject] error.");
            Assert.isTrue(StringUtils.isNotBlank(body), "request params[body] error.");
            Assert.isTrue(StringUtils.isNotBlank(sign), "request params[sign] error.");

            // 查询商户信息
            JSONObject mchInfo = getMchInfo(mchId);
            String reqKey = mchInfo.getString("reqKey");
            String agentMchId = mchInfo.getString("agentId");
            // 查询商户订单号是否已存在
            testMchOrderNo(mchId, mchOrderNo);

            // 查询三方支付渠道(结果：表t_pay_channel)
            JSONObject payChannel = selectAndTestPayChannel(mchId, payType, amount);
            String payChannelId = payChannel.getString("id");
            String payChannelCode = payChannel.getString("code");
            String payChannelName = payChannel.getString("name");
            Double thirdDeductionRate = payChannel.getDouble("thirdDeductionRate"); //三方手续费率
            //计算三方手续费 = 交易金额 * 三方手续费率
            long thirdDeduction = BigDecimalUtils.mul(amount.longValue(), thirdDeductionRate.doubleValue());
            // 计算平台到账金额 = 交易金额 - 三方手续费
            long platformActualAmount = BigDecimalUtils.sub(amount.longValue(), thirdDeduction);

            // 查询商户对应的支付渠道(结果：表t_mch_pay_channel)
            JSONObject mchPayChannel = selectAndTestMchPayChannel(params, payChannelId);
            String mchPayChannelId = mchPayChannel.getString("id");
            Double platformDeductionRate = mchPayChannel.getDouble("platformDeductionRate");//平台手续费率
            //平台手续费率必须大于或等于三方手续费率
            Assert.isTrue(platformDeductionRate.compareTo(thirdDeductionRate) >= 0, "platformDeductionRate can't be less than the thirdDeductionRate [mchPayChannelId=" + mchPayChannelId + "].");
            //平台手续费 = 交易金额 * 平台手续费率
            long platformDeduction = BigDecimalUtils.mul(amount.longValue(), platformDeductionRate.doubleValue());
            //计算子商户到账金额 = 交易金额 - 平台手续费
            long subMchActualAmount = BigDecimalUtils.sub(amount.longValue(), platformDeduction);

            // 查询代理商户对应的支付渠道
            JSONObject agentMchPayChannel = selectAndTestAgentMchPayChannel(payChannelId, agentMchId);
            Double agentMchCommissionRate = agentMchPayChannel.getDouble("agentMchCommissionRate");
            String agentMchPayChannelId = agentMchPayChannel.getString("id");
            //验证平台手续费，必须大于（或等于） 三方手续费率 + 代理利润率 之和
            double tempTotalRate = BigDecimalUtils.add(thirdDeductionRate.doubleValue(), agentMchCommissionRate.doubleValue());
            Assert.isTrue(platformDeductionRate.compareTo(tempTotalRate) >= 0, "platformDeductionRate can't be less than the thirdDeductionRate add agentMchCommissionRate [mchPayChannelId=" + mchPayChannelId + "].");

            // 验证签名数据
            boolean verifyFlag = XXPayUtil.verifyPaySign(params, reqKey);
            Assert.isTrue(verifyFlag, "Verify sign failed.");

            // 验证参数通过,返回JSONObject对象
            JSONObject payOrder = new JSONObject();
            payOrder.put("id", MySeq.getPay());
            payOrder.put("agentMchId", agentMchId);
            payOrder.put("mchId", mchId);
            payOrder.put("mchOrderNo", mchOrderNo);
            payOrder.put("agentMchPayChannelId", agentMchPayChannelId);
            payOrder.put("mchPayChannelId", mchPayChannelId);
            payOrder.put("payChannelId", payChannelId);
            payOrder.put("payChannelCode", payChannelCode);
            payOrder.put("payChannelName", payChannelName);
            payOrder.put("amount", amount);
            payOrder.put("payType", payType);
            payOrder.put("payAmount", amount);
            payOrder.put("platformActualAmount", platformActualAmount);
            payOrder.put("subMchActualAmount", subMchActualAmount);
            payOrder.put("platformDeductionRate", platformDeductionRate);
            payOrder.put("agentMchCommissionRate", agentMchCommissionRate);
            payOrder.put("thirdDeductionRate", thirdDeductionRate);
            payOrder.put("currency", currency);
            payOrder.put("clientIp", clientIp);
            payOrder.put("device", device);
            payOrder.put("subject", subject);
            payOrder.put("body", body);
            payOrder.put("extra", extra);
            payOrder.put("param1", param1);
            payOrder.put("param2", param2);
            payOrder.put("frontUrl", frontUrl);
            payOrder.put("notifyUrl", notifyUrl);
            payOrder.put("bankCode", params.getString("bankCode"));
            payOrder.put("cardNo", params.getString("cardNo"));
            payOrder.put("mobile", params.getString("mobile"));
            payOrder.put("BkAcctNo", BkAcctNo);
            payOrder.put("IDNo", IDNo);
            payOrder.put("CstmrNm", CstmrNm);
            payOrder.put("MobNo", MobNo);
            return payOrder;
        } catch (IllegalArgumentException ex) {
            errorMessage = ex.getMessage();
            return errorMessage;
        }
    }

    JSONObject getAndTestPayType(String payTypeId) {
        // 查询商户信息
        String retStr = payTypeServiceClient.selectPayType(getJsonParam("id", payTypeId));

        JSONObject retObj = JSON.parseObject(retStr);
        boolean retSucceed = retSuccess(retObj, "payType");
        Assert.isTrue(retSucceed, "未找到该支付类型[payType=" + payTypeId + "] ");

        JSONObject payType = retObj.getJSONObject("result");
        Assert.notNull(payType, "未找到该支付类型[payType=" + payTypeId + "] ");
        return payType;
    }

    JSONObject testMchOrderNo(String mchId, String mchOrderNo) {
        // 查询商户信息
        String retStr = payOrderServiceClient.queryPayOrder(getJsonParam(new String[]{"mchId", "mchOrderNo"}, new Object[]{mchId, mchOrderNo}));

        JSONObject retObj = JSON.parseObject(retStr);
        JSONObject payOrder = retObj.getJSONObject("result");
        Assert.isNull(payOrder, "支付订单已存在，请勿重复支付。 [mchId=" + mchId + ", mchOrderNo=" + mchOrderNo + "] ");
        return payOrder;
    }

    JSONObject getMchInfo(String mchId) {
        // 查询商户信息
        String retStr = mchInfoServiceClient.selectMchInfo(getJsonParam("mchId", mchId));

        JSONObject retObj = JSON.parseObject(retStr);
        boolean retSucceed = retSuccess(retObj, "mchInfo");
        Assert.isTrue(retSucceed, "未找到该商户信息。[mchId=" + mchId + "]");

        JSONObject mchInfo = retObj.getJSONObject("result");
        Assert.notNull(mchInfo, "未找到该商户信息。[mchId=" + mchId + "] ");
        Assert.isTrue(MchState.ENABLE.getCode().equals(mchInfo.getByte("state")), "商户当前不可用。 [mchId=" + mchId + "]");


        String reqKey = mchInfo.getString("reqKey");
        Assert.isTrue(StringUtils.isNotBlank(reqKey), "reqKey is null[mchId=" + mchId + "] record in db.");

        String agentMchId = mchInfo.getString("agentId");
        Assert.isTrue(StringUtils.isNotBlank(agentMchId), "agentId is null[mchId=" + mchId + "] record in db.");
        return mchInfo;
    }

    private JSONObject selectAndTestAgentMchPayChannel(String payChannelId, String agentMchId) {
        // 查询代理商户对应的支付渠道
        String retStr = agentMchPayChannelServiceClient.selectAgentMchPayChannel(getJsonParam(new String[]{"payChannelId", "mchId"}, new String[]{payChannelId, agentMchId}));
        JSONObject retObj = JSON.parseObject(retStr);
        boolean retSucceed = retSuccess(retObj, "agentMchPayChannel");
        Assert.isTrue(retSucceed, "未配置代理通道 [payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "] ");
        JSONObject agentMchPayChannel = JSON.parseObject(retObj.getString("result"));
        Assert.notNull(agentMchPayChannel, "未配置代理通道 [payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "] ");
        Assert.isTrue(PayChannelState.ENABLE.getCode().equals(agentMchPayChannel.getByte("state")), "代理通道不可用 [payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "]");

        Double agentMchCommissionRate = agentMchPayChannel.getDouble("agentMchCommissionRate");
        Assert.notNull(agentMchCommissionRate, "未配置代理分润 [payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "] .");
        Assert.isTrue(agentMchCommissionRate.compareTo(0.0) >= 0, "代理分润配置异常 [payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "] .");
        return agentMchPayChannel;
    }

    private JSONObject selectAndTestMchPayChannel(JSONObject params, String payChannelId) {
        String mchId = params.getString("mchId");// 商户id
        String retStr = mchPayChannelServiceClient.selectMchPayChannel(getJsonParam(new String[]{"payChannelId", "mchId"}, new String[]{payChannelId, mchId}));
        JSONObject retObj = JSON.parseObject(retStr);
        boolean retSucceed = retSuccess(retObj, "mchPayChannel");
        Assert.isTrue(retSucceed, "找不到商户通道 [payChannelId=" + payChannelId + ",mchId=" + mchId + "]");
        JSONObject mchPayChannel = JSON.parseObject(retObj.getString("result"));
        Assert.notNull(mchPayChannel, "找不到商户通道 [payChannelId=" + payChannelId + ",mchId=" + mchId + "]");
        Assert.isTrue(PayChannelState.ENABLE.getCode().equals(mchPayChannel.getByte("state")), "商户通道不可用 [payChannelId=" + payChannelId + ",mchId=" + mchId + "]");
        Double platformDeductionRate = mchPayChannel.getDouble("platformDeductionRate");//平台手续费率
        Assert.notNull(platformDeductionRate, "未配置通道手续费 [payChannelId=" + payChannelId + ",mchId=" + mchId + "]");
        //平台手续费率必须大于或者等于0
        Assert.isTrue(platformDeductionRate.compareTo(0.0) >= 0, "通道手续费配置错误 [payChannelId=" + payChannelId + ",mchId=" + mchId + "] ");
        return mchPayChannel;
    }

    private JSONObject selectAndTestPayChannel(String mchId, String payType, Long amount) {
        // 查询三方支付渠道
        String retStr = payChannelServiceClient.selectByMchIdAndPayType(getJsonParam(new String[]{"mchId", "payType"}, new String[]{mchId, payType}));
        JSONObject retObj = JSON.parseObject(retStr);
        boolean retSucceed = retSuccess(retObj, "payChannel");
        String failLogWherePath = "[mchId=" + mchId + " and payType=" + payType + "]";

        Assert.isTrue(retSucceed, "未找到支付通道" + failLogWherePath);
        JSONObject payChannel = JSON.parseObject(retObj.getString("result"));
        Assert.notNull(payChannel, "未找到支付通道" + failLogWherePath);
        Assert.isTrue(PayChannelState.ENABLE.getCode().equals(payChannel.getByte("state")), "支付通道不可用" + failLogWherePath);

        String payChannelCode = payChannel.getString("code");
        Assert.isTrue(StringUtils.isNotBlank(payChannelCode), "通道标识码未配置 " + failLogWherePath);

        Double thirdDeductionRate = payChannel.getDouble("thirdDeductionRate"); //三方手续费率
        Assert.notNull(thirdDeductionRate, "未配置三方手续费 " + failLogWherePath);
        Assert.isTrue(thirdDeductionRate.compareTo(0.0) >= 0, "三方手续费配置错误 " + failLogWherePath);

        //验证最大，最小交易金额限制
        Long maxTransactionAmount = payChannel.getLong("maxTransactionAmount");
        Assert.notNull(maxTransactionAmount, "通道未配置最大单笔金额 " + failLogWherePath);
        Assert.isTrue(amount.compareTo(maxTransactionAmount) <= 0, "金额不正确。单笔最大金额为" + AmountUtil.convertCent2Dollar(maxTransactionAmount + ""));
        Long minTransactionAmount = payChannel.getLong("minTransactionAmount");
        Assert.notNull(minTransactionAmount, "通道未配置最小单笔金额 " + failLogWherePath);
        Assert.isTrue(amount.compareTo(minTransactionAmount) >= 0, "金额不正确。单笔最小金额为" + AmountUtil.convertCent2Dollar(minTransactionAmount + ""));

        //验证每天交易开始时间
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        String strStartTime = payChannel.getString("startTime");
        Assert.notNull(strStartTime, "通道未配置每天开始交易时间 " + failLogWherePath);
        boolean startTimeTestValid = MyTimeUtils.testIsHourAndMinuteString(strStartTime);
        Assert.isTrue(startTimeTestValid, "通道配置开始时间有误 " + failLogWherePath);
        MyTimeUtils.HourAndMinute hourAndMinute = MyTimeUtils.convert2HourAndMinute(strStartTime);
        calendar.set(Calendar.HOUR_OF_DAY, hourAndMinute.getHour().intValue());
        calendar.set(Calendar.MINUTE, hourAndMinute.getMinute().intValue());
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();
        Assert.isTrue(now.after(startTime), "通道每天开始交易时间为 " + strStartTime);

        //验证每天交易结束时间
        String strEndTime = payChannel.getString("endTime");
        Assert.notNull(strEndTime, "通道未配置每天结束交易时间 " + failLogWherePath);
        boolean endTimeTestValid = MyTimeUtils.testIsHourAndMinuteString(strEndTime);
        Assert.isTrue(endTimeTestValid, "通道配置结束交易时间有误 " + failLogWherePath);
        hourAndMinute = MyTimeUtils.convert2HourAndMinute(strEndTime);

        calendar.set(Calendar.HOUR_OF_DAY, hourAndMinute.getHour().intValue());
        calendar.set(Calendar.MINUTE, hourAndMinute.getMinute().intValue());
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();
        Assert.isTrue(now.before(endTime), "通道每天关闭交易时间为 " + strEndTime + " " + failLogWherePath);
        return payChannel;
    }

    boolean retSuccess(JSONObject retObj, String entityName) {
        String retCode = retObj.getString("code");
        boolean succeed = "0000".equals(retCode);
        if (!succeed) {
            _log.info("查询{}没有正常返回数据,code={},msg={}", entityName, retObj.getString("code"), retObj.getString("msg"));
        }
        return succeed;
    }

    /**
     * private Object validateParams(JSONObject params) {
     * // 验证请求参数,参数有问题返回错误提示
     * String errorMessage;
     * // 支付参数
     * String mchId = params.getString("mchId");                // 商户ID
     * String mchOrderNo = params.getString("mchOrderNo");    // 商户订单号
     * String channelCode = params.getString("channelCode");        // 渠道Code
     * String strAmount = params.getString("amount");            // 支付金额（单位分）
     * String currency = params.getString("currency");         // 币种
     * String clientIp = params.getString("clientIp");            // 客户端IP
     * String device = params.getString("device");            // 设备
     * String extra = params.getString("extra");                // 特定渠道发起时额外参数
     * String param1 = params.getString("param1");            // 扩展参数1
     * String param2 = params.getString("param2");            // 扩展参数2
     * String frontUrl = params.getString("frontUrl");        // 支付回跳URL
     * String notifyUrl = params.getString("notifyUrl");        // 支付结果异步通知URL
     * String sign = params.getString("sign");                // 签名
     * String subject = params.getString("subject");            // 商品主题
     * String body = params.getString("body");                    // 商品描述信息
     * // 验证请求参数有效性（必选项）
     * if (StringUtils.isBlank(mchId)) {
     * errorMessage = "request params[mchId] error.";
     * return errorMessage;
     * }
     * if (StringUtils.isBlank(mchOrderNo)) {
     * errorMessage = "request params[mchOrderNo] error.";
     * return errorMessage;
     * }
     * if (StringUtils.isBlank(channelCode)) {
     * errorMessage = "request params[channelCode] error.";
     * return errorMessage;
     * }
     * if (!NumberUtils.isNumber(strAmount)) {
     * errorMessage = "request params[amount] error.";
     * return errorMessage;
     * }
     * Long amount=Long.parseLong(strAmount);
     * if(amount.compareTo(0L)<=0){
     * errorMessage = "request params[amount] error.";
     * return errorMessage;
     * }
     * if (StringUtils.isBlank(currency)) {
     * errorMessage = "request params[currency] error.";
     * return errorMessage;
     * }
     * if (StringUtils.isBlank(notifyUrl)) {
     * errorMessage = "request params[notifyUrl] error.";
     * return errorMessage;
     * }
     * if (StringUtils.isBlank(subject)) {
     * errorMessage = "request params[subject] error.";
     * return errorMessage;
     * }
     * if (StringUtils.isBlank(body)) {
     * errorMessage = "request params[body] error.";
     * return errorMessage;
     * }
     * <p>
     * // 签名信息
     * if (StringUtils.isEmpty(sign)) {
     * errorMessage = "request params[sign] error.";
     * return errorMessage;
     * }
     * <p>
     * // 查询商户信息
     * JSONObject mchInfo;
     * String retStr = mchInfoServiceClient.selectMchInfo(getJsonParam("mchId", mchId));
     * <p>
     * JSONObject retObj = JSON.parseObject(retStr);
     * if ("0000".equals(retObj.getString("code"))) {
     * mchInfo = retObj.getJSONObject("result");
     * if (mchInfo == null) {
     * errorMessage = "Can't found mchInfo[mchId=" + mchId + "] record in db.";
     * return errorMessage;
     * }
     * if (mchInfo.getByte("state") != MchState.ENABLE.getCode()) {
     * errorMessage = "mchInfo not available [mchId=" + mchId + "] record in db.";
     * return errorMessage;
     * }
     * } else {
     * errorMessage = "Can't found mchInfo[mchId=" + mchId + "] record in db.";
     * _log.info("查询商户没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
     * return errorMessage;
     * }
     * <p>
     * String reqKey = mchInfo.getString("reqKey");
     * if (StringUtils.isBlank(reqKey)) {
     * errorMessage = "reqKey is null[mchId=" + mchId + "] record in db.";
     * return errorMessage;
     * }
     * <p>
     * String agentMchId=mchInfo.getString("parentId");
     * if (StringUtils.isBlank(agentMchId)) {
     * errorMessage = "parentId is null[mchId=" + mchId + "] record in db.";
     * return errorMessage;
     * }
     * <p>
     * // 查询三方支付渠道
     * JSONObject payChannel;
     * retStr = payChannelServiceClient.selectPayChannel(getJsonParam(new String[]{"code"}, new String[]{channelCode}));
     * retObj = JSON.parseObject(retStr);
     * if ("0000".equals(retObj.getString("code"))) {
     * payChannel = JSON.parseObject(retObj.getString("result"));
     * if (payChannel == null) {
     * errorMessage = "Can't found payChannel[channelCode=" + channelCode + "] record in db.";
     * return errorMessage;
     * }
     * if (payChannel.getByte("state") != PayChannelState.ENABLE.getCode()) {
     * errorMessage = "channel not available [channelCode=" + channelCode + "]";
     * return errorMessage;
     * }
     * } else {
     * errorMessage = "Can't found payChannel[channelCode=" + channelCode + "] record in db.";
     * _log.info("查询渠道没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
     * return errorMessage;
     * }
     * <p>
     * String payChannelId = payChannel.getString("id");
     * <p>
     * // 查询商户对应的支付渠道
     * JSONObject mchPayChannel;
     * retStr = mchPayChannelServiceClient.selectMchPayChannel(getJsonParam(new String[]{"payChannelId", "mchId"}, new String[]{payChannelId, mchId}));
     * retObj = JSON.parseObject(retStr);
     * if ("0000".equals(retObj.getString("code"))) {
     * mchPayChannel = JSON.parseObject(retObj.getString("result"));
     * if (mchPayChannel == null) {
     * errorMessage = "Can't found mchPayChannel[payChannelId=" + payChannelId + ",mchId=" + mchId + "] record in db.";
     * return errorMessage;
     * }
     * if (mchPayChannel.getByte("state") != 1) {
     * errorMessage = "mchPayChannel not available [payChannelId=" + payChannelId + ",mchId=" + mchId + "]";
     * return errorMessage;
     * }
     * } else {
     * errorMessage = "Can't found mchPayChannel[payChannelId=" + payChannelId + ",mchId=" + mchId + "] record in db.";
     * _log.info("查询渠道没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
     * return errorMessage;
     * }
     * String mchPayChannelId=mchPayChannel.getString("id");
     * <p>
     * // 查询代理商户对应的支付渠道
     * JSONObject agentMchPayChannel;
     * retStr = mchPayChannelServiceClient.selectMchPayChannel(getJsonParam(new String[]{"payChannelId", "mchId"}, new String[]{payChannelId, agentMchId}));
     * retObj = JSON.parseObject(retStr);
     * if ("0000".equals(retObj.getString("code"))) {
     * agentMchPayChannel = JSON.parseObject(retObj.getString("result"));
     * if (agentMchPayChannel == null) {
     * errorMessage = "Can't found agentMchPayChannel[payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "] record in db.";
     * return errorMessage;
     * }
     * if (agentMchPayChannel.getByte("state") != 1) {
     * errorMessage = "agentMchPayChannel not available [payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "]";
     * return errorMessage;
     * }
     * } else {
     * errorMessage = "Can't found agentMchPayChannel[payChannelId=" + payChannelId + ",agentMchId=" + agentMchId + "] record in db.";
     * _log.info("查询渠道没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
     * return errorMessage;
     * }
     * String agentMchPayChannelId=agentMchPayChannel.getString("id");
     * <p>
     * // 验证签名数据
     * boolean verifyFlag = XXPayUtil.verifyPaySign(params, reqKey);
     * if (!verifyFlag) {
     * errorMessage = "Verify XX pay sign failed.";
     * return errorMessage;
     * }
     * <p>
     * <p>
     * // 验证参数通过,返回JSONObject对象
     * JSONObject payOrder = new JSONObject();
     * payOrder.put("id", MySeq.getPay());
     * payOrder.put("agentMchId", agentMchId);
     * payOrder.put("mchId", mchId);
     * payOrder.put("mchOrderNo", mchOrderNo);
     * payOrder.put("agentMchPayChannelId", agentMchPayChannelId);
     * payOrder.put("mchPayChannelId", mchPayChannelId);
     * payOrder.put("payChannelId", payChannelId);
     * payOrder.put("amount", Long.parseLong(amount));
     * payOrder.put("currency", currency);
     * payOrder.put("clientIp", clientIp);
     * payOrder.put("device", device);
     * payOrder.put("subject", subject);
     * payOrder.put("body", body);
     * payOrder.put("extra", extra);
     * payOrder.put("channelMchId", payChannel.getString("channelMchId"));
     * payOrder.put("param1", param1);
     * payOrder.put("param2", param2);
     * payOrder.put("notifyUrl", notifyUrl);
     * return payOrder;
     * }
     **/


    String getJsonParam(String[] names, Object[] values) {
        JSONObject jsonParam = new JSONObject();
        for (int i = 0; i < names.length; i++) {
            jsonParam.put(names[i], values[i]);
        }
        return jsonParam.toJSONString();
    }

    String getJsonParam(String name, Object value) {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(name, value);
        return jsonParam.toJSONString();
    }

}
