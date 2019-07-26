package org.xxpay.dal.dao.model.dto;

import java.util.Date;

public class PayOrderGroupCreateDateSumAmountModel {

    private Date createDate;

    private Long amount;

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
}
