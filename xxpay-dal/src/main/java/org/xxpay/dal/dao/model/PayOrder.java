package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;

public class PayOrder implements Serializable {
    /**
     * 支付订单号
     *
     * @mbggenerated
     */
    private String id;


    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    private String provinceCode; //省市编码

    /**
     * 代理商户号
     */
    private String agentMchId;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    private String cardNo;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    private String bankName;//真实姓名

    /**
     * 子商户号
     *
     * @mbggenerated
     */
    private String mchId;



    private String bankCity;

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    /**
     * 商户订单号
     *
     * @mbggenerated
     */
    private String mchOrderNo;

    /**
     * ͨ代理商户支付通道关系表Id
     */
    private String agentMchPayChannelId;

    /**
     * 子商户支付通道关系表Id
     */
    private String mchPayChannelId;

    /**
     * 支付渠道id
     */
    private String payChannelId;

    /**
     * 订单金额,单位分
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     * 实际支付金额,单位分
     *
     * @mbggenerated
     */
    private Long payAmount;

    /**
     * 三位货币代码,人民币:cny
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * 平台实际到账金额,单位分
     */
    private Long platformActualAmount;

    /**
     * 子商户实际到账金额,单位分
     */
    private Long subMchActualAmount;

    /**
     * 平台手续费率
     */
    private Double platformDeductionRate;

    /**
     * 代理商户分润率
     */
    private Double agentMchCommissionRate;

    /**
     * 三方手续费率
     */
    private Double thirdDeductionRate;

    /**
     * 支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 客户端IP
     *
     * @mbggenerated
     */
    private String clientIp;

    /**
     * 设备
     *
     * @mbggenerated
     */
    private String device;

    /**
     * 商品标题
     *
     * @mbggenerated
     */
    private String subject;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    private String bankCode; //银行代码

    /**
     * 商品描述信息
     *
     * @mbggenerated
     */
    private String body;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    private String channelOrderNo;

    /**
     * 渠道支付错误码
     *
     * @mbggenerated
     */
    private String errCode;

    /**
     * 渠道支付错误描述
     *
     * @mbggenerated
     */
    private String errMsg;

    /**
     * 扩展参数1
     *
     * @mbggenerated
     */
    private String param1;

    /**
     * 扩展参数2
     *
     * @mbggenerated
     */
    private String param2;

    /**
     * 支付完成回跳地址
     *
     * @mbggenerated
     */
    private String frontUrl;

    /**
     * 通知地址
     *
     * @mbggenerated
     */
    private String notifyUrl;

    /**
     * 通知次数
     *
     * @mbggenerated
     */
    private Byte notifyCount;

    /**
     * 最后一次通知时间
     *
     * @mbggenerated
     */
    private Long lastNotifyTime;

    /**
     * 订单失效时间
     *
     * @mbggenerated
     */
    private Long expireTime;

    /**
     * 订单支付成功时间
     *
     * @mbggenerated
     */
    private Long paySuccTime;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgentMchId() {
        return agentMchId;
    }

    public void setAgentMchId(String agentMchId) {
        this.agentMchId = agentMchId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getAgentMchPayChannelId() {
        return agentMchPayChannelId;
    }

    public void setAgentMchPayChannelId(String agentMchPayChannelId) {
        this.agentMchPayChannelId = agentMchPayChannelId;
    }

    public String getMchPayChannelId() {
        return mchPayChannelId;
    }

    public void setMchPayChannelId(String mchPayChannelId) {
        this.mchPayChannelId = mchPayChannelId;
    }

    public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getPlatformActualAmount() {
        return platformActualAmount;
    }

    public void setPlatformActualAmount(Long platformActualAmount) {
        this.platformActualAmount = platformActualAmount;
    }

    public Long getSubMchActualAmount() {
        return subMchActualAmount;
    }

    public void setSubMchActualAmount(Long subMchActualAmount) {
        this.subMchActualAmount = subMchActualAmount;
    }

    public Double getPlatformDeductionRate() {
        return platformDeductionRate;
    }

    public void setPlatformDeductionRate(Double platformDeductionRate) {
        this.platformDeductionRate = platformDeductionRate;
    }

    public Double getAgentMchCommissionRate() {
        return agentMchCommissionRate;
    }

    public void setAgentMchCommissionRate(Double agentMchCommissionRate) {
        this.agentMchCommissionRate = agentMchCommissionRate;
    }

    public Double getThirdDeductionRate() {
        return thirdDeductionRate;
    }

    public void setThirdDeductionRate(Double thirdDeductionRate) {
        this.thirdDeductionRate = thirdDeductionRate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Byte getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Byte notifyCount) {
        this.notifyCount = notifyCount;
    }

    public Long getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(Long lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(Long paySuccTime) {
        this.paySuccTime = paySuccTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mchId=").append(mchId);
        sb.append(", agentMchId=").append(agentMchId);
        sb.append(", mchOrderNo=").append(mchOrderNo);
        sb.append(", mchPayChannelId=").append(mchPayChannelId);
        sb.append(", agentMchPayChannelId=").append(agentMchPayChannelId);
        sb.append(", payChannelId=").append(payChannelId);
        sb.append(", amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append(", platformActualAmount=").append(platformActualAmount);
        sb.append(", subMchActualAmount=").append(subMchActualAmount);
        sb.append(", platformDeductionRate=").append(platformDeductionRate);
        sb.append(", agentMchCommissionRate=").append(agentMchCommissionRate);
        sb.append(", thirdDeductionRate=").append(thirdDeductionRate);
        sb.append(", status=").append(status);
        sb.append(", status=").append(status);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", device=").append(device);
        sb.append(", subject=").append(subject);
        sb.append(", body=").append(body);
        sb.append(", channelOrderNo=").append(channelOrderNo);
        sb.append(", errCode=").append(errCode);
        sb.append(", errMsg=").append(errMsg);
        sb.append(", param1=").append(param1);
        sb.append(", param2=").append(param2);
        sb.append(", frontUrl=").append(frontUrl);
        sb.append(", notifyUrl=").append(notifyUrl);
        sb.append(", notifyCount=").append(notifyCount);
        sb.append(", lastNotifyTime=").append(lastNotifyTime);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", paySuccTime=").append(paySuccTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PayOrder other = (PayOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAgentMchId() == null ? other.getAgentMchId() == null : this.getAgentMchId().equals(other.getAgentMchId()))
                && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
                && (this.getMchOrderNo() == null ? other.getMchOrderNo() == null : this.getMchOrderNo().equals(other.getMchOrderNo()))
                && (this.getMchPayChannelId() == null ? other.getMchPayChannelId() == null : this.getMchPayChannelId().equals(other.getMchPayChannelId()))
                && (this.getAgentMchPayChannelId() == null ? other.getAgentMchPayChannelId() == null : this.getAgentMchPayChannelId().equals(other.getAgentMchPayChannelId()))
                && (this.getPayChannelId() == null ? other.getPayChannelId() == null : this.getPayChannelId().equals(other.getPayChannelId()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
                && (this.getPlatformActualAmount() == null ? other.getPlatformActualAmount() == null : this.getPlatformActualAmount().equals(other.getPlatformActualAmount()))
                && (this.getSubMchActualAmount() == null ? other.getSubMchActualAmount() == null : this.getSubMchActualAmount().equals(other.getSubMchActualAmount()))
                && (this.getPlatformDeductionRate() == null ? other.getPlatformDeductionRate() == null : this.getPlatformDeductionRate().equals(other.getPlatformDeductionRate()))
                && (this.getAgentMchCommissionRate() == null ? other.getAgentMchCommissionRate() == null : this.getAgentMchCommissionRate().equals(other.getAgentMchCommissionRate()))
                && (this.getThirdDeductionRate() == null ? other.getThirdDeductionRate() == null : this.getThirdDeductionRate().equals(other.getThirdDeductionRate()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
                && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
                && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
                && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
                && (this.getChannelOrderNo() == null ? other.getChannelOrderNo() == null : this.getChannelOrderNo().equals(other.getChannelOrderNo()))
                && (this.getErrCode() == null ? other.getErrCode() == null : this.getErrCode().equals(other.getErrCode()))
                && (this.getErrMsg() == null ? other.getErrMsg() == null : this.getErrMsg().equals(other.getErrMsg()))
                && (this.getParam1() == null ? other.getParam1() == null : this.getParam1().equals(other.getParam1()))
                && (this.getParam2() == null ? other.getParam2() == null : this.getParam2().equals(other.getParam2()))
                && (this.getFrontUrl() == null ? other.getFrontUrl() == null : this.getFrontUrl().equals(other.getFrontUrl()))
                && (this.getNotifyUrl() == null ? other.getNotifyUrl() == null : this.getNotifyUrl().equals(other.getNotifyUrl()))
                && (this.getNotifyCount() == null ? other.getNotifyCount() == null : this.getNotifyCount().equals(other.getNotifyCount()))
                && (this.getLastNotifyTime() == null ? other.getLastNotifyTime() == null : this.getLastNotifyTime().equals(other.getLastNotifyTime()))
                && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
                && (this.getPaySuccTime() == null ? other.getPaySuccTime() == null : this.getPaySuccTime().equals(other.getPaySuccTime()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAgentMchId() == null) ? 0 : getAgentMchId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getMchOrderNo() == null) ? 0 : getMchOrderNo().hashCode());
        result = prime * result + ((getMchPayChannelId() == null) ? 0 : getMchPayChannelId().hashCode());
        result = prime * result + ((getAgentMchPayChannelId() == null) ? 0 : getAgentMchPayChannelId().hashCode());
        result = prime * result + ((getPayChannelId() == null) ? 0 : getPayChannelId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getPlatformActualAmount() == null) ? 0 : getPlatformActualAmount().hashCode());
        result = prime * result + ((getSubMchActualAmount() == null) ? 0 : getSubMchActualAmount().hashCode());
        result = prime * result + ((getPlatformDeductionRate() == null) ? 0 : getPlatformDeductionRate().hashCode());
        result = prime * result + ((getAgentMchCommissionRate() == null) ? 0 : getAgentMchCommissionRate().hashCode());
        result = prime * result + ((getThirdDeductionRate() == null) ? 0 : getThirdDeductionRate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getChannelOrderNo() == null) ? 0 : getChannelOrderNo().hashCode());
        result = prime * result + ((getErrCode() == null) ? 0 : getErrCode().hashCode());
        result = prime * result + ((getErrMsg() == null) ? 0 : getErrMsg().hashCode());
        result = prime * result + ((getParam1() == null) ? 0 : getParam1().hashCode());
        result = prime * result + ((getParam2() == null) ? 0 : getParam2().hashCode());
        result = prime * result + ((getFrontUrl() == null) ? 0 : getFrontUrl().hashCode());
        result = prime * result + ((getNotifyUrl() == null) ? 0 : getNotifyUrl().hashCode());
        result = prime * result + ((getNotifyCount() == null) ? 0 : getNotifyCount().hashCode());
        result = prime * result + ((getLastNotifyTime() == null) ? 0 : getLastNotifyTime().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        result = prime * result + ((getPaySuccTime() == null) ? 0 : getPaySuccTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}