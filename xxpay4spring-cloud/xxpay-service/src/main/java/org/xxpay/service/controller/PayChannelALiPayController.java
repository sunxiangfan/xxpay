package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.changjie.*;
import org.xxpay.service.service.PayOrderService;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version V1.0
 * @Description: 支付渠道接口:changjie
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannelALiPayController {

//    @Autowired
//    private PayOrderService payOrderService;
//    @Autowired
//    private MchInfoMapper mchInfoMapper;

    private final MyLog log = MyLog.getLog(PayChannelALiPayController.class);

    @RequestMapping("/pay/channel/alipay")
    public String nmg_ebank_pay(@RequestParam String jsonParam) {
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        JSONObject payOrders = paramObj.getJSONObject("payOrder");
        String mchId = payOrders.getString("mchId");
        Integer amount = payOrders.getInteger("amount");
        String mchOrderNo = payOrders.getString("mchOrderNo");

        Map map = new HashMap();
        map.put("orderNo", mchOrderNo);
        map.put("channelCode", "HPY");
        map.put("amount", changeF2Y(amount));
        map.put("userId", 12);

        String url = "http://shop.xxscloud.com/api/open/order/createOrder";
        String result = HttpClientUtilNew.post(JSONObject.parseObject(JSON.toJSONString(map)), url);

        JSONObject json = JSONObject.parseObject(result);
        JSONObject jsonData = JSONObject.parseObject(json.getString("data"));
        String resultUrl = jsonData.getString("url");
        System.out.println(resultUrl);
        return resultUrl;
    }

    public static String changeF2Y(int price) {
        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
    }

}