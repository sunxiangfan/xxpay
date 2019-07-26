package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class PayChannel implements ICleanBackgroundInfo, Serializable {

    @Override
    public void cleanBackgroundInfo() {
        this.setParam(null);
        this.setRemark(null);
        this.setThirdDeductionRate(null);
        this.setLabel(null);
    }

    /**
     * Id
     */
    private String id;

    /**
     * 通道显示名称
     */
    private String label;

    /**
     * 支付通道名称
     */
    private String name;

    /**
     * 支付通道Code
     */
    private String code;

    /**
     * 支付方式(如 FAST_PAY,GATEWAY)
     */
    private String payType;

    /**
     * 状态,0-停止使用,1-使用中
     */
    private Byte state;

    /**
     * 是否需要进件,0-不需要,1-需要
     */
    private Byte material;
    /**
     * 到账时间
     */
    private String accountingCycle;

    /**
     * 最大交易金额(单位分)
     */
    private Long maxTransactionAmount;

    /**
     * 最小交易金额(单位分)
     */
    private Long minTransactionAmount;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 三方手续费率
     */
    private Double thirdDeductionRate;

    /**
     * T1比例
     */
    private Double t1Rate;

    /**
     * T1结算时间
     */
    private String t1Time;

    /**
     * 配置参数,json字符串
     */
    private String param;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人用户ID
     */
    private String createBy;

    /**
     * 更新人用户ID
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

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

    public Byte getMaterial() {
        return material;
    }

    public void setMaterial(Byte material) {
        this.material = material;
    }

    public String getAccountingCycle() {
        return accountingCycle;
    }

    public void setAccountingCycle(String accountingCycle) {
        this.accountingCycle = accountingCycle;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getThirdDeductionRate() {
        return thirdDeductionRate;
    }

    public void setThirdDeductionRate(Double thirdDeductionRate) {
        this.thirdDeductionRate = thirdDeductionRate;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    public Double getT1Rate() {
        return t1Rate;
    }

    public void setT1Rate(Double t1Rate) {
        this.t1Rate = t1Rate;
    }

    public String getT1Time() {
        return t1Time;
    }

    public void setT1Time(String t1Time) {
        this.t1Time = t1Time;
    }
}
