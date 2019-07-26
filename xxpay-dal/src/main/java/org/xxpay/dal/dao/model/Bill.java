package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 账单明细对象
 */
public class Bill implements Serializable {

    /**
     * Id
     */
    private String id;
    /**
     * 子商户号
     */
    private String mchId;
    /**
     * 代理商户号
     */
    private String agentMchId;
    /**
     * 支付订单号
     */
    private String payOrderId;
    /**
     * ͨ通道Id
     */
    private String payChannelId;
    /**
     * 支付金额,单位分
     */
    private Long amount;
    /**
     * 平台实际到账金额,单位分
     */
    private Long platformActualAmount;
    /**
     * 子商户实际到账金额,单位分
     */
    private Long mchActualAmount;
    /**
     * 三方手续费率
     */
    private Double thirdDeductionRate;
    /**
     * 三方手续费,单位分
     */
    private Long thirdDeduction;
    /**
     * 代理商户分润率
     */
    private Double agentMchCommissionRate;
    /**
     * 代理商户分润金额,单位分
     */
    private Long agentMchCommission;
    /**
     * 平台手续费率
     */
    private Double platformDeductionRate;
    /**
     * 平台手续费,单位分
     */
    private Long platformDeduction;
    /**
     * 平台分润率
     */
    private Double platformCommissionRate;
    /**
     * 平台分润金额,单位分
     */
    private Long platformCommission;

    /**
     * 结算状态(0-结算中，1-已结算)
     */
    private Byte state;
    /**
     * 交易时间
     */
    private Date payOrderTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAgentMchId() {
        return agentMchId;
    }

    public void setAgentMchId(String agentMchId) {
        this.agentMchId = agentMchId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
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

    public Long getPlatformActualAmount() {
        return platformActualAmount;
    }

    public void setPlatformActualAmount(Long platformActualAmount) {
        this.platformActualAmount = platformActualAmount;
    }

    public Long getMchActualAmount() {
        return mchActualAmount;
    }

    public void setMchActualAmount(Long mchActualAmount) {
        this.mchActualAmount = mchActualAmount;
    }

    public Double getThirdDeductionRate() {
        return thirdDeductionRate;
    }

    public void setThirdDeductionRate(Double thirdDeductionRate) {
        this.thirdDeductionRate = thirdDeductionRate;
    }

    public Long getThirdDeduction() {
        return thirdDeduction;
    }

    public void setThirdDeduction(Long thirdDeduction) {
        this.thirdDeduction = thirdDeduction;
    }

    public Double getAgentMchCommissionRate() {
        return agentMchCommissionRate;
    }

    public void setAgentMchCommissionRate(Double agentMchCommissionRate) {
        this.agentMchCommissionRate = agentMchCommissionRate;
    }

    public Long getAgentMchCommission() {
        return agentMchCommission;
    }

    public void setAgentMchCommission(Long agentMchCommission) {
        this.agentMchCommission = agentMchCommission;
    }

    public Double getPlatformDeductionRate() {
        return platformDeductionRate;
    }

    public void setPlatformDeductionRate(Double platformDeductionRate) {
        this.platformDeductionRate = platformDeductionRate;
    }

    public Long getPlatformDeduction() {
        return platformDeduction;
    }

    public void setPlatformDeduction(Long platformDeduction) {
        this.platformDeduction = platformDeduction;
    }

    public Double getPlatformCommissionRate() {
        return platformCommissionRate;
    }

    public void setPlatformCommissionRate(Double platformCommissionRate) {
        this.platformCommissionRate = platformCommissionRate;
    }

    public Long getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(Long platformCommission) {
        this.platformCommission = platformCommission;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getPayOrderTime() {
        return payOrderTime;
    }

    public void setPayOrderTime(Date payOrderTime) {
        this.payOrderTime = payOrderTime;
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
}
