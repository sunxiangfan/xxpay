package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 保证金对象
 */
public class DepositAmount implements Serializable {

    /**
     * Id
     */
    private String id;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户类型,0-代理,1-子商户
     */
    private  Byte mchType;

    /**
     * 关联单号
     */
    private String payOrderId;

    /**
     * 收支类型(0-收入，1-支出)
     */
    private Byte state;

    /**
     * 金额,单位分
     */
    private Long amount;

    /**
     * 计划解冻时间
     */
    private  Date planUnlockTime;

    /**
     * 解冻时间
     */
    private Date unlockTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

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

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getPlanUnlockTime() {
        return planUnlockTime;
    }

    public void setPlanUnlockTime(Date planUnlockTime) {
        this.planUnlockTime = planUnlockTime;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
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
}
