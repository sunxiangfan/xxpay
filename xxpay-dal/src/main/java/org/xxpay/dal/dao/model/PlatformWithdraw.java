package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class PlatformWithdraw implements Serializable {

    /**
     * Id
     */
    private String id;

    /**
     * 申请金额
     */
    private Long applyAmount;

    /**
     * 卡号
     */
    private String number;

    /**
     * 开户行名称
     */
    private String registeredBankName;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 绑卡手机号
     */
    private String mobile;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 三方手续费
     */
    private Long thirdDeduction;

    /**
     * 代付通道id
     */
    private String cashChannelId;

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

    public Long getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Long applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegisteredBankName() {
        return registeredBankName;
    }

    public void setRegisteredBankName(String registeredBankName) {
        this.registeredBankName = registeredBankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getThirdDeduction() {
        return thirdDeduction;
    }

    public void setThirdDeduction(Long thirdDeduction) {
        this.thirdDeduction = thirdDeduction;
    }

    public String getCashChannelId() {
        return cashChannelId;
    }

    public void setCashChannelId(String cashChannelId) {
        this.cashChannelId = cashChannelId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
