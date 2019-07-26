package org.xxpay.service.channel.minfu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: xxpay-master
 * @Package: org.xxpay.service.channel.minfu
 * @ClassName: MinFuConfig
 * @Description:
 * @CreateDate: 2019/6/17 18:41
 * @UpdateUser: 更新者
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RefreshScope
@Configuration
public class MinFuConfig {
    //网银支付
    private String url = "https://pay.minfupay.cn/gateway?input_charset=UTF-8";
    //快捷支付url
    private String mfExpressUrl="https://api.minfupay.cn/gateway/api/express";
    //转账url
    private String mfTransferUrl="https://transfer.minfupay.cn/transfer";

    public String getMfTransferUrl() {
        return mfTransferUrl;
    }

    public void setMfTransferUrl(String mfTransferUrl) {
        this.mfTransferUrl = mfTransferUrl;
    }


    public String getMfExpressUrl() {
        return mfExpressUrl;
    }

    public void setMfExpressUrl(String mfExpressUrl) {
        this.mfExpressUrl = mfExpressUrl;
    }

    // 商户appid
    private String appId;
    private String mchId;
    private String mchKey;//私钥

    public String getMfPubKey() {
        return mfPubKey;
    }

    public void setMfPubKey(String mfPubKey) {
        this.mfPubKey = mfPubKey;
    }

    private String mfPubKey;//敏付公钥

    @Value("${ali.notify_url}")
    private String minFuNotifyUrl="http://www.gzxinwei123.com:3000/pay/minFuPayNotifyRes.htm";

    public String getMinFuNotifyUrl() {
        return this.minFuNotifyUrl;
    }

    public void setMinFuNotifyUrl(String minFuNotifyUrl) {
        this.minFuNotifyUrl = minFuNotifyUrl;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 请求网关地址

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    /**
     * 获取敏付（网银支付）支付配置
     * @param configParam
     * @return
     */
    public static MinFuConfig getMinFuPayConfig(String configParam) {
        MinFuConfig minFuConfig = new MinFuConfig();
        JSONObject paramObj = JSON.parseObject(configParam);
        minFuConfig.setMchId(paramObj.getString("mchId"));
        //minFuConfig.setAppId(paramObj.getString("appId"));
        minFuConfig.setMchKey(paramObj.getString("priKey"));
        minFuConfig.setMfPubKey(paramObj.getString("mfPubKey"));
        return minFuConfig;
    }
}
