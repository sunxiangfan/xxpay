package org.xxpay.mgr.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.xxpay.common.constant.CashConstant;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.MchWithdrawApply;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.common.base.Charsets.UTF_8;

public class ChannelDS {
    private static final String MCH_ID = "";

    private static final String MERCH_PRIVATE_KEY_TEXT = "";
    private final static MyLog _log = MyLog.getLog(ChannelDS.class);

    public JSONObject apply(MchWithdrawApply apply, CashChannel cashChannel,String bankCode) throws UnsupportedEncodingException, IllegalAccessException, IOException {
        _log.info("===========ds 代付接口开始处理============");
        _log.info("===========提现申请单id: {}============", apply.getId());
        String payUrl = "";
        long thirdDeduction = cashChannel.getThirdDeduction();
        long applyAmount = apply.getApplyAmount();
        long actualAmount = BigDecimalUtils.add(thirdDeduction, applyAmount);
        Map<String, String> bizData = buildBizData(apply, actualAmount,bankCode);
        Map<String, String> signData = buildPayParams(null, bizData, "P100784");

        try (Response res = HttpClientOkHttp.formPost(payUrl, signData)) {
            String json = res.body().string();
            JSONObject resultJson = JSON.parseObject(json);
            JSONObject result = new JSONObject();
            _log.info("===========ds 代付接口返回============");
            _log.info(json);
            result.put("state", "I102".equalsIgnoreCase(resultJson.getString("ret_code")));
            result.put("msg", resultJson.getString("ret_msg"));
//            result.put("channelOrderNo", resultJson.getString("out_order_no"));
            return result;
        }
    }

    public JSONObject queryCashState(String cashOrderId) throws Exception {
        String payUrl = "";

        Map<String, String> bizData = buildQueryBizData(cashOrderId);
        Map<String, String> signData = buildPayParams(null, bizData, "P100784");

        try (Response res = HttpClientOkHttp.formPost(payUrl, signData)) {
            String json = res.body().string();
            JSONObject resultJson = JSON.parseObject(json);
            Assert.isTrue("0000".equals(resultJson.getString("ret_code")), "返回失败！" + resultJson.getString("ret_msg"));
            String status = resultJson.getString("status");
            //"status":"SUCCESS"表示代付成功  FAILURE失败  PROCESS处理中
            JSONObject result = new JSONObject();
            if ("SUCCESS".equalsIgnoreCase(status)) {
                result.put("state", CashConstant.CASH_STATUS_SUCCESS);
                result.put("amount", resultJson.getString("amount"));
                String successTime=DateUtil.date2Str(new Date());
                result.put("cashSuccTime",successTime);
            } else if ("FAILURE".equalsIgnoreCase(status)) {
                result.put("state", CashConstant.CASH_STATUS_FAIL);
            } else {
                result.put("state", CashConstant.CASH_STATUS_APPLY);
            }
            return result;
        }

    }

    // 构建API参数。
    Map<String, String> buildPayParams(Map<String, String> customer, Map<String, String> bizMap, String bizCode) {
        if (customer == null) customer = ImmutableMap.of();
        String customMerchId = MCH_ID;
        String customBizCode = bizCode;
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

        _log.info("生成原始业务参数(biz_data)：{}", srcBizData);

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
//        System.out.printf("对待签名的内容字符串(sign_content)进行签名(sign)：%s%n", sign);

        // 除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
//        String encryptedBizData = AES.encryptToBase64(srcBizData, AES_KEY);
        String b64BizData = Base64.encodeBase64URLSafeString(srcBizData.getBytes(UTF_8));
        // 使用密文替换原业务参数数据
        params.put("biz_data", b64BizData);
//        System.out.printf("原始业务参数(biz_data)进行 base64 编码：%s%n", b64BizData);
        return params;
    }

    Map<String, String> buildBizData(MchWithdrawApply apply, long actualAmount,String bankCode) {
        TreeMap<String, String> bizMap = new TreeMap<String, String>();
        // 商户生成的订单号
        bizMap.put("out_order_no", apply.getId());
        // 交易金额，单位"分"
        bizMap.put("amount", actualAmount + "");
        // 商户服务端回调通知地址
        bizMap.put("notify_url", "");
        // 0: 对公，1：对私
        bizMap.put("pp_type", "1");
        // 银行账号（这里的银行帐号是虚拟的）
        bizMap.put("acc_no", apply.getNumber());
        // 银行账户名（这里的银行账户名是虚拟的）
        bizMap.put("acc_name", apply.getAccountName());
//        String bankCode = bankNameCodeMap.get(apply.getBankName());
        Assert.isTrue(!StringUtils.isEmpty(bankCode), "未找到该银行编码。名称：" + apply.getBankName());
        bizMap.put("bank_code", bankCode);
        // 银行开户行（这里的开户行是虚拟的）
        bizMap.put("opening_name", apply.getRegisteredBankName());
        // 银行预留手机号（必须与账户一致）
        bizMap.put("mobile", apply.getMobile());
        // 银行开户省份
        bizMap.put("prov", apply.getProvince());
        // 银行开户城市
        bizMap.put("city", apply.getCity());
        // 银行开户行联行号（这里的联行号是虚拟的）
        Assert.isTrue(!StringUtils.isEmpty(apply.getBankUnionCode()),"银行联号不能为空！");
        bizMap.put("cnaps", apply.getBankUnionCode());
        // RMB
        bizMap.put("currency", "CNY");
        return bizMap;
    }

    Map<String, String> buildQueryBizData(String cashOrderId) {
        TreeMap<String, String> bizMap = new TreeMap<String, String>();
        // 商户生成的订单号
        bizMap.put("out_order_no", cashOrderId);
        return bizMap;
    }


    String rsaSign(String content, PrivateKey privateKey) {
        return RSA.signBase64(content, privateKey);
    }

    protected static String toQueryString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder(32);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
