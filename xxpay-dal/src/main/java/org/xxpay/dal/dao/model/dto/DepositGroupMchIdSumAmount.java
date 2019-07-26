package org.xxpay.dal.dao.model.dto;

/**
 * 保证金按商户号分组统计金额model
 */
public class DepositGroupMchIdSumAmount {
    private String mchId;
    private Long sumAmount;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Long sumAmount) {
        this.sumAmount = sumAmount;
    }
}
