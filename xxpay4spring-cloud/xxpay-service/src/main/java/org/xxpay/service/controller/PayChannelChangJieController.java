package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.service.channel.changjie.*;

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
public class PayChannelChangJieController {

    private final MyLog log = MyLog.getLog(PayChannelChangJieController.class);

    /**
     * 畅捷支付平台公钥
     */
    private static String MERCHANT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPq3oXX5aFeBQGf3Ag/86zNu0VICXmkof85r+DDL46w3vHcTnkEWVbp9DaDurcF7DMctzJngO0u9OG1cb4mn+Pn/uNC1fp7S4JH4xtwST6jFgHtXcTG9uewWFYWKw/8b3zf4fXyRuI/2ekeLSstftqnMQdenVP7XCxMuEnnmM1RwIDAQAB";//生产环境公钥

    /**
     * 商户号私钥
     */
    private static String MERCHANT_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOhQ+OV9NjzRHgFqsEatX149qvEcXiyAlKIJhVbluAi46q3CLRivVGeuQ7LQ7Wt99HJ1maYpQv3sedryKVlWe3FiGIo6En/+E62s6PVAVXcvTmM3lNNlDVD8arp4Qy7wH5M7ofqSKsKCibtIzQkkhio6O+ti2Tjf7rDcl8TE9fefAgMBAAECgYEAtN++OWa4YLdrxiybnBuF4ejfe6Pbioh9sH77KsHl/ByE0s4YsFxpueOK3+EcyJEjTi5Td3QurWZ9JUNfgCf+KLgrusJgUW4tOLbaS51HRFsTgXbqiEw7dr4jPKuSTbeMkmuhp/57hbsUpPoyCKsmtzgMLVMa/HUXoLzh0dcg+fECQQD4AiITSFestrUmJmh+NzQsQkVfdqkC8oVbhIfCoo/uh+YMIvRmSD2oK9c9EIjrmeQxREriU4DGLbatbALJqB2HAkEA781kg3iYPOZkG9bm7bkMMqVNbgPjwgx93V2xReVKFOl9xb/WZweHhiwPaOy/F2BZNOzxjsO0BCjaacIl7bQbKQJABLI9pPHUvy+ChKNrjSBMe54RpDoh/y1Key4qR/Q+F305TPeIeztY94tE+yIKBbQXTxuE834zTQ1mjSgjcWAelwJASORFBlWU6QYbLf8v6NjT5V0r4SIbjDOh2rUNGrLsxtiGm6KJeH3oaxdfg8Ra/a8SzqyrbHr+cDk+0uDqCIwzqQJBAK1E2Y7SnH4QS+F50j/TMC1wEBmQjRBt+E0AjjMoti7O0zU/MzjvguIiAVeRuxs3AVRGmpw2yaYsUjky6nb9i2I=";


    private static String urlStr = "https://pay.chanpay.com/mag-unify/gateway/receiveOrder.do?";

    /**
     * 编码类型
     */
    private static String charset = "UTF-8";

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String parttern)
            throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }

    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取钱包的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
     * "",sParaTemp)
     * <p>
     * 文件类型的参数名
     * 文件路径
     *
     * @param sParaTemp 请求参数数组
     * @return 钱包处理结果
     * @throws Exception
     */
    public static String buildRequest(Map<String, String> sParaTemp, String signType, String key, String inputCharset,
                                      String gatewayUrl) throws Exception {
        // 待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp, signType, key, inputCharset);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        // 设置编码集
        request.setCharset(inputCharset);
        request.setMethod(HttpRequest.METHOD_POST);
        request.setParameters(generatNameValuePair(createLinkRequestParas(sPara), inputCharset));
        request.setUrl(gatewayUrl);
        System.out.println(gatewayUrl + "" + httpProtocolHandler.toString(generatNameValuePair(createLinkRequestParas(sPara), inputCharset)));
        if (sParaTemp.get("Service").equalsIgnoreCase("nmg_quick_onekeypay") || sParaTemp.get("Service").equalsIgnoreCase("nmg_nquick_onekeypay")) {
            return null;
        }

        // 返回结果处理
//        HttpResponse response = httpProtocolHandler.execute(request, null, null);
//        if (response == null) {
//            return null;
//        }
//        String strResult = response.getStringResult();

        String str = JSONObject.toJSONString(request.getParameters());
        return str;
    }

    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取钱包的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
     * "",sParaTemp)
     * <p>
     * 文件类型的参数名
     * 文件路径
     *
     * @param sParaTemp 请求参数数组
     * @return 钱包处理结果
     * @throws Exception
     */
    public static String buildFastPayRequest(Map<String, String> sParaTemp, String signType, String key, String inputCharset,
                                             String gatewayUrl) throws Exception {
        // 待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp, signType, key, inputCharset);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        // 设置编码集
        request.setCharset(inputCharset);
        request.setMethod(HttpRequest.METHOD_POST);
        request.setParameters(generatNameValuePair(createLinkRequestParas(sPara), inputCharset));
        request.setUrl(gatewayUrl);
        System.out.println(gatewayUrl + "" + httpProtocolHandler.toString(generatNameValuePair(createLinkRequestParas(sPara), inputCharset)));
        if (sParaTemp.get("Service").equalsIgnoreCase("nmg_quick_onekeypay") || sParaTemp.get("Service").equalsIgnoreCase("nmg_nquick_onekeypay")) {
            return null;
        }

        // 返回结果处理
        HttpResponse response = httpProtocolHandler.execute(request, null, null);
        if (response == null) {
            return null;
        }
        String strResult = response.getStringResult();
        System.out.println(strResult);
        return strResult;
    }

    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取钱包的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
     * "",sParaTemp)
     * <p>
     * 文件类型的参数名
     * 文件路径
     *
     * @param sParaTemp 请求参数数组
     * @return 钱包处理结果
     * @throws Exception
     */
    public static Object buildRequests(Map<String, String> sParaTemp,
                                       String signType, String key, String inputCharset, String gatewayUrl)
            throws Exception {
        // 待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp, signType, key,
                inputCharset);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler
                .getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        // 设置编码集
        request.setCharset(inputCharset);
        request.setMethod(HttpRequest.METHOD_POST);
        request.setParameters(generatNameValuePair(
                createLinkRequestParas(sPara), inputCharset));
        request.setUrl(gatewayUrl);
        HttpResponse response = httpProtocolHandler
                .execute(request, null, null);
        if (response == null) {
            return null;
        }

        byte[] byteResult = response.getByteResult();
        if (byteResult.length > 1024) {
            return byteResult;
        } else {
            return response.getStringResult();
        }

    }

    @SuppressWarnings("unused")
    private static Map convertMap(Map<?, ?> parameters, String InputCharset)
            throws ArrayIndexOutOfBoundsException, UnsupportedEncodingException, IllegalArgumentException {
        Map<String, String> formattedParameters = new HashMap<String, String>(parameters.size());
        for (Map.Entry<?, ?> entry : parameters.entrySet()) {
            if (entry.getValue() == null || Array.getLength(entry.getValue()) == 0) {
                formattedParameters.put((String) entry.getKey(), null);
            } else {
                String value = new String(((String) Array.get(entry.getValue(), 0)).getBytes(InputCharset), charset);

            }
        }
        return formattedParameters;
    }

    /**
     * MAP类型数组转换成NameValuePair类型
     *
     * @param properties MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties, String charset)
            throws Exception {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            // nameValuePair[i++] = new NameValuePair(entry.getKey(),
            // URLEncoder.encode(entry.getValue(),charset));
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }
        return nameValuePair;
    }

    /**
     * 生成要请求参数数组
     *
     * @param sParaTemp    请求前的参数数组
     * @param signType     RSA
     * @param key          商户自己生成的商户私钥
     * @param inputCharset UTF-8
     * @return 要请求的参数数组
     * @throws Exception
     */
    public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String signType, String key,
                                                       String inputCharset) throws Exception {
        // 除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        // 生成签名结果
        String mysign = "";
        if ("MD5".equalsIgnoreCase(signType)) {
            mysign = buildRequestByMD5(sPara, key, inputCharset);
        } else if ("RSA".equalsIgnoreCase(signType)) {
            mysign = buildRequestByRSA(sPara, key, inputCharset);
        }
        // 签名结果与签名方式加入请求提交参数组中
        System.out.println("Sign:" + mysign);
        sPara.put("Sign", mysign);
        sPara.put("SignType", signType);

        return sPara;
    }

    /**
     * 生成MD5签名结果
     *
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestByMD5(Map<String, String> sPara, String key, String inputCharset)
            throws Exception {
        String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = MD5.sign(prestr, key, inputCharset);
        return mysign;
    }

    /**
     * 生成RSA签名结果
     *
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestByRSA(Map<String, String> sPara, String privateKey, String inputCharset)
            throws Exception {
        String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = RSA.sign(prestr, privateKey, inputCharset);
        return mysign;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     *               是否需要urlEncode
     * @return 拼接后字符串
     */
    public static Map<String, String> createLinkRequestParas(Map<String, String> params) {
        Map<String, String> encodeParamsValueMap = new HashMap<String, String>();
        List<String> keys = new ArrayList<String>(params.keySet());
        String charset = params.get("InputCharset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value;
            try {
                value = URLEncoder.encode(params.get(key), charset);
                encodeParamsValueMap.put(key, value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return encodeParamsValueMap;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {

        params = paraFilter(params);

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        String charset = params.get("InputCharset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("Sign") || key.equalsIgnoreCase("SignType") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 向服务器发送post请求
     *
     * @param origMap              参数map
     * @param charset              编码字符集
     * @param MERCHANT_PRIVATE_KEY 私钥
     * @throws Exception
     */
    public String gatewayPost(Map<String, String> origMap, String charset, String MERCHANT_PRIVATE_KEY) {
        String result = "";
        try {
            Map<String, String> sPara = buildRequestPara(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset);
            result = buildRequest(origMap, "RSA", PayChannelChangJieController.MERCHANT_PRIVATE_KEY, charset, urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向服务器发送post请求
     *
     * @param origMap              参数map
     * @param charset              编码字符集
     * @param MERCHANT_PRIVATE_KEY 私钥
     * @throws Exception
     */
    public String fastpayPost(Map<String, String> origMap, String charset, String MERCHANT_PRIVATE_KEY) {
        String result = "";
        try {
            Map<String, String> sPara = buildRequestPara(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset);
            result = buildFastPayRequest(origMap, "RSA", PayChannelChangJieController.MERCHANT_PRIVATE_KEY, charset, urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向服务器发送post请求
     *
     * @param origMap              参数map
     * @param charset              编码字符集
     * @param MERCHANT_PRIVATE_KEY 私钥
     */
    public Object gatewayPosts(Map<String, String> origMap, String charset, String MERCHANT_PRIVATE_KEY) {
        try {
            Map<String, String> sPara = buildRequestPara(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset);
            System.out.println(urlStr + createLinkString(sPara, true));
            Object obj = buildRequests(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset, urlStr);
            System.out.println(obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密，部分接口，有参数需要加密
     *
     * @param src       原值
     * @param publicKey 畅捷支付发送的平台公钥
     * @param charset   UTF-8
     * @return RSA加密后的密文
     */
    private String encrypt(String src, String publicKey, String charset) {
        try {
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes(charset), publicKey);
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公共请求参数设置
     */
    public Map<String, String> setCommonMap(Map<String, String> origMap) {
        // 2.1 基本参数
        Date date = new Date();
        origMap.put("Version", "1.0");
        origMap.put("InputCharset", charset);// 字符集
        origMap.put("TradeDate", getDateFormat("yyyyMMdd").format(date));
        origMap.put("TradeTime", getDateFormat("HHmmss").format(date));
        origMap.put("Memo", null);
        return origMap;
    }

    @PostMapping("/nmg_quick_payment_notify")
    public String nmg_quick_payment_notify(@RequestBody ChangJieEntity changJieEntity) {
        log.info("nmg_quick_payment_notify:" + JSON.toJSONString(changJieEntity));
        return "success";
    }

    @PostMapping("/api/pay/cjreq")
    public Object cjreq(@RequestParam Map<String, String> params) {
        return params;
    }

    /**
     * 2.5 支付确认接口： api nmg_api_quick_payment_smsconfirm
     */

    @RequestMapping("/pay/channel/nmg_api_quick_payment_smsconfirm")
    public String nmg_api_quick_payment_smsconfirm(@RequestParam String jsonParam) {
        JSONObject payOrders = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        Map<String, String> origMap = new HashMap<String, String>();

        String mchId = payOrders.getString("mchId");
        String SmsCode = payOrders.getString("SmsCode");
        String trxId = payOrders.getString("trxId");
        String OriPayTrxId = payOrders.getString("OriPayTrxId");

        // 2.1 基本参数
        origMap = setCommonMap(origMap);
        origMap.put("Service", "nmg_api_quick_payment_smsconfirm");// 请求的接口名称
        // 2.2 业务参数
        origMap.put("TrxId", trxId);// 订单号
        origMap.put("OriPayTrxId", OriPayTrxId);// 原有支付请求订单号
        origMap.put("SmsCode", SmsCode);// 短信验证码

        String merchantId = "";
        if (mchId.equals("10007")) {
            merchantId = "200005280204";
        } else if (mchId.equals("10003")) {
            merchantId = "200005300210";
        } else if (mchId.equals("10017")) {
            merchantId = "200005300210";
        } else {
            merchantId = "200005300210";
        }

        origMap.put("PartnerId", merchantId);//商户号
        String result = fastpayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
        return result;
    }

    public String nmg_api_quick_payment_smsconfirm1() {

        Map<String, String> origMap = new HashMap<String, String>();
        // 2.1 基本参数
        origMap = setCommonMap(origMap);
        origMap.put("Service", "nmg_api_quick_payment_smsconfirm");// 请求的接口名称
        // 2.2 业务参数
        String trxId = Long.toString(System.currentTimeMillis());
        origMap.put("TrxId", trxId);// 订单号
        origMap.put("OriPayTrxId", "G20190806213043000000");// 原有支付请求订单号
        origMap.put("SmsCode", "801251");// 短信验证码
        String result = fastpayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
        return result;
    }


    /**
     * 2.6 支付请求接口(直付通) api nmg_zft_api_quick_payment
     */
    @RequestMapping("/pay/channel/nmg_zft_api_quick_payment")
    public String nmg_zft_api_quick_payment(@RequestParam String jsonParam) {
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        JSONObject payOrders = paramObj.getJSONObject("payOrder");
        Map<String, String> origMap = new HashMap<String, String>();
        String mchId = payOrders.getString("mchId");
        String amount = changeF2Y(payOrders.getString("amount"));
        String mchOrderNo = payOrders.getString("mchOrderNo");
        String subject = payOrders.getString("subject");
        String BkAcctNo = payOrders.getString("BkAcctNo");
        String IDNo = payOrders.getString("IDNo");
        String CstmrNm = payOrders.getString("CstmrNm");
        String MobNo = payOrders.getString("MobNo");
        String merchantId = "";
        if (mchId.equals("10007")) {
            merchantId = "200005280204";
        } else if (mchId.equals("10003")) {
            merchantId = "200005300210";
        } else if (mchId.equals("10017")) {
            merchantId = "200005300210";
        } else {
            merchantId = "200005300210";
        }        // 2.1 基本参数
        origMap = setCommonMap(origMap);
        origMap.put("Service", "nmg_zft_api_quick_payment");// 支付接口名称
        // 2.2 业务参数
        origMap.put("TrxId", mchOrderNo);// 订单号
        origMap.put("OrdrName", "交易");// 商品名称
        origMap.put("MerUserId", merchantId);// 用户标识（测试时需要替换一个新的meruserid）
        origMap.put("SellerId", merchantId);// 子账户号
        origMap.put("SubMerchantNo", merchantId);// 子商户号
        origMap.put("PartnerId", merchantId);//商户号
        origMap.put("ExpiredTime", "40m");// 订单有效期
        origMap.put("BkAcctTp", "01");// 卡类型（00 – 银行贷记卡;01 – 银行借记卡;）
        origMap.put("BkAcctNo", this.encrypt(BkAcctNo, MERCHANT_PUBLIC_KEY, charset));// 卡号
        origMap.put("IDTp", "01");// 证件类型 （目前只支持身份证 01：身份证）
        origMap.put("IDNo", this.encrypt(IDNo, MERCHANT_PUBLIC_KEY, charset));// 证件号
        origMap.put("CstmrNm", this.encrypt(CstmrNm, MERCHANT_PUBLIC_KEY, charset));// 持卡人姓名
        origMap.put("MobNo", this.encrypt(MobNo, MERCHANT_PUBLIC_KEY, charset));// 银行预留手机号
        origMap.put("EnsureAmount", "1");//担保金额
        origMap.put("TrxAmt", amount);// 交易金额
        origMap.put("TradeType", "11");// 交易类型
        origMap.put("SmsFlag", "1");//短信发送标识
        origMap.put("ReturnUrl", "http://106.12.39.167:3000/nmg_quick_payment_notify");//同步回调地址
        origMap.put("NotifyUrl", "http://106.12.39.167:3000/nmg_quick_payment_notify");//异步回调地址
        String result = fastpayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
        return result;
    }


    /**
     * 4.2.1.1. api nmg_ebank_pay 下单支付(网银支付)
     */
    @RequestMapping("/pay/channel/nmg_ebank_pay")
    public String nmg_ebank_pay(@RequestParam String jsonParam) {
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        JSONObject payOrders = paramObj.getJSONObject("payOrder");
        String mchId = payOrders.getString("mchId");
        String amount = changeF2Y(payOrders.getString("amount"));
        String mchOrderNo = payOrders.getString("mchOrderNo");
        String subject = payOrders.getString("subject");
        String bankCode = payOrders.getString("bankCode");
        String merchantId = "";
        if (mchId.equals("10007")) {
            merchantId = "200005280204";
        } else if (mchId.equals("10003")) {
            merchantId = "200005300210";
        } else if (mchId.equals("10017")) {
            merchantId = "200005300210";
        } else {
            merchantId = "200005300210";
        }
        Date date = new Date();
        Map<String, String> origMap = new HashMap<String, String>();
        // 基本参数
        origMap.put("Service", "nmg_ebank_pay");
        origMap.put("Version", "1.0");
        origMap.put("PartnerId", merchantId);
        origMap.put("InputCharset", charset);
        origMap.put("TradeDate", getDateFormat("yyyyMMdd").format(date));
        origMap.put("TradeTime", getDateFormat("HHmmss").format(date));
        // origMap.put("SignType","RSA");
        origMap.put("ReturnUrl", "http://www.baidu.com");// 前台跳转url
        origMap.put("Memo", "备注");

        // 4.2.1.1. 网银支付 api 业务参数
        origMap.put("OutTradeNo", mchOrderNo);
        origMap.put("MchId", merchantId);
        origMap.put("MchName", "商户");
        origMap.put("ChannelType", "02");
        origMap.put("BizType", "01");
        origMap.put("CardFlag", "01");
        origMap.put("PayFlag", "00");
        origMap.put("ServiceType", "01");
        origMap.put("BankCode", bankCode);
        origMap.put("OrderDesc", "商户订单");
        origMap.put("BuyerId", "");
        origMap.put("BuyerName", "");
        origMap.put("BuyerMoblie", "");
        origMap.put("BuyerAddress", "");
        origMap.put("ConsigneeAddress", "");
        origMap.put("BuyerCertType", "");
        origMap.put("BuyerCertId", "");

        origMap.put("TradeType", "00");
        origMap.put("GoodsType", "00");
        origMap.put("GoodsName", "交易");
        origMap.put("GoodsDetail", "交易");
        origMap.put("Currency", "00");
        origMap.put("OrderStartTime", "20170731191900");
        origMap.put("ExpiredTime", "2d");

        origMap.put("OrderAmt", amount);
        origMap.put("EnsureAmt", "");
        origMap.put("NotifyUrl", "http://106.12.39.167:3000/nmg_quick_payment_notify");
        origMap.put("UserIp", "127.0.0.1");
        origMap.put("PreferentialAmt", "");
        origMap.put("SplitList", "");
        origMap.put("Ext", "{'ext':'ext1'}");

        String result = this.gatewayPost(origMap, charset, MERCHANT_PRIVATE_KEY);

        Map<String, Object> requestParam = new HashMap<>();
        JSONArray jsons = JSONArray.parseArray(result);
        for (int i = 0; i < jsons.size(); i++) {
            requestParam.put(jsons.getJSONObject(i).getString("name"), jsons.getJSONObject(i).getString("value"));
        }
        String json = JSONObject.toJSONString(requestParam);

        return json;
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {

        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    private void nmg_api_query_trade() {

        Map<String, String> origMap = new HashMap<String, String>();
        // 2.1 基本参数
        origMap = setCommonMap(origMap);
        origMap.put("Service", "nmg_api_query_trade");// 请求的接口名
        // 2.2 业务参数
        origMap.put("TrxId", "20190309152323322302027");// 订单号
        origMap.put("OrderTrxId", "101156439145128573099");// 原业务请求订单号
        origMap.put("TradeType", "pay_order");// 原业务订单类型
        this.fastpayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
    }

//    public static void main(String[] args) {
//        PayChannelChangJieController test = new PayChannelChangJieController();
//        test.nmg_api_quick_payment_smsconfirm1(); //2.5 支付确认---API
//        //       test.nmg_zft_api_quick_payment(); //2.6 支付请求（直付通）
////        test.nmg_ebank_pay(""); //网银支付
//        //       test.nmg_api_query_trade();
//    }
}