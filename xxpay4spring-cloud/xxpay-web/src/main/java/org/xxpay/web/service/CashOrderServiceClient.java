package org.xxpay.web.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xxpay.common.util.MyBase64;

/**
 * @Description:
 * @author tanghaibo
 * @date 2019-04-01
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Service
public class CashOrderServiceClient {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 创建提现订单
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "createCashOrderFallback")
    public String createCashOrder(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/cash/create?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String createCashOrderFallback(String jsonParam) {
        return "error";
    }

    /**
     * 查询支付订单
     * @param jsonParam
     * @return
     */
    @HystrixCommand(fallbackMethod = "queryCashOrderFallback")
    public String queryCashOrder(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/cash/query?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String queryCashOrderFallback(String jsonParam) {
        return "error";
    }

}