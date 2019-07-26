package org.xxpay.web.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xxpay.common.util.MyBase64;

/**
 * @Description:
 * @author tanghaibo
 * @date 2019-02-27
 * @version V1.0
 */
@Service
public class AgentMchPayChannelServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "selectAgentMchPayChannelFallback")
    public String selectAgentMchPayChannel(String jsonParam) {
        return restTemplate.getForEntity("http://xxpay-service/agent_mch_pay_channel/select?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String selectAgentMchPayChannelFallback(String jsonParam) {
        return "error";
    }

}