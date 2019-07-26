package org.xxpay.service.channel.tengjing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xxpay.common.util.RSA;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
@RefreshScope
public class TengjingProperties {

    @Value("${tengjing.notifyUrl}")
    private String notifyUrl;
    @Value("${tengjing.platPublicKeyText}")
    private String platPublicKeyText;
    @Value("${tengjing.merchPrivateKeyText}")
    private String merchPrivateKeyText;


    private String mchId="";

    private String transferBizCode="";

    private String transferPayUrl="";


    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPlatPublicKeyText() {
        return platPublicKeyText;
    }

    public void setPlatPublicKeyText(String platPublicKeyText) {
        this.platPublicKeyText = platPublicKeyText;
    }

    public String getMerchPrivateKeyText() {
        return merchPrivateKeyText;
    }

    public void setMerchPrivateKeyText(String merchPrivateKeyText) {
        this.merchPrivateKeyText = merchPrivateKeyText;
    }

    private PublicKey platPublicKey;

    public PublicKey getPlatPublicKey() {
        if (null == platPublicKey) {
            platPublicKey = RSA.toPublicKey(getPlatPublicKeyText());
        }
        return platPublicKey;
    }

    private PrivateKey mchPrivateKey;

    public PrivateKey getMchPrivateKey() {
        if (null == mchPrivateKey) {
            mchPrivateKey = RSA.toPrivateKey(getMerchPrivateKeyText());
        }
        return mchPrivateKey;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getTransferBizCode() {
        return transferBizCode;
    }

    public void setTransferBizCode(String transferBizCode) {
        this.transferBizCode = transferBizCode;
    }

    public String getTransferPayUrl() {
        return transferPayUrl;
    }

    public void setTransferPayUrl(String transferPayUrl) {
        this.transferPayUrl = transferPayUrl;
    }
}
