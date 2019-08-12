package org.xxpay.shop.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.PayDigestUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/pay")
public class PayController {

    static final String baseUrl = "http://106.12.13.47:3010/api";
    static final String notifyUrl = "http://www.baidu.com";

    static final String mchId = "";
    // 加签key
    static final String reqKey = "yN8Oj38HY88OIIV4VvYAQdKqYrdVhCYCvYRQxbX34DVSYQ8RqwKn4Vh3jqvp3V8d4YQV7VYVHr44NbArhXdxwqKp";
    // 验签key
    static final String resKey = "";

    private AtomicLong seq = new AtomicLong(0L);

    @RequestMapping("/index")
    public String index(ModelMap modelMap) {
        return "pay/index";
    }

    @RequestMapping("/do_pay")
    public String doPay(@RequestParam HashMap<String, String> params, HttpServletResponse response) {
        String mchId = "10003";

        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        String payType = params.get("payType");
        //String amount=params.get("amount");
        //String centAmount= AmountUtil.convertDollar2Cent(amount);
        long amount = 200;
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
        paramMap.put("payType", payType);             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);                          // 支付金额,单位元
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", "iphone");
        paramMap.put("body", "iphone x");
        paramMap.put("frontUrl", "https://baidu.com");         // 回调URL
        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2
        paramMap.put("extra", "");  // 附加参数
        paramMap.put("bankCode", "ICBC");//测试
        paramMap.put("BkAcctNo", "6222023500015959782");// 卡号
        paramMap.put("IDNo", "231222199110194015");// 证件号
        paramMap.put("CstmrNm", "孙祥帆");// 持卡人姓名
        paramMap.put("MobNo", "13111110495");// 银行预留手机号

        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = baseUrl + "/pay/create_order";
// form1.submit()
        String html = "<html><body onload=''><form id='form1' action='" + url + "' method='post'>"
                + createForm(paramMap)
                + "<button type='submit'>提交</button></form></body></html>";
        try {
            response.setHeader("content-type", "text/html;charset=utf-8");
            response.getWriter().write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/fast_pay", method = RequestMethod.GET)
    @ResponseBody
    public String fast_pay(HttpServletResponse response) {
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);

        String mchId = "10003";
        long amount = 200;
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
        paramMap.put("payType", "FAST_PAY");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", "iphone");
        paramMap.put("body", "iphone x");
        paramMap.put("frontUrl", "https://baidu.com");         // 回调URL
        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2
        paramMap.put("extra", "");  // 附加参数
        paramMap.put("bankCode", "CIB");//测试敏付
        paramMap.put("cardNo", "6214623521001915597");//测试敏付
        paramMap.put("mobile", "18855782343");//测试敏付
        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = baseUrl + "/pay/create_order";

        String html = "<html><body onload=''><form id='form1' action='" + url + "' method='post'>"
                + createForm(paramMap)

                + "<button type='submit'>提交</button></form></body></html>";
        try {
            response.setHeader("content-type", "text/html;charset=utf-8");
            response.getWriter().write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
//        return result+"";
    }

    @RequestMapping(value = "/gateway", method = RequestMethod.GET)
    @ResponseBody
    public String gateway(HttpServletResponse response) {
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);

        String mchId = "10002";
        long amount = 1;
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
        paramMap.put("payType", "GATEWAY");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", "iphone");
        paramMap.put("body", "iphone x");
        paramMap.put("frontUrl", "https://www.baidu.com");         // 回调URL
        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2
        paramMap.put("extra", "");  // 附加参数

        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = baseUrl + "/pay/create_order";

        String html = "<html><body onload=''><form id='form1' action='" + url + "' method='post'>"
                + createForm(paramMap)

                + "<button type='submit'>提交</button></form></body></html>";
        try {
            response.setHeader("content-type", "text/html;charset=utf-8");
            response.getWriter().write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
//        return result+"";
    }


    private String createForm(Map<String, Object> map) {
        String form = "";
        for (String key : map.keySet()) {
            form += String.format("<input type='text' name='%s' value='%s' ></input>", key, map.get(key));
        }
        return form;

    }
}
