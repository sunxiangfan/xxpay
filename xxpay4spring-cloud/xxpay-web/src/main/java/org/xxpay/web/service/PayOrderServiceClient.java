package org.xxpay.web.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;
import org.xxpay.common.util.MyBase64;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Service
public class PayOrderServiceClient {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 创建支付订单
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "createPayOrderFallback")
    public String createPayOrder(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/create?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String createPayOrderFallback(String jsonParam) {
        return "error";
    }

    /**
     * 查询支付订单
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "queryPayOrderFallback")
    public String queryPayOrder(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/query?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String queryPayOrderFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理微信支付
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doWxPayReqFallback")
    public String doWxPayReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/wx?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doWxPayReqFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理支付宝wap支付
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doAliPayWapReqFallback")
    public String doAliPayWapReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/ali_wap?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doAliPayWapReqFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理支付宝即时到账支付
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doAliPayPcReqFallback")
    public String doAliPayPcReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/ali_pc?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doAliPayPcReqFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理支付宝手机支付
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doAliPayMobileReqFallback")
    public String doAliPayMobileReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/ali_mobile?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doAliPayMobileReqFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理支付宝当面付扫码支付
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doAliPayQrReqFallback")
    public String doAliPayQrReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/ali_qr?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doAliPayQrReqFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理Tengjing线下转账当面付扫码支付
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doTengjingTransferReqReqFallback")
    public String doTengjingTransferReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/tingjing/transfer?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doTengjingTransferReqReqFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理Blzqzbs
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doBlzqzbsReqFallback")
    public String doBlzqzbsReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/ali_qr?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doBlzqzbsReqFallback(String jsonParam) {
        return "error";
    }


    /**
     * 处理doXq316Req
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doXq316ReqFallback")
    public String doXq316Req(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/xq316?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doXq316ReqFallback(String jsonParam) {
        return "error";
    }


    /**
     * 处理three10000
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doThree10000ReqFallback")
    public String doThree10000Req(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/three10000?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doThree10000ReqFallback(String jsonParam) {
        return "error";
    }


    /**
     * 处理MIFU(敏付)
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doMinFuPayFallback")
    public String doMinFuPayReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/channel/minfupay?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }
    public String doMinFuPayFallback(String jsonParam) {
        return "error";
    }

    /**
     * 处理MIFU(敏付快捷支付获取短信)
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doMinFuMessagePayFallback")
    public String doMinFuMessagePayReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/kj/message?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doMinFuMessagePayFallback(String jsonParam) {
        return "error";
    }
     /**
        * 处理MIFU(敏付快捷支付确认)
     * @param jsonParam
     * @return
             */
    @HystrixCommand(fallbackMethod = "doMinFuMessageVerifyPayFallback")
    public String doMinFuMessageVerifyPayReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/kj/verify?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doMinFuMessageVerifyPayFallback(String jsonParam) {
        return "error";
    }
    /**
     * 处理MIFU(敏付快捷支付确认)
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "doMinFuTransferPayReqFallback")
    public String doMinFuTransferPayReq(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay/mf/transfer?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String doMinFuTransferPayReqFallback(String jsonParam) {
        return "error";
    }


}