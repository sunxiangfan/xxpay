package org.xxpay.web.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description: 网关
 * @date 2019-3-25
 * @Copyright: www.xxpay.org
 */
@Service
public class PayChannel4MzwyqService {

    private final MyLog _log = MyLog.getLog(PayChannel4MzwyqService.class);


    private static final String logPrefix = "【three10000支付统一下单】";

    private static final String PAY_URL = "";
    private static final String MCH_ID = "";
    private static final String KEY = "";
    private static final String NOTIFY_URL = "http://localhost:3030/notify/pay/mzwyqPayNotifyRes.htm";

    /**
     * 发起微信支付(统一下单)
     *
     * @param
     * @return
     */
    public Map<String, Object> buildRequestData(JSONObject payOrder) {
        try {
            String payOrderId = payOrder.getString("id");
            String payType = payOrder.getString("payType");
            try {
                Map<String, Object> retMap = unifiedOrder(payOrder);
                _log.info("{} >>> 下单成功", logPrefix);
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
                map.put("payOrderId", payOrderId);

                switch (payType) {
                    case PayConstant.PAY_TYPE_ALIPAY_WAP: {
                        map.put("type", retMap.get("type"));
                        map.put("data", retMap.get("data"));
                        break;
                    }
                }
                return map;
            } catch (IllegalArgumentException e) {
                _log.info("{}下单返回失败", logPrefix);
                _log.info(e.getMessage());
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, null);
                return map;
            }
        } catch (Exception e) {
            _log.error(e, "支付统一下单异常");
            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, null);
            return map;
        }
    }

    Map<String, Object> unifiedOrder(JSONObject payOrder) throws IOException, IllegalAccessException {
        String apiUrl = PAY_URL;
        String pay_memberid = MCH_ID;
        String pay_orderid = payOrder.getString("id");
        String pay_amount = AmountUtil.convertCent2Dollar(payOrder.getLong("amount") + "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("Y-M-d H:m:s");
        String pay_applydate = dateFormat.format(new Date());
        String payType = payOrder.getString("payType");

        String pay_service = null;
        if (payType.equals(PayConstant.PAY_TYPE_ALIPAY_WAP)) {
            pay_service = "922";
        } else {
            throw new IllegalArgumentException("无效的支付类型：" + payType);
        }

        String pay_notifyurl = NOTIFY_URL;
        String pay_callbackurl = "http://localhost:3030/notify/pay/front_url/" + pay_orderid + ".htm";//回调地址

        Map<String, String> signData = new HashMap<>();
        signData.put("pay_memberid", pay_memberid);
        signData.put("pay_orderid", pay_orderid);
        signData.put("pay_amount", pay_amount);
        signData.put("pay_applydate", pay_applydate);
        signData.put("pay_service", pay_service);
        signData.put("pay_notifyurl", pay_notifyurl);
        signData.put("pay_callbackurl", pay_callbackurl);
        String key = KEY;
        String sign = PayDigestUtil.getSign(signData, key);
        signData.put("pay_md5sign", sign);

        _log.info("上游下单请求数据：{}" + JSON.toJSONString(signData));
        // 支付请求返回结果
        String result = null;
        try (Response res1 = HttpClientOkHttp.formPost(apiUrl, signData)) {
            result = res1.body().string();
        }
        _log.info("上游下单返回数据：{}" + result);
        Assert.isTrue(!StringUtils.isEmpty(result), "下单失败！上游下单失败！");
        JSONObject retMap = JSON.parseObject(result);
        Assert.isTrue(retMap.getString("status").equals("success"), "下单失败！详情 :msg:" + retMap.get("msg"));
        return retMap;

    }

}
