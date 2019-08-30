package org.xxpay.service.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.dal.dao.model.RepayCommonRequest;
import org.xxpay.dal.dao.model.RepayCommonResponse;
import org.xxpay.service.channel.minfu.MinFuConfig;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @version V1.0
 * @Description: 支付渠道接口:兆行
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannel4ZhaoXingController {

    private final MyLog _log = MyLog.getLog(PayChannel4ZhaoXingController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MinFuConfig minFuConfig;

    @Autowired
    private MchInfoService mchInfoService;


    private static String mchNo = "5d5f3fe54bcf69e1b4dd2600";//商户号
    private static String mchPriKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIdXvmIo0rI6WA2nqddqfqy40r97q7kU616AcJDiY7ZAYqmYzIHfsozq9CX4P242cian3iilB+IR72atE6cHX+wHpC1AVTS7R3q7f+BN8f5p5cpI2ec3DwcSZoORRAbrqc5fA9Fwmo/FjbTGXZ7Lf/glV90MrXtWoora3t9FOWSDAgMBAAECgYA5bvfAqfg3X+Dr8oF1g6C75TaxwWZvBTyYfsLGCPwtZAc6Vzg5yNb5S99NmZo5R+uLEeOJtfaxbdD87mePkuaWIYzLklb5nio2oe3LoJZMj1Nib5/X3NP7sZfmMHzVqNRatJ6gQGnxNVW1u3MDDfZWRcns+NMlnLhWD0XIbyQDEQJBAMe00NhD47FLc9L4JpX+t6qdYfBsNl3x0aYwEk1u/uKsKU8rBIzdSsHMADO7BKAV1fYJ+sBxfpSz66PovSiAQDkCQQCtflTuSi91OWOqa4/xWnHNabtbH3sQ/WQ01HnJBNmW/v40CbByISpmhuc8UCheV5oKtA/DdymFy+AH17HugpKbAkBwj8bjDqjoxnl7IOlGw/Ir1VddlvCx1NnxDMlUIcrCYn1KJRoEd7pqLxyyC6hYvtYBPrC6BX5uPFcF5OmxSVBpAkAddMCIwLG0Dm1chuymhruhq5zyyLKFXPBBg/21Yoxq9ZpFTYvWpMjlJWxnMhjYg7kUriOrBTVYkRfVXQrJuwOXAkBCunuBs6U3ipvtr22LcdBkG3dTCkC+nSgsRRArvVhVm4cskKWObaLcfeB/gbBqKGew0Ose8ybF6DxEzRwS+m+I";//商户私钥
    private static String systemPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCM6B277H+EiyWbPN8Uog8XSRv9qCvjImmCEn5BRcmhjqYylnyVfav2cZyTm7FDi4FDkEZx1Y2tHSR64eATbpu6o8WpZZPFbopaqD7Kr2yDyeMW1V/hK92jrx6m4jH8ur+v2/n1CMfVIx6AVdI/E6e+rlvt3D2LLPAUBBNwsYd1CQIDAQAB";//壹帐通公钥
    private static String mchPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHV75iKNKyOlgNp6nXan6suNK/e6u5FOtegHCQ4mO2QGKpmMyB37KM6vQl+D9uNnImp94opQfiEe9mrROnB1/sB6QtQFU0u0d6u3/gTfH+aeXKSNnnNw8HEmaDkUQG66nOXwPRcJqPxY20xl2ey3/4JVfdDK17VqKK2t7fRTlkgwIDAQAB";//商户公钥
    private static final int AES_KEY_SIZE = 128;
    private static final String ALGORITHM = "AES";
    private static final String domain = "http://inf.lanjon.cn/api/";
    public static final String REPAY_URL = domain + "repay";
    public static final String REPAY_QUERY_URL = domain + "repay/query";

    public static RepayCommonRequest encrypt(String aesKey, Object o) throws Exception {
        System.out.println("aesKey=" + aesKey);
        String requestData = JSONObject.toJSONString(o);
        System.out.println("加密前参数为:" + requestData);
        AES aes = new AES(Base64.decode(aesKey));
        RSA rsa = new RSA(mchPriKey, systemPubKey);
        String encryptKey = rsa.encryptBase64(aesKey, KeyType.PublicKey);
        String encryptData = aes.encryptBase64(requestData);
        //System.out.println(aes.decryptStrFromBase64(encryptData));
        String sign = Base64.encode(SecureUtil.sign(SignAlgorithm.SHA1withRSA, mchPriKey, null).sign(SecureUtil.md5(requestData).getBytes()));
        //System.out.println(sign);
        boolean b = SecureUtil.sign(SignAlgorithm.SHA1withRSA, null, mchPubKey).verify(SecureUtil.md5(requestData).getBytes(), Base64.decode(sign));
        System.out.print(b);
        RepayCommonRequest repayCommonRequest = new RepayCommonRequest();
        repayCommonRequest.setMchNo(mchNo);
        repayCommonRequest.setRequestTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        repayCommonRequest.setRequestData(encryptData);
        repayCommonRequest.setEncryptKey(encryptKey);
        repayCommonRequest.setSign(sign);

        return repayCommonRequest;
    }

    public RepayCommonResponse doRequest(Object o, String url) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();
        String aesKey = Base64.encode(secretKey.getEncoded());
        System.out.println("请求地址为:" + url);
        String body = JSONObject.toJSONString(encrypt(aesKey, o));
        System.out.println(body);
        String request = HttpRequest.post(url).charset("UTF-8")
                .header("Content-Type", "application/json")
                .body(body)
                .execute().body();
        System.out.println(request);
        RepayCommonResponse commonResponse = JSONObject.parseObject(request, RepayCommonResponse.class);
        return commonResponse;
    }


    /**
     * 兆行企业转账接口（代付）
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/pay/zx/transfer")
    public String doMinFuTransferReq(@RequestBody Map<String, String> map) {
        _log.info("jsonParam:" + JSON.toJSONString(map));
        try {
            map.put("bankCardMobile", map.get("bankCardMobile"));
            map.put("bankCardNo", map.get("bankCardNo"));
            map.put("bankCardOwnerName", map.get("bankCardOwnerName"));
            map.put("bankName", map.get("bankName"));
            map.put("idCardNo", map.get("idCardNo"));
            map.put("mchRequestNo", map.get("mchRequestNo"));
            map.put("transferAmt", map.get("transferAmt"));
            map.put("callbackUrl", map.get("callbackUrl"));
            RepayCommonResponse result = doRequest(map, REPAY_URL);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            _log.error(e, "企业转账异常");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }
    }
}
