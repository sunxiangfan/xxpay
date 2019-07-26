package org.xxpay.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.web.service.MchInfoServiceClient;
import org.xxpay.web.service.PayOrderServiceClient;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 支付订单查询
 * @date 2017-08-31
 * @Copyright: www.xxpay.org
 */
@RestController
public class QueryPayOrderController {

    private final MyLog _log = MyLog.getLog(QueryPayOrderController.class);

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    /**
     * 查询支付订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/pay/query_order")
    public String queryPayOrder(@RequestParam String params) {
        _log.info("###### 开始接收商户查询支付订单请求 ######");
        String logPrefix = "【商户支付订单查询】";
        ServiceInstance instance = client.getLocalServiceInstance();
        _log.info("{}/pay/query_order, host:{}, service_id:{}, params:{}", logPrefix, instance.getHost(), instance.getServiceId(), params);
        try {
            JSONObject po = JSONObject.parseObject(params);
            JSONObject payContext = new JSONObject();
            // 验证参数有效性
            String errorMessage = validateParams(po, payContext);
            if (!"success".equalsIgnoreCase(errorMessage)) {
                _log.warn(errorMessage);
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, errorMessage, null, null));
            }
            _log.debug("请求参数及签名校验通过");
            String mchId = po.getString("mchId");                // 商户ID
            String mchOrderNo = po.getString("mchOrderNo");    // 商户订单号
            String payOrderId = po.getString("payOrderId");    // 支付订单号
            String executeNotify = po.getString("executeNotify");   // 是否执行回调
            JSONObject payOrder;
            String retStr = payOrderServiceClient.queryPayOrder(getJsonParam(new String[]{"mchId", "payOrderId", "mchOrderNo", "executeNotify"}, new Object[]{mchId, payOrderId, mchOrderNo, executeNotify}));
            JSONObject retObj = JSON.parseObject(retStr);
            _log.info("{}查询支付订单,结果:{}", logPrefix, retObj);
            if (!"0000".equals(retObj.getString("code"))) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, retObj.getString("msg"), null, null));
            }
            payOrder = retObj.getJSONObject("result");
            if (payOrder == null) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付订单不存在", null, null));
            }
//            payOrder.put("thirdDeductionRate", null);
//            payOrder.put("platformActualAmount", null);
//            payOrder.put("agentMchId", null);
//            payOrder.put("agentMchCommissionRate", null);
//            payOrder.put("agentMchPayChannelId", null);

            Map<String, Object> map = null;
            Byte payOrderStatus = payOrder.getByte("status");
            if (payOrderStatus.equals(PayConstant.PAY_STATUS_SUCCESS) || payOrderStatus.equals(PayConstant.PAY_STATUS_COMPLETE)) {
                map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            } else {
                map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, null);
            }
            Map<String, Object> result = buildResult(payOrder);
            map.put("result", new String(Base64Utils.encode(JSON.toJSONString(result).getBytes("UTF-8")), "UTF-8"));
            _log.info("商户查询订单成功,payOrder={}", payOrder);
            _log.info("###### 商户查询订单处理完成 ######");
            return XXPayUtil.makeRetData(map, payContext.getString("resKey"));
//            return map;
        } catch (Exception e) {
            _log.error(e, "");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, null));
        }
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     *
     * @param params
     * @return
     */
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId");                // 商户ID
        String mchOrderNo = params.getString("mchOrderNo");    // 商户订单号
        String payOrderId = params.getString("payOrderId");    // 支付订单号

        String sign = params.getString("sign");                // 签名

        // 验证请求参数有效性（必选项）
        if (StringUtils.isBlank(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        if (StringUtils.isBlank(mchOrderNo) && StringUtils.isBlank(payOrderId)) {
            errorMessage = "request params[mchOrderNo or payOrderId] error.";
            return errorMessage;
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "request params[sign] error.";
            return errorMessage;
        }

        // 查询商户信息
        JSONObject mchInfo;
        String retStr = mchInfoServiceClient.selectMchInfo(getJsonParam("mchId", mchId));

        JSONObject retObj = JSON.parseObject(retStr);
        if ("0000".equals(retObj.getString("code"))) {
            mchInfo = retObj.getJSONObject("result");
            if (mchInfo == null) {
                errorMessage = "Can't found mchInfo[mchId=" + mchId + "] record in db.";
                return errorMessage;
            }
            if (mchInfo.getByte("state") != 1) {
                errorMessage = "mchInfo not available [mchId=" + mchId + "] record in db.";
                return errorMessage;
            }
        } else {
            errorMessage = "Can't found mchInfo[mchId=" + mchId + "] record in db.";
            _log.info("查询商户没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
            return errorMessage;
        }

        String reqKey = mchInfo.getString("reqKey");
        if (StringUtils.isBlank(reqKey)) {
            errorMessage = "reqKey is null[mchId=" + mchId + "] record in db.";
            return errorMessage;
        }
        payContext.put("resKey", mchInfo.getString("resKey"));

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, reqKey);
        if (!verifyFlag) {
            errorMessage = "Verify XX pay sign failed.";
            return errorMessage;
        }

        return "success";
    }

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

    private Map<String, Object> buildResult(JSONObject payOrder) {
        Map<String, Object> result = new HashMap<>();
        result.put("payOrderId", payOrder.getString("id") == null ? "" : payOrder.getString("id"));           // 支付订单号
        result.put("mchId", payOrder.getString("mchId") == null ? "" : payOrder.getString("mchId"));                        // 商户ID
        result.put("mchOrderNo", payOrder.getString("mchOrderNo") == null ? "" : payOrder.getString("mchOrderNo"));        // 商户订单号
        result.put("channelId", "");              // 渠道ID
        result.put("amount", payOrder.getLong("amount") == null ? "" : payOrder.getLong("amount"));                        // 支付金额
        result.put("currency", payOrder.getString("currency") == null ? "" : payOrder.getString("currency"));                 // 货币类型
        result.put("status", payOrder.getString("status") == null ? "" : payOrder.getString("status"));                    // 支付状态
        result.put("clientIp", payOrder.getString("clientIp") == null ? "" : payOrder.getString("clientIp"));                // 客户端IP
        result.put("device", payOrder.getString("device") == null ? "" : payOrder.getString("device"));                        // 设备
        result.put("subject", payOrder.getString("subject") == null ? "" : payOrder.getString("subject"));                        // 商品标题
        result.put("channelOrderNo", ""); // 渠道订单号
        result.put("param1", payOrder.getString("param1") == null ? "" : payOrder.getString("param1"));                        // 扩展参数1
        result.put("param2", payOrder.getString("param2") == null ? "" : payOrder.getString("param2"));                        // 扩展参数2
        result.put("paySuccTime", payOrder.getString("paySuccTime") == null ? "" : payOrder.getString("paySuccTime"));            // 支付成功时间

        // 先对原文签名
//        String reqSign = PayDigestUtil.getSign(result, resKey);
//        result.put("sign", reqSign);   // 签名
        // 签名后再对有中文参数编码
//        try {
//            result.put("device", URLEncoder.encode((String) result.get("device"), PayConstant.RESP_UTF8));
//            result.put("subject", URLEncoder.encode((String) result.get("subject"), PayConstant.RESP_UTF8));
//            result.put("param1", URLEncoder.encode((String) result.get("param1"), PayConstant.RESP_UTF8));
//            result.put("param2", URLEncoder.encode((String) result.get("param2"), PayConstant.RESP_UTF8));
//        } catch (UnsupportedEncodingException e) {
//            _log.error("URL Encode exception.", e);
//            return null;
//        }
        return result;
    }
}
