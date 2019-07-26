package org.xxpay.dal.dao.model;

import java.util.Date;

/**
 * 对账单model
 */
public class StatementModel {

    /**
     * 商户号
     */
    private  String mchId;

    /**
     * 日期
     */
    private Date createDate;

    /**
     * 订单总数
     */
    private Integer totalCount;

    /**
     * 成功订单数
     */
    private  Integer successCount;

    /**
     * 未支付订单数
     */
    private  Integer failCount;

    /**
     * 订单总额
     */
    private  Long applyAmount;

    /**
     * 订单实付金额
     */
    private  Long successAmount;

    /**
     * 到账金额
     */
    private  Long successActualAmount;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Long getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Long applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Long getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(Long successAmount) {
        this.successAmount = successAmount;
    }

    public Long getSuccessActualAmount() {
        return successActualAmount;
    }

    public void setSuccessActualAmount(Long successActualAmount) {
        this.successActualAmount = successActualAmount;
    }
}
