package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class MchBankCard implements Serializable {

    /**
     * Id
     */
    private String id;

    /**
     * 商户Id
     */
    private String mchId;

    /**
     * 商户类型
     */
    private Byte mchType;

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
     * 银行银联号
     */
    private  String bankUnionCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型,0-对私,1-对公
     */
    private Byte type;


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
    private  String province;

    /**
     * 市
     */
    private String city;

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

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBankUnionCode() {
        return bankUnionCode;
    }

    public void setBankUnionCode(String bankUnionCode) {
        this.bankUnionCode = bankUnionCode;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
