package org.xxpay.dal.dao.model;
import java.io.Serializable;
import java.util.Date;

public class MchCompany  implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 客服电话
     */
    private String companyTel;

    /**
     * 企业类型
     */
    private String corpType;

    /**
     * 公司简称
     */
    private String shortName;

    /**
     * 地址
     */
    private String address;

    /**
     * 公司类型
     */
    private String companyType;

    /**
     * 营业执照号码
     */
    private String businessNo;

    /**
     * 有效期
     */
    private String businessNoExpiryDate;

    /**
     * 法人姓名
     */
    private String corporationName;

    /**
     * 法人身份证
     */
    private String corporationId;

    /**
     * 法人手机
     */
    private String corporationMobile;

    /**
     * 身份证正面照地址
     */
    private String idCardFrontUrl;

    /**
     * 身份证反面照地址
     */
    private String idCardBackUrl;

    /**
     * 营业执照地址
     */
    private String businessNoUrl;

    /**
     * 创建人用户ID
     */
    private String createBy;

    /**
     * 更新用户id
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessNoExpiryDate() {
        return businessNoExpiryDate;
    }

    public void setBusinessNoExpiryDate(String businessNoExpiryDate) {
        this.businessNoExpiryDate = businessNoExpiryDate;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public String getCorporationMobile() {
        return corporationMobile;
    }

    public void setCorporationMobile(String corporationMobile) {
        this.corporationMobile = corporationMobile;
    }

    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }

    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }

    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }

    public String getBusinessNoUrl() {
        return businessNoUrl;
    }

    public void setBusinessNoUrl(String businessNoUrl) {
        this.businessNoUrl = businessNoUrl;
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
}
