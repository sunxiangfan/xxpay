package org.xxpay.service.channel.blzqzbs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;


@RefreshScope
@Service
public class BlzqzbsConfig {
    @Value("${blzqzbs.mchId}")
    private String mchId;

    @Value("${blzqzbs.key}")
    private String key;

    @Value("${blzqzbs.notifyUrl}")
    private  String notifyUrl;
    
    
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
