package org.xxpay.dal.dao.model.dto;

import java.util.Date;

/**
 * 结算单分组统计Model
 */
public class BillGroupCreateDateSumAmountModel {
    /**
     * 日期
     */
    private Date createDate;
    /**
     * 交易金额
     */
    private Long amount;
    /**
     * 三方手续费
     */
    private Long thirdDeduction;
    /**
     * 代理分润
     */
    private Long agentMchCommission;
    /**
     * 平台分润
     */
    private Long platformCommission;


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getThirdDeduction() {
        return thirdDeduction;
    }

    public void setThirdDeduction(Long thirdDeduction) {
        this.thirdDeduction = thirdDeduction;
    }

    public Long getAgentMchCommission() {
        return agentMchCommission;
    }

    public void setAgentMchCommission(Long agentMchCommission) {
        this.agentMchCommission = agentMchCommission;
    }

    public Long getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(Long platformCommission) {
        this.platformCommission = platformCommission;
    }
}
