package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 账户金额变动对象
 */
public class AmountFlow implements Serializable {

    /**
     * Id
     */
    private String id;
    /**
     * 商户号
     */
    private String mchId;

    /**
     * 关联单号
     */
    private String sourceId;

    /**
     * 收支类型(0-收入，1-支出)
     */
    private Byte flowType;

    /**
     * 金额类型(001-交易金额，002-代理分润,003-手工补入账,004-解冻金额,101-交易手续费,102-商户提现,103-代理提现,104-冻结金额,105-手工补出账)
     */
    private String amountType;

    /**
     * 金额,单位分
     */
    private Long amount;

    /**
     * 操作前账户余额,单位分
     */
    private Long preBalance;

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

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Byte getFlowType() {
        return flowType;
    }

    public void setFlowType(Byte flowType) {
        this.flowType = flowType;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(Long preBalance) {
        this.preBalance = preBalance;
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
