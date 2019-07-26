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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.enumm.MchState;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.enumm.PayChannelState;
import org.xxpay.common.util.*;
import org.xxpay.web.service.*;
import org.xxpay.web.service.channel.PayO9nService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description: 提现申请
 * @date 2019-04-01
 */
@Controller
public class CashOrderController {

    private final MyLog _log = MyLog.getLog(CashOrderController.class);

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    @Autowired
    private CashOrderServiceClient cashOrderServiceClient;

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
    @RequestMapping(value = "/cash/create_order")
    public Object cashOrder(@RequestParam Map<String, String> params, HttpServletResponse response, ModelMap model) {
        _log.info("###### 开始接收商户提现申请请求 ######");
        String logPrefix = "【商户提现申请】";
        ServiceInstance instance = client.getLocalServiceInstance();
        _log.info("{}/pay/create_order, host:{}, service_id:{}, params:{}", logPrefix, instance.getHost(), instance.getServiceId(), params);
        String retMsg = null;
        try {
            JSONObject po = (JSONObject) JSON.toJSON(params);
            JSONObject cashOrder = null;
            // 验证参数有效性
            Object object = validateParams(po);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
                return writeResponse(response, retMsg);
            }
            if (object instanceof JSONObject) cashOrder = (JSONObject) object;
            if (cashOrder == null) {
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "提现中心下单失败", null, null));
                return writeResponse(response, retMsg);
            }
            String result = cashOrderServiceClient.createCashOrder(cashOrder.toJSONString());
            _log.info("{}创建支付订单,结果:{}", logPrefix, result);
            if (StringUtils.isEmpty(result)) {
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建提现订单失败", null, null));
                return writeResponse(response, retMsg);
            }
            JSONObject resObj = JSON.parseObject(result);
            if (resObj == null || !"1".equals(resObj.getString("result"))) {
                retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建提现订单失败。" + resObj.getString("msg"), null, null));
                return writeResponse(response, retMsg);
            }
            retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "创建提现订单成功", PayConstant.RETURN_VALUE_SUCCESS, null));
            return writeResponse(response, retMsg);
        } catch (Exception e) {
            _log.error(e, "");
            retMsg = XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "提现中心系统异常", null, null));
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
            String sign = params.getString("sign");                // 签名
            String mchId = params.getString("mchId");                // 商户ID
            String mchOrderNo = params.getString("mchOrderNo");    // 商户订单号
            String strAmount = params.getString("amount");            // 支付金额（单位分）
            String currency = params.getString("currency");         // 币种
            String number = params.getString("number");               //卡号
            String bankName = params.getString("bankName");           //银行名称
            String registeredBankName = params.getString("registeredBankName"); //开户行
            String accountName = params.getString("accountName");     //账户名
            String mobile = params.getString("mobile");              //预留手机号
            String province = params.getString("province");           //省
            String city = params.getString("city");                   //市
            String idCard = params.getString("idCard");                   //身份证号

            Assert.isTrue(StringUtils.isNotBlank(sign), "签名不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(mchId), "商户号不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(mchOrderNo), "商户订单号不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(strAmount), "申请金额不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(number), "卡号不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(bankName), "银行名称不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(registeredBankName), "开户行不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(accountName), "账户名不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(mobile), "预留手机号不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(province), "省不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(city), "市不能为空！");
//            Assert.isTrue(StringUtils.isNotBlank(idCard),"身份证号不能为空！");

            Assert.isTrue(NumberUtils.isNumber(strAmount), "request params[amount] error.");
            Long amount = Long.parseLong(strAmount);
            Assert.isTrue(amount.compareTo(0L) > 0, "request params[amount] error.");
            Assert.isTrue(StringUtils.isNotBlank(currency), "request params[currency] error.");

            // 查询商户信息
            JSONObject mchInfo = getMchInfo(mchId);
            String reqKey = mchInfo.getString("reqKey");
            Byte mchType = MchType.MERCHANT.getCode();
            // 查询商户订单号是否已存在
            testMchOrderNo(mchId, mchOrderNo);

            // 验证签名数据
            boolean verifyFlag = XXPayUtil.verifyPaySign(params, reqKey);
            Assert.isTrue(verifyFlag, "Verify sign failed.");

            // 验证参数通过,返回JSONObject对象

            JSONObject cashOrder = new JSONObject();
            cashOrder.put("mchId", mchId);
            cashOrder.put("mchType", mchType);
            cashOrder.put("applyAmount", amount);
            cashOrder.put("mchOrderNo", mchOrderNo);
            cashOrder.put("number", number);
            cashOrder.put("registeredBankName", registeredBankName);
            cashOrder.put("bankName", bankName);
            cashOrder.put("mobile", mobile);
            cashOrder.put("accountName", accountName);
            cashOrder.put("idCard", idCard);
            cashOrder.put("province", province);
            cashOrder.put("city", city);
            return cashOrder;
        } catch (IllegalArgumentException ex) {
            errorMessage = ex.getMessage();
            return errorMessage;
        }
    }

    JSONObject testMchOrderNo(String mchId, String mchOrderNo) {
        // 查询商户信息
        String retStr = cashOrderServiceClient.queryCashOrder(getJsonParam(new String[]{"mchId", "mchOrderNo"}, new Object[]{mchId, mchOrderNo}));

        JSONObject retObj = JSON.parseObject(retStr);
        JSONObject cashOrder = retObj.getJSONObject("result");
        Assert.isNull(cashOrder, "cashOrder exist [mchId=" + mchId + ", mchOrderNo=" + mchOrderNo + "]  record in db.");
        return cashOrder;
    }

    JSONObject getMchInfo(String mchId) {
        // 查询商户信息
        String retStr = mchInfoServiceClient.selectMchInfo(getJsonParam("mchId", mchId));

        JSONObject retObj = JSON.parseObject(retStr);
        boolean retSucceed = retSuccess(retObj, "mchInfo");
        Assert.isTrue(retSucceed, "Can't found mchInfo[mchId=" + mchId + "] record in db.");

        JSONObject mchInfo = retObj.getJSONObject("result");
        Assert.notNull(mchInfo, "Can't found mchInfo[mchId=" + mchId + "] record in db.");
        Assert.isTrue(MchState.ENABLE.getCode().equals(mchInfo.getByte("state")), "mchInfo not available [mchId=" + mchId + "] record in db.");


        String reqKey = mchInfo.getString("reqKey");
        Assert.isTrue(StringUtils.isNotBlank(reqKey), "reqKey is null[mchId=" + mchId + "] record in db.");

        String agentMchId = mchInfo.getString("agentId");
        Assert.isTrue(StringUtils.isNotBlank(agentMchId), "agentId is null[mchId=" + mchId + "] record in db.");
        return mchInfo;
    }


    boolean retSuccess(JSONObject retObj, String entityName) {
        String retCode = retObj.getString("code");
        boolean succeed = "0000".equals(retCode);
        if (!succeed) {
            _log.info("查询{}没有正常返回数据,code={},msg={}", entityName, retObj.getString("code"), retObj.getString("msg"));
        }
        return succeed;
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

}
