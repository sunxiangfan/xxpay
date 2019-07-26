package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class MchWithdrawApply implements Serializable {

    /**
     * Id
     */
    private String id;

    /**
     * 商户Id
     */
    private String mchId;

    /**
     * 申请金额
     */
    private Long applyAmount;

    /**
     * 实际到账金额
     */
    private Long actualAmount;

    /**
     * 银行卡id
     */
    private String mchBankCardId;

    /**
     * 银行银联号
     */
    private  String bankUnionCode;

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
     * 商户类型,0-普通商户,1-子商户
     */
    private Byte mchType;

    /**
     * 审核状态,0-审核中,1-审核通过,2-审核未通过
     */
    private Byte state;


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
     * 审核人
     */
    private String auditBy;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 申请前金额
     */
    private Long preBalance;

    /**
     * 申请后金额
     */
    private Long afterBalance;


    /**
     * 平台手续费
     */
    private Long platformDeduction;

    /**
     * 三方手续费
     */
    private Long thirdDeduction;

    /**
     * 代付状态,0-未申请,1-已申请,2-审核通过（下放），3-拒绝
     */
    private Byte cashState;

    /**
     * 代付通道id
     */
    private String cashChannelId;

    /**
     * 代付渠道单号
     */
    private String channelOrderNo;


    /**
     * 代付金额
     */
    private Long cashAmount;

    /**
     * 代付成功时间
     */
    private Date cashSuccTime;


    /**
     * 商户订单号
     */
    private String mchOrderNo;

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

    public Long getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Long applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Long getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Long actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getMchBankCardId() {
        return mchBankCardId;
    }

    public void setMchBankCardId(String mchBankCardId) {
        this.mchBankCardId = mchBankCardId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBankUnionCode() {
        return bankUnionCode;
    }

    public void setBankUnionCode(String bankUnionCode) {
        this.bankUnionCode = bankUnionCode;
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

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
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

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Long getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(Long preBalance) {
        this.preBalance = preBalance;
    }

    public Long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Long afterBalance) {
        this.afterBalance = afterBalance;
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

    public Byte getCashState() {
        return cashState;
    }

    public void setCashState(Byte cashState) {
        this.cashState = cashState;
    }

    public String getCashChannelId() {
        return cashChannelId;
    }

    public void setCashChannelId(String cashChannelId) {
        this.cashChannelId = cashChannelId;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Date getCashSuccTime() {
        return cashSuccTime;
    }

    public void setCashSuccTime(Date cashSuccTime) {
        this.cashSuccTime = cashSuccTime;
    }

    public Long getPlatformDeduction() {
        return platformDeduction;
    }

    public void setPlatformDeduction(Long platformDeduction) {
        this.platformDeduction = platformDeduction;
    }

    public Long getThirdDeduction() {
        return thirdDeduction;
    }

    public void setThirdDeduction(Long thirdDeduction) {
        this.thirdDeduction = thirdDeduction;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }
}
