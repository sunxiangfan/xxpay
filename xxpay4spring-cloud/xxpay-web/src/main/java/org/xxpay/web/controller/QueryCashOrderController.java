package org.xxpay.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.CashConstant;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.web.service.CashOrderServiceClient;
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
public class QueryCashOrderController {

    private final MyLog _log = MyLog.getLog(QueryCashOrderController.class);

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private CashOrderServiceClient cashOrderServiceClient;

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    /**
     * 查询提现订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/cash/query_order")
    public String queryPayOrder(@RequestParam String params) {
        _log.info("###### 开始接收商户查询提现订单请求 ######");
        String logPrefix = "【商户提现订单查询】";
        ServiceInstance instance = client.getLocalServiceInstance();
        _log.info("{}/cash/query_order, host:{}, service_id:{}, params:{}", logPrefix, instance.getHost(), instance.getServiceId(), params);
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
            JSONObject cashOrder;
            String retStr = cashOrderServiceClient.queryCashOrder(getJsonParam(new String[]{"mchId", "mchOrderNo"}, new Object[]{mchId, mchOrderNo}));
            JSONObject retObj = JSON.parseObject(retStr);
            _log.info("{}查询提现订单,结果:{}", logPrefix, retObj);
            if (!"0000".equals(retObj.getString("code"))) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, retObj.getString("msg"), null, null));
            }
            cashOrder = retObj.getJSONObject("result");
            if (cashOrder == null) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "提现订单不存在", null, null));
            }

            Map<String, Object> map = null;
            Map<String, Object> result = buildResult(cashOrder);
            Byte cashOrderState = (Byte) result.get("state");
            if (cashOrderState.equals(CashConstant.CASH_STATUS_SUCCESS)) {
                map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            } else {
                map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, null);
            }
            map.put("result", new String(Base64Utils.encode(JSON.toJSONString(result).getBytes("UTF-8")), "UTF-8"));
            _log.info("商户查询订单成功,cashOrder={}", cashOrder);
            _log.info("###### 商户查询订单处理完成 ######");
            return XXPayUtil.makeRetData(map, payContext.getString("resKey"));
        } catch (Exception e) {
            _log.error(e, "");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "提现中心系统异常", null, null));
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

        String sign = params.getString("sign");                // 签名

        // 验证请求参数有效性（必选项）
        if (StringUtils.isBlank(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        if (StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "request params[mchOrderNo] error.";
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

    private Map<String, Object> buildResult(JSONObject cashOrder) {
        Map<String, Object> result = new HashMap<>();
        result.put("payOrderId", cashOrder.getString("id") == null ? "" : cashOrder.getString("id"));           // 支付订单号
        result.put("mchId", cashOrder.getString("mchId") == null ? "" : cashOrder.getString("mchId"));           // 商户ID
        result.put("mchOrderNo", cashOrder.getString("mchOrderNo") == null ? "" : cashOrder.getString("mchOrderNo"));  // 商户订单号
        result.put("amount", cashOrder.getLong("applyAmount") == null ? "" : cashOrder.getLong("applyAmount"));    // 支付金额
        result.put("currency", "cny");                 // 货币类型
        Byte state = cashOrder.getByte("state");  //审核状态,0-审核中,1-审核通过,2-审核未通过
        Byte cashState = cashOrder.getByte("cashState"); //代付状态,0-未申请,1-已申请,2-审核通过（下放），3-拒绝

        if (state == 1 && cashState == 2) {
            result.put("state", CashConstant.CASH_STATUS_SUCCESS);
            result.put("cashSuccTime", cashOrder.getString("cashSuccTime"));
        } else if (state == 2) {
            result.put("state", CashConstant.CASH_STATUS_FAIL);
        } else if (state == 1) {
            result.put("state", CashConstant.CASH_STATUS_APPLY);
        } else {
            result.put("state", CashConstant.CASH_STATUS_INIT);
        }

        return result;
    }
}
