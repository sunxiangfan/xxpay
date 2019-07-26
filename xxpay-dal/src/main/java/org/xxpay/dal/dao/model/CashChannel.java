package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class CashChannel implements ICleanBackgroundInfo, Serializable {

    @Override
    public void cleanBackgroundInfo() {
        this.setParam(null);
    }

    /**
     * Id
     */
    private String id;

    /**
     * 代付通道名称
     */
    private String name;

    /**
     * 通道显示名称
     */
    private String label;
    /**
     * 支付通道Code
     */
    private String code;

    /**
     * 支付方式(如 CASH)
     */
    private String payType;

    /**
     * 状态,0-停止使用,1-使用中
     */
    private Byte state;

    /**
     * 最大交易金额(单位分)
     */
    private Long maxTransactionAmount;

    /**
     * 最小交易金额(单位分)
     */
    private Long minTransactionAmount;

    /**
     * 三方手续费(单位分)
     */
    private Long thirdDeduction;

    /**
     * 配置参数,json字符串
     */
    private String param;

    /**
     * 备注
     */
    private String remark;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Long getMaxTransactionAmount() {
        return maxTransactionAmount;
    }

    public void setMaxTransactionAmount(Long maxTransactionAmount) {
        this.maxTransactionAmount = maxTransactionAmount;
    }

    public Long getMinTransactionAmount() {
        return minTransactionAmount;
    }

    public void setMinTransactionAmount(Long minTransactionAmount) {
        this.minTransactionAmount = minTransactionAmount;
    }

    public Long getThirdDeduction() {
        return thirdDeduction;
    }

    public void setThirdDeduction(Long thirdDeduction) {
        this.thirdDeduction = thirdDeduction;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
