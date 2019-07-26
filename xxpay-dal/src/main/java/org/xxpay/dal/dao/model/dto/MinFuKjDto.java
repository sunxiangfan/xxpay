package org.xxpay.dal.dao.model.dto;

/**
 * @ProjectName: xxpay-master
 * @Package: org.xxpay.dal.dao.model.dto
 * @ClassName: MinFuKjDto
 * @Description:
 * @CreateDate: 2019/6/18 18:42
 * @UpdateUser: 更新者
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MinFuKjDto {
    private String mobile;//签约手机号
    private String cardNo;//签约银行卡号
    private String bankCode;//银行代码

    public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    private String payChannelId;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    private String mchId;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
