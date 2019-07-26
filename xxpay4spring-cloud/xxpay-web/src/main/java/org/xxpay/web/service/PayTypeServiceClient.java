package org.xxpay.web.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xxpay.common.util.MyBase64;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Service
public class PayTypeServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "selectPayTypeFallback")
    public String selectPayType(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/pay_type/select?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String selectPayTypeFallback(String jsonParam) {
        return "error";
    }

}