package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.util.MyLog;
import org.xxpay.service.channel.changjie.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.*;

/**
 * @version V1.0
 * @Description: 支付渠道接口:changjie
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannelChangJieController {

    private final MyLog _log = MyLog.getLog(PayChannelChangJieController.class);


    /**
     * 畅捷支付平台公钥
     */
    private static String MERCHANT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPq3oXX5aFeBQGf3Ag/86zNu0VICXmkof85r+DDL46w3vHcTnkEWVbp9DaDurcF7DMctzJngO0u9OG1cb4mn+Pn/uNC1fp7S4JH4xtwST6jFgHtXcTG9uewWFYWKw/8b3zf4fXyRuI/2ekeLSstftqnMQdenVP7XCxMuEnnmM1RwIDAQAB";//生产环境公钥

    /**
     * 商户号私钥
     */
    private static String MERCHANT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANB5cQ5pf+QHF9Z2+DjrAXstdxQHJDHyrni1PHijKVn5VHy/+ONiEUwSd5nx1d/W+mtYKxyc6HiN+5lgWSB5DFimyYCiOInh3tGQtN+pN/AtE0dhMh4J9NXad0XEetLPRgmZ795O/sZZTnA3yo54NBquT19ijYfrvi0JVf3BY9glAgMBAAECgYBFdSCox5eXlpFnn+2lsQ6mRoiVAKgbiBp/FwsVum7NjleK1L8MqyDOMpzsinlSgaKfXxnGB7UgbVW1TTeErS/iQ06zx3r4CNMDeIG1lYwiUUuguIDMedIJxzSNXfk65Bhps37lm129AE/VnIecpKxzelaUuzyGEoFWYGevwc/lQQJBAPO0mGUxOR/0eDzqsf7ehE+Iq9tEr+aztPVacrLsEBAwqOjUEYABvEasJiBVj4tECnbgGxXeZAwyQAJ5YmgseLUCQQDa/dgviW/4UMrY+cQnzXVSZewISKg/bv+nW1rsbnk+NNwdVBxR09j7ifxg9DnQNk1Edardpu3z7ipHDTC+z7exAkAM5llOue1JKLqYlt+3GvYr85MNNzSMZKTGe/QoTmCHStwV/uuyN+VMZF5cRcskVwSqyDAG10+6aYqD1wMDep8lAkBQBoVS0cmOF5AY/CTXWrht1PsNB+gbzic0dCjkz3YU6mIpgYwbxuu69/C3SWg7EyznQIyhFRhNlJH0hvhyMhvxAkEAuf7DNrgmOJjRPcmAXfkbaZUf+F4iK+szpggOZ9XvKAhJ+JGd+3894Y/05uYYRhECmSlPv55CBAPwd8VUsSb/1w==";


    private static String urlStr = "https://pay.chanpay.com/mag-unify/gateway/receiveOrder.do";
    /**
     * 编码类型
     */
    private static String charset = "UTF-8";

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
        HttpResponse response = httpProtocolHandler.execute(request, null, null);
        if (response == null) {
            return null;
        }
        String strResult = response.getStringResult();

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
     * 向测试服务器发送post请求
     *
     * @param origMap              参数map
     * @param charset              编码字符集
     * @param MERCHANT_PRIVATE_KEY 私钥
     * @throws Exception
     */
    public void gatewayPost(Map<String, String> origMap, String charset, String MERCHANT_PRIVATE_KEY) {
        String result = "";
        try {
            Map<String, String> sPara = buildRequestPara(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset);
            result = buildRequest(origMap, "RSA", PayChannelChangJieController.MERCHANT_PRIVATE_KEY, charset, urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);


    }

    /**
     * 向测试服务器发送post请求
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
        origMap.put("Version", "1.0");
        origMap.put("PartnerId", "200005300210");//商户号
        origMap.put("InputCharset", charset);// 字符集
        origMap.put("TradeDate", "20190612");// 商户请求时间
        origMap.put("TradeTime", "141200");// 商户请求时间
        origMap.put("Memo", null);
        return origMap;
    }


    @PostMapping("/nmg_quick_payment_notify")
    private String nmg_quick_payment_notify(@RequestBody ChangJieEntity changJieEntity) {
        System.out.println(JSON.toJSONString(changJieEntity));
        return "success";
    }

    /**
     * 2.5 支付确认接口： api nmg_api_quick_payment_smsconfirm
     */

    private void nmg_api_quick_payment_smsconfirm() {

        Map<String, String> origMap = new HashMap<String, String>();
        // 2.1 基本参数
        origMap = setCommonMap(origMap);
        origMap.put("Service", "nmg_api_quick_payment_smsconfirm");// 请求的接口名称
        // 2.2 业务参数
        String trxId = Long.toString(System.currentTimeMillis());
        origMap.put("TrxId", trxId);// 订单号
        origMap.put("OriPayTrxId", "101156414100736177980");// 原有支付请求订单号
        origMap.put("SmsCode", "760745");// 短信验证码
        this.gatewayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
    }

    /**
     * 2.6 支付请求接口(直付通) api nmg_zft_api_quick_payment
     */

    private void nmg_zft_api_quick_payment() {

        Map<String, String> origMap = new HashMap<String, String>();
        // 2.1 基本参数
        origMap = setCommonMap(origMap);
        origMap.put("Service", "nmg_zft_api_quick_payment");// 支付接口名称
        // 2.2 业务参数
        origMap.put("TrxId", "2019031310452343143512");// 订单号
        origMap.put("OrdrName", "畅捷支付");// 商品名称
        origMap.put("MerUserId", "101010123012011");// 用户标识（测试时需要替换一个新的meruserid）
        origMap.put("SellerId", "200005300210");// 子账户号
        origMap.put("SubMerchantNo", "200005300210");// 子商户号
        origMap.put("ExpiredTime", "40m");// 订单有效期
        origMap.put("BkAcctTp", "01");// 卡类型（00 – 银行贷记卡;01 – 银行借记卡;）
        origMap.put("BkAcctNo", this.encrypt("6222023500015959782", MERCHANT_PUBLIC_KEY, charset));// 卡号
        origMap.put("IDTp", "01");// 证件类型 （目前只支持身份证 01：身份证）
        origMap.put("IDNo", this.encrypt("231222199110194015", MERCHANT_PUBLIC_KEY, charset));// 证件号
        origMap.put("CstmrNm", this.encrypt("孙祥帆", MERCHANT_PUBLIC_KEY, charset));// 持卡人姓名
        origMap.put("MobNo", this.encrypt("13111110495", MERCHANT_PUBLIC_KEY, charset));// 银行预留手机号
        origMap.put("EnsureAmount", "1");//担保金额
        origMap.put("TrxAmt", "1");// 交易金额
        origMap.put("TradeType", "11");// 交易类型
        origMap.put("SmsFlag", "1");//短信发送标识
        origMap.put("ReturnUrl", "http://106.12.39.167:3000/nmg_quick_payment_notify");//同步回调地址
        origMap.put("NotifyUrl", "http://106.12.39.167:3000/nmg_quick_payment_notify");//异步回调地址


        this.gatewayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
    }

    public static void main(String[] args) {
        PayChannelChangJieController test = new PayChannelChangJieController();
//        test.nmg_biz_api_auth_req();
//        test.nmg_biz_api_quick_payment();


//		test.nmg_biz_api_auth_req(); // 2.1 鉴权请求---API
//		test.nmg_page_api_auth_req(); //2.2 鉴权请求 ---畅捷前端
//		test.nmg_api_auth_sms(); // 2.3 鉴权请求确认---API
//		test.nmg_biz_api_quick_payment(); //2.4 支付请求---API
        test.nmg_api_quick_payment_smsconfirm(); //2.5 支付确认---API
        // 		test.nmg_zft_api_quick_payment(); //2.6 支付请求（直付通）
//		test.nmg_quick_onekeypay();  //2.7 直接请求---畅捷前端
//		test.nmg_nquick_onekeypay();  //2.8 支付请求---畅捷前端
//		test.nmg_api_auth_info_qry(); // 2.9 鉴权绑卡查询
//		test.nmg_api_auth_unbind(); // 鉴权解绑（普通）
//		test.nmg_api_refund();//商户退款请求
//

//		test.nmg_sms_resend(); //2.11 短信重发
//		test.nmg_api_query_trade(); //2.14 订单状态查询
//		test.nmg_api_refund_trade_file(); //2.12 退款对账单
//		test.nmg_api_everyday_trade_file(); //2.15 交易对账单
//		test.nmg_api_quick_payment_receiptconfirm();// 2.13 确认收货接口
//		test.notifyVerify(); // 测试异步通知验签
    }


}
