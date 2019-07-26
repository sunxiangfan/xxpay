package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 平台备付金对象
 */
public class ReserveAmount implements Serializable {

    /**
     * Id
     */
    private String id;

    /**
     * 收支类型(0-收入，1-支出)
     */
    private Byte flowType;

    /**
     * 金额,单位分
     */
    private Long amount;

    /**
     * 操作前账户余额,单位分
     */
    private Long balance;

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

    public Byte getFlowType() {
        return flowType;
    }

    public void setFlowType(Byte flowType) {
        this.flowType = flowType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
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
