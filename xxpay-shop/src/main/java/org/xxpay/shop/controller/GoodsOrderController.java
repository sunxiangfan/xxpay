package org.xxpay.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.*;
import org.xxpay.shop.dao.model.GoodsOrder;
import org.xxpay.shop.service.GoodsOrderService;
import org.xxpay.shop.util.Constant;
import org.xxpay.shop.util.OAuth2RequestParamHelper;
import org.xxpay.shop.util.vx.WxApi;
import org.xxpay.shop.util.vx.WxApiClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/goods")
public class GoodsOrderController {

    private final static MyLog _log = MyLog.getLog(GoodsOrderController.class);

    @Autowired
    private GoodsOrderService goodsOrderService;

    static final String mchId = "";
    // 加签key
    static final String reqKey = "Q6Rpd1k3EAABEUEW2n6WXAAqh35PAh63Rpn6h4WUnW3vNx0Psa0USPpqk0Kv3bU2aY20nAWY3S3UbbAbXQPbRFAQ";
    // 验签key
    static final String resKey = "iapT1pcMjpqVCh4nqpcjhp1dMJCSndJdn3DVjTrDa3aSjJUcG1dDinJWGoahDaW3adgjTJDjMWjqLgd5r5G1GWJr";
    //static final String baseUrl = "http://api.xxpay.org/api";
    static final String baseUrl = "http://127.0.0.1:3030/api";
    static final String notifyUrl = "http://shop.xxpay.org/goods/payNotify";
    private AtomicLong seq = new AtomicLong(0L);
    private final static String QR_PAY_URL = "http://shop.xxpay.org/goods/qrPay.html";
    static final String AppID = "";
    static final String AppSecret = "";
    private final static String GetOpenIdURL = "http://shop.xxpay.org/goods/getOpenId";
//    private final static String GetOpenIdURL2 = "http://shop.xxpay.org/goods/getOpenId2";
    private final static String GetOpenIdURL2 = "http://201p1g5819.51mypc.cn/goods/getOpenId2";


    @RequestMapping(value = "/buy/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    public String buy(@PathVariable("goodsId") String goodsId) {
        if (!"G_0001".equals(goodsId)) {
            return "fail";
        }
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setGoodsOrderId(goodsOrderId);
        goodsOrder.setGoodsId(goodsId);
        goodsOrder.setGoodsName("XXPAY捐助商品G_0001");
        goodsOrder.setAmount(100L);
        goodsOrder.setUserId("xxpay_000001");
        goodsOrder.setStatus(Constant.GOODS_ORDER_STATUS_INIT);
        int result = goodsOrderService.addGoodsOrder(goodsOrder);
        _log.info("插入商品订单,返回:{}", result);
        return result + "";
    }

    @RequestMapping(value = "/pay001", method = RequestMethod.GET)
    @ResponseBody
    public Map pay001(HttpServletResponse response) throws Exception {
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        long amount = 1000;

        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", "");                       // 商户ID
        paramMap.put("appId", "");                       // 商户ID
        paramMap.put("productId", "8001");                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
//        paramMap.put("payType", "TRANSFER");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", "iphone");
        paramMap.put("body", "iphone x");
        paramMap.put("returnUrl", notifyUrl);         // 回调URL
        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2
        paramMap.put("extra", "{\"productId\":\"120989823\",\"openId\":\"o2RvowBf7sOVJf8kJksUEMceaDqo\"}");  // 附加参数

//        String reqSign = PayDigestUtil.getSign(paramMap, "X4ODOZTGVQF6MIFRR0FXXYADHAXYNLCFB6VHIPVFZK8MIULD444CAKWEXHBHIF4ZVUUOVWUAUDQN90FLJBEB4YFC45XKKDCPYMVDEPVYZLQ4LNXN9N6D0FZSJEFDIYXN");
        String key = "";
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "?params=" + URLEncoder.encode(paramMap.toJSONString(), "utf-8");
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = "";
        String result = XXPayUtil.call4Post(url + reqData);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
        Map retMap = JSON.parseObject(result);
        if ("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, resKey, "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if (checkSign.equals(retSign)) {
                System.out.println("=========支付中心下单验签成功=========");
            } else {
                System.err.println("=========支付中心下单验签失败=========");
                return null;
            }
        }
        return retMap;
    }

    @RequestMapping("/pay002")
    public String pay002(HttpServletResponse response) throws IOException {
        String fxid = "";
        String apiUrl = "";
        String fxkey = "";
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        String fxnotifyurl = "http://localhost:8084/qzf/notifyUrl.htm";
        String fxbackurl = "http://localhost:8084/qzf/backUrl.htm";
        String fxattch = "test";
        String fxdesc = "desc";
        String fxfee = "10";
        String fxpay = "bank";
        String fxddh = goodsOrderId; //订单号
        String fxip = "127.0.0.1";

        //订单签名
//        String fxsign = MD5Tool.encoding(fxid + fxddh + fxfee + fxnotifyurl + fxkey);
        String fxsign = PayDigestUtil.md5(fxid + fxddh + fxfee + fxnotifyurl + fxkey, "utf-8");
        fxsign = fxsign.toLowerCase();

        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("fxid", fxid);
        reqMap.put("fxddh", fxddh);
        reqMap.put("fxfee", fxfee);
        reqMap.put("fxpay", fxpay);
        reqMap.put("fxnotifyurl", fxnotifyurl);
        reqMap.put("fxbackurl", fxbackurl);
        reqMap.put("fxattch", fxattch);
        reqMap.put("fxdesc", fxdesc);
        reqMap.put("fxip", fxip);
        reqMap.put("fxsign", fxsign);

        // 支付请求返回结果
        String result = null;
//        result = new HttpClientUtil().doPost(wg, reqMap);
        Response res1 = HttpClientOkHttp.formPost(apiUrl, reqMap);
        result = res1.body().string();
        response.getWriter().print(result);
        return null;
    }

    @RequestMapping(value = "/pay/{goodsOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public String pay(@PathVariable("goodsOrderId") String goodsOrderId,HttpServletResponse response) {
//        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);

//        GoodsOrder goodsOrder = goodsOrderService.getGoodsOrder(goodsOrderId);
//        if (goodsOrder == null) return "fail";
//        int status = goodsOrder.getStatus();
//        if (status != Constant.GOODS_ORDER_STATUS_INIT) {
//            return "fail_001";
//        }
        String mchId="";
        long amount = 1000;
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
//        paramMap.put("payType", "GATEWAY");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("payType", "FAST_PAY");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", "iphone");
        paramMap.put("body", "iphone x");
        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2
        paramMap.put("extra", "{\"productId\":\"120989823\",\"openId\":\"o2RvowBf7sOVJf8kJksUEMceaDqo\"}");  // 附加参数

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

    @RequestMapping(value = "/pay/test_api", method = RequestMethod.GET)
    @ResponseBody
    public String pay(HttpServletResponse response) {
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);

        String mchId="10002";
        long amount = 10000;
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
//        paramMap.put("payType", "GATEWAY");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("payType", "FAST_PAY");             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
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



    @RequestMapping(value = "/pay/test003", method = RequestMethod.GET)
    @ResponseBody
    public String  test(HttpServletResponse response){
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        String merchantId = "";
        String keyValue = "";
        String pay_bankcode="908";
        String pay_memberid = merchantId;//商户id
        String pay_orderid = goodsOrderId;//20位订单号 时间戳+6位随机字符串组成
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pay_applydate =simpleDateFormat.format(new Date());//yyyy-MM-dd HH:mm:ss
        String pay_notifyurl = "";//通知地址
        String pay_callbackurl = "";//回调地址
        String pay_amount = "10";
        String pay_attach = "";
        String pay_productname = "iphone";
        String pay_productnum = "";
        String pay_productdesc = "iphone";
        String pay_producturl = "";
//        String stringSignTemp = "pay_amount=" + pay_amount + "&pay_applydate=" + pay_applydate + "&pay_bankcode=" + pay_bankcode + "&pay_callbackurl=" + pay_callbackurl + "&pay_memberid=" + pay_memberid + "&pay_notifyurl=" + pay_notifyurl + "&pay_orderid=" + pay_orderid + "&key=" + keyValue + "";
//        String pay_md5sign = md5(stringSignTemp);
        Map<String, Object> result = new HashMap<>();
        result.put("pay_memberid",pay_memberid);
        result.put("pay_orderid",pay_orderid);
        result.put("pay_applydate",pay_applydate);
        result.put("pay_bankcode",pay_bankcode);
        result.put("pay_notifyurl",pay_notifyurl);
        result.put("pay_callbackurl",pay_callbackurl);
        result.put("pay_amount",pay_amount);

        String pay_md5sign=PayDigestUtil.getSign(result,keyValue);
        result.put("pay_md5sign",pay_md5sign);

        result.put("pay_reserved1","");
        result.put("pay_reserved2","");
        result.put("pay_reserved3","");
        result.put("pay_productname",pay_productname);
        result.put("pay_productnum",pay_productnum);
        result.put("pay_productdesc",pay_productdesc);
        result.put("pay_producturl",pay_producturl);

        String url="";

        String html = "<html><body onload=''><form id='form1' action='" + url + "' method='post'>"
                + createForm(result)

                + "<button type='submit'>提交</button></form></body></html>";
        try {
            response.setHeader("content-type", "text/html;charset=utf-8");
            response.getWriter().write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String createForm(Map<String, Object> map) {
        String form = "";
        for (String key : map.keySet()) {
            form += String.format("<input type='text' name='%s' value='%s' ></input>", key, map.get(key));
        }
        return form;

    }

    private Map createPayOrder(GoodsOrder goodsOrder, Map<String, Object> params) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrder.getGoodsOrderId());           // 商户订单号
        paramMap.put("channelId", params.get("channelId"));             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", goodsOrder.getAmount());                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", goodsOrder.getGoodsName());
        paramMap.put("body", goodsOrder.getGoodsName());
        paramMap.put("notifyUrl", notifyUrl);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2

        JSONObject extra = new JSONObject();
        extra.put("openId", params.get("openId"));
        paramMap.put("extra", extra.toJSONString());  // 附加参数

        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = baseUrl + "/pay/create_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
        Map retMap = JSON.parseObject(result);
        if ("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, resKey, "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if (checkSign.equals(retSign)) {
                System.out.println("=========支付中心下单验签成功=========");
            } else {
                System.err.println("=========支付中心下单验签失败=========");
                return null;
            }
        }
        return retMap;
    }

    @RequestMapping("/openQrPay.html")
    public String openQrPay(ModelMap model) {
        return "openQrPay";
    }

    @RequestMapping("/qrPay.html")
    public String qrPay(ModelMap model, HttpServletRequest request, Long amount) {
        String logPrefix = "【二维码扫码支付】";
        String view = "qrPay";
        _log.info("====== 开始接收二维码扫码支付请求 ======");
        String ua = request.getHeader("User-Agent");
        String goodsId = "G_0001";
        _log.info("{}接收参数:goodsId={},amount={},ua={}", logPrefix, goodsId, amount, ua);
        String client = "alipay";
        String channelId = "ALIPAY_WAP";
        if (StringUtils.isBlank(ua)) {
            String errorMessage = "User-Agent为空！";
            _log.info("{}信息：{}", logPrefix, errorMessage);
            model.put("result", "failed");
            model.put("resMsg", errorMessage);
            return view;
        } else {
            if (ua.contains("Alipay")) {
                client = "alipay";
                channelId = "ALIPAY_WAP";
            } else if (ua.contains("MicroMessenger")) {
                client = "wx";
                channelId = "WX_JSAPI";
            }
        }
        if (client == null) {
            String errorMessage = "请用微信或支付宝扫码";
            _log.info("{}信息：{}", logPrefix, errorMessage);
            model.put("result", "failed");
            model.put("resMsg", errorMessage);
            return view;
        }
        // 先插入订单数据
        GoodsOrder goodsOrder = null;
        Map<String, String> orderMap = null;
        if ("alipay".equals(client)) {
            _log.info("{}{}扫码下单", logPrefix, "支付宝");
            Map params = new HashMap<>();
            params.put("channelId", channelId);
            // 下单
            goodsOrder = createGoodsOrder(goodsId, amount);
            orderMap = createPayOrder(goodsOrder, params);
        } else if ("wx".equals(client)) {
            _log.info("{}{}扫码", logPrefix, "微信");
            // 判断是否拿到openid，如果没有则去获取
            String openId = request.getParameter("openId");
            if (StringUtils.isNotBlank(openId)) {
                _log.info("{}openId：{}", logPrefix, openId);
                Map params = new HashMap<>();
                params.put("channelId", channelId);
                params.put("openId", openId);
                goodsOrder = createGoodsOrder(goodsId, amount);
                // 下单
                orderMap = createPayOrder(goodsOrder, params);
            } else {
                String redirectUrl = QR_PAY_URL + "?amount=" + amount;
                String url = GetOpenIdURL2 + "?redirectUrl=" + redirectUrl;
                _log.info("跳转URL={}", url);
                return "redirect:" + url;
            }
        }
        model.put("goodsOrder", goodsOrder);
        model.put("amount", AmountUtil.convertCent2Dollar(goodsOrder.getAmount() + ""));
        if (orderMap != null) {
            model.put("orderMap", orderMap);
            String payOrderId = orderMap.get("payOrderId");
            GoodsOrder go = new GoodsOrder();
            go.setGoodsOrderId(goodsOrder.getGoodsOrderId());
            go.setPayOrderId(payOrderId);
            go.setChannelId(channelId);
            int ret = goodsOrderService.update(go);
            _log.info("修改商品订单,返回:{}", ret);
        }
        model.put("client", client);
        return view;
    }

    GoodsOrder createGoodsOrder(String goodsId, Long amount) {
        // 先插入订单数据
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setGoodsOrderId(goodsOrderId);
        goodsOrder.setGoodsId(goodsId);
        goodsOrder.setGoodsName("XXPAY捐助商品G_0001");
        goodsOrder.setAmount(amount);
        goodsOrder.setUserId("xxpay_000001");
        goodsOrder.setStatus(Constant.GOODS_ORDER_STATUS_INIT);
        int result = goodsOrderService.addGoodsOrder(goodsOrder);
        _log.info("插入商品订单,返回:{}", result);
        return goodsOrder;
    }

    /**
     * 获取code
     *
     * @return
     */
    @RequestMapping("/getOpenId")
    public void getOpenId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        _log.info("进入获取用户openID页面");
        String redirectUrl = request.getParameter("redirectUrl");
        String code = request.getParameter("code");
        String openId = "";
        if (!StringUtils.isBlank(code)) {//如果request中包括code，则是微信回调
            try {
                openId = WxApiClient.getOAuthOpenId(AppID, AppSecret, code);
                _log.info("调用微信返回openId={}", openId);
            } catch (Exception e) {
                _log.error(e, "调用微信查询openId异常");
            }
            if (redirectUrl.indexOf("?") > 0) {
                redirectUrl += "&openId=" + openId;
            } else {
                redirectUrl += "?openId=" + openId;
            }
            response.sendRedirect(redirectUrl);
        } else {//oauth获取code
            String redirectUrl4Vx = GetOpenIdURL + "?redirectUrl=" + redirectUrl;
            String state = OAuth2RequestParamHelper.prepareState(request);
            String url = WxApi.getOAuthCodeUrl(AppID, redirectUrl4Vx, "snsapi_base", state);
            _log.info("跳转URL={}", url);
            response.sendRedirect(url);
        }
    }

    /**
     * 获取code
     *
     * @return
     */
    @RequestMapping("/getOpenId2")
    public void getOpenId2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        _log.info("进入获取用户openID页面");
        String redirectUrl = request.getParameter("redirectUrl");
        String code = request.getParameter("code");
        String openId = "";
        if (!StringUtils.isBlank(code)) {//如果request中包括code，则是微信回调
            try {
                openId = WxApiClient.getOAuthOpenId(AppID, AppSecret, code);
                _log.info("调用微信返回openId={}", openId);
            } catch (Exception e) {
                _log.error(e, "调用微信查询openId异常");
            }
            if (redirectUrl.indexOf("?") > 0) {
                redirectUrl += "&openId=" + openId;
            } else {
                redirectUrl += "?openId=" + openId;
            }
            response.sendRedirect(redirectUrl);
        } else {//oauth获取code
            //http://www.abc.com/xxx/get-weixin-code.html?appid=XXXX&scope=snsapi_base&state=hello-world&redirect_uri=http%3A%2F%2Fwww.xyz.com%2Fhello-world.html
            String redirectUrl4Vx = GetOpenIdURL2 + "?redirectUrl=" + redirectUrl;
            String url = String.format("http://www.xiaoshuding.com/get-weixin-code.html?appid=%s&scope=snsapi_base&state=hello-world&redirect_uri=%s", AppID, WxApi.urlEnodeUTF8(redirectUrl4Vx));
            _log.info("跳转URL={}", url);
            response.sendRedirect(url);
        }
    }

    /**
     * 接收支付中心通知
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/payNotify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _log.info("====== 开始处理支付中心通知 ======");
        Map<String, Object> paramMap = request2payResponseMap(request, new String[]{
                "payOrderId", "mchId", "mchOrderNo", "channelId", "amount", "currency", "status", "clientIp",
                "device", "subject", "channelOrderNo", "param1",
                "param2", "paySuccTime", "backType", "sign"
        });
        _log.info("支付中心通知请求参数,paramMap={}", paramMap);
        outResult(response, "success");
        return;
//
//        if (!verifyPayResponse(paramMap)) {
//            String errorMessage = "verify request param failed.";
//            _log.warn(errorMessage);
//            outResult(response, "fail");
//            return;
//        }
//        String payOrderId = (String) paramMap.get("payOrderId");
//        String mchOrderNo = (String) paramMap.get("mchOrderNo");
//        String resStr;
//        try {
//            GoodsOrder goodsOrder = goodsOrderService.getGoodsOrder(mchOrderNo);
//            if (goodsOrder != null && goodsOrder.getStatus() == Constant.GOODS_ORDER_STATUS_COMPLETE) {
//                outResult(response, "success");
//                return;
//            }
//            // 执行业务逻辑
//            int ret = goodsOrderService.updateStatus4Success(mchOrderNo);
//            // ret返回结果
//            // 等于1表示处理成功,返回支付中心success
//            // 其他值,返回支付中心fail,让稍后再通知
//            if (ret == 1) {
//                ret = goodsOrderService.updateStatus4Complete(mchOrderNo);
//                if (ret == 1) {
//                    resStr = "success";
//                } else {
//                    resStr = "fail";
//                }
//            } else {
//                resStr = "fail";
//            }
//        } catch (Exception e) {
//            resStr = "fail";
//            _log.error(e, "执行业务异常,payOrderId=%s.mchOrderNo=%s", payOrderId, mchOrderNo);
//        }
//        _log.info("响应支付中心通知结果:{},payOrderId={},mchOrderNo={}", resStr, payOrderId, mchOrderNo);
//        outResult(response, resStr);
//        _log.info("====== 支付中心通知处理完成 ======");
    }

    @RequestMapping("/notify_test")
    public void notifyTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        outResult(response, "success");
    }

    @RequestMapping("/toAliPay.html")
    @ResponseBody
    public String toAliPay(HttpServletRequest request, Long amount, String channelId) {
        String logPrefix = "【支付宝支付】";
        _log.info("====== 开始接收支付宝支付请求 ======");
        String goodsId = "G_0001";
        _log.info("{}接收参数:goodsId={},amount={},channelId={}", logPrefix, goodsId, amount, channelId);
        // 先插入订单数据
        Map params = new HashMap<>();
        params.put("channelId", channelId);
        // 下单
        GoodsOrder goodsOrder = createGoodsOrder(goodsId, amount);
        Map<String, String> orderMap = createPayOrder(goodsOrder, params);
        if (orderMap != null && "success".equalsIgnoreCase(orderMap.get("resCode"))) {
            String payOrderId = orderMap.get("payOrderId");
            GoodsOrder go = new GoodsOrder();
            go.setGoodsOrderId(goodsOrder.getGoodsOrderId());
            go.setPayOrderId(payOrderId);
            go.setChannelId(channelId);
            int ret = goodsOrderService.update(go);
            _log.info("修改商品订单,返回:{}", ret);
        }
        if (PayConstant.PAY_CHANNEL_ALIPAY_MOBILE.equalsIgnoreCase(channelId)) return orderMap.get("payParams");
        return orderMap.get("payUrl");
    }

    void outResult(HttpServletResponse response, String content) {
        response.setContentType("text/html");
        PrintWriter pw;
        try {
            pw = response.getWriter();
            pw.print(content);
            _log.error("response xxpay complete.");
        } catch (IOException e) {
            _log.error(e, "response xxpay write exception.");
        }
    }

    public Map<String, Object> request2payResponseMap(HttpServletRequest request, String[] paramArray) {
        Map<String, Object> responseMap = new HashMap<>();
        for (int i = 0; i < paramArray.length; i++) {
            String key = paramArray[i];
            String v = request.getParameter(key);
            if (v != null) {
                responseMap.put(key, v);
            }
        }
        return responseMap;
    }

    public boolean verifyPayResponse(Map<String, Object> map) {
        String mchId = (String) map.get("mchId");
        String payOrderId = (String) map.get("payOrderId");
        String mchOrderNo = (String) map.get("mchOrderNo");
        String amount = (String) map.get("amount");
        String sign = (String) map.get("sign");

        if (StringUtils.isEmpty(mchId)) {
            _log.warn("Params error. mchId={}", mchId);
            return false;
        }
        if (StringUtils.isEmpty(payOrderId)) {
            _log.warn("Params error. payOrderId={}", payOrderId);
            return false;
        }
        if (StringUtils.isEmpty(amount) || !NumberUtils.isNumber(amount)) {
            _log.warn("Params error. amount={}", amount);
            return false;
        }
        if (StringUtils.isEmpty(sign)) {
            _log.warn("Params error. sign={}", sign);
            return false;
        }

        // 验证签名
        if (!verifySign(map)) {
            _log.warn("verify params sign failed. payOrderId={}", payOrderId);
            return false;
        }

        // 根据payOrderId查询业务订单,验证订单是否存在
        GoodsOrder goodsOrder = goodsOrderService.getGoodsOrder(mchOrderNo);
        if (goodsOrder == null) {
            _log.warn("业务订单不存在,payOrderId={},mchOrderNo={}", payOrderId, mchOrderNo);
            return false;
        }
        // 核对金额
        if (goodsOrder.getAmount() != Long.parseLong(amount)) {
            _log.warn("支付金额不一致,dbPayPrice={},payPrice={}", goodsOrder.getAmount(), amount);
            return false;
        }
        return true;
    }

    public boolean verifySign(Map<String, Object> map) {
        String mchId = (String) map.get("mchId");
        if (!this.mchId.equals(mchId)) return false;
        String localSign = PayDigestUtil.getSign(map, resKey, "sign");
        String sign = (String) map.get("sign");
        return localSign.equalsIgnoreCase(sign);
    }

}