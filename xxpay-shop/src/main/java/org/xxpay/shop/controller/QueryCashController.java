package org.xxpay.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.util.HttpClient;
import org.xxpay.common.util.PayDigestUtil;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/query/cash")
public class QueryCashController {

    static final String reqKey = "";
    // 验签key
    static final String resKey = "";

    @RequestMapping("/test/{id}")
    public String test(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        // 支付请求返回结果
        String queryUrl = "http://localhost:3030/api/cash/query_order";
        String mchId = "";
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("mchOrderNo", id);
        reqMap.put("mchId", mchId);
        String sign = PayDigestUtil.getSign(reqMap, reqKey);
        reqMap.put("sign", sign);
        queryUrl += "?params=" + URLEncoder.encode(JSON.toJSONString(reqMap), "utf-8");

        String result = null;
//        result = new HttpClientUtil().doPost(wg, reqMap);
        result = HttpClient.callHttpPost(queryUrl);
        JSONObject jsonObject=JSON.parseObject(result);
        String tempSign=PayDigestUtil.getSign(jsonObject,resKey,"sign");

        String resSign=jsonObject.getString("sign");
        String orderResult=jsonObject.getString("result");
        String dd=new String( Base64Utils.decode(orderResult.getBytes("UTF-8")),"UTF-8");

//        Response res1 = HttpClientOkHttp.jsonPost(queryUrl, null);
//        result = res1.body().string();
        response.getWriter().print(result);
        return null;
    }
}
