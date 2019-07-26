package org.xxpay.service.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.Three10000Config;
import org.xxpay.service.channel.Xq316Config;
import org.xxpay.service.channel.wechat.WxPayProperties;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import javax.annotation.Resource;
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
@RestController
public class PayChannel4Three10000Controller {

    private final MyLog _log = MyLog.getLog(PayChannel4Three10000Controller.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MchInfoService mchInfoService;

    private static final String logPrefix = "【three10000支付统一下单】";

    /**
     * 发起微信支付(统一下单)
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/pay/channel/three10000")
    public String doWxPayReq(@RequestParam String jsonParam) {
        try {
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
            String payOrderId = payOrder.getId();
//            String payBankCode = null;
            String mchId = payOrder.getMchId();
            String channelId = payOrder.getPayChannelId();
            MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if ("".equals(resKey))
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
            PayChannel payChannel = payChannelService.selectPayChannelById(channelId);
            String payType = payChannel.getPayType();
            try {
                Map<String, Object> retMap = unifiedOrder(payOrder, payChannel);
                _log.info("{} >>> 下单成功", logPrefix);
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
                map.put("payOrderId", payOrderId);
                int result = payOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("更新第三方支付订单号:payOrderId={},result={}", payOrderId, result);

                switch (payType) {
                    case PayConstant.PAY_TYPE_GATEWAY:
                    case PayConstant.PAY_TYPE_FAST_PAY:{
                        map.put("type", retMap.get("type"));
                        map.put("data", retMap.get("data"));
                        break;
                    }
                }
                return XXPayUtil.makeRetData(map, resKey);
            } catch (IllegalArgumentException e) {
                _log.info("{}下单返回失败", logPrefix);
                _log.info(e.getMessage());
                return XXPayUtil.makeRetData(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_FAIL, "0111", "调用支付失败," + e.getMessage()), resKey);

            }
        } catch (Exception e) {
            _log.error(e, "支付统一下单异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }

    Map<String, Object> unifiedOrder(PayOrder payOrder, PayChannel payChannel) throws IOException, IllegalAccessException {
        String apiUrl = Three10000Config.PAY_URL;
        String pay_memberid = Three10000Config.MCH_ID;
        String pay_orderid = payOrder.getId();
        String pay_amount = AmountUtil.convertCent2Dollar(payOrder.getAmount() + "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("Y-M-d H:m:s");
        String pay_applydate = dateFormat.format(new Date());
        String payType = payChannel.getPayType();

        String pay_service = null;
        if (payType.equals(PayConstant.PAY_TYPE_GATEWAY)) {
            pay_service = "907";
        } else if (payType.equals(PayConstant.PAY_TYPE_FAST_PAY)) {
            pay_service = "911";
        } else {
            throw new IllegalArgumentException("无效的支付类型：" + payType);
        }

        String pay_notifyurl = Three10000Config.NOTIFY_URL;
        String pay_callbackurl = payOrder.getFrontUrl();

        Map<String, String> signData = new HashMap<>();
        signData.put("pay_memberid", pay_memberid);
        signData.put("pay_orderid", pay_orderid);
        signData.put("pay_amount", pay_amount);
        signData.put("pay_applydate", pay_applydate);
        signData.put("pay_service", pay_service);
        signData.put("pay_notifyurl", pay_notifyurl);
        signData.put("pay_callbackurl", pay_callbackurl);
        String key = Three10000Config.KEY;
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
