package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class MchInfo implements Serializable ,ICleanBackgroundInfo{

    @Override
    public void cleanBackgroundInfo() {
        this.setResKey(null);
        this.setReqKey(null);
    }

    /**
     * id
     */
    private String id;

    /**
     * 代理商户号
     */
    private String agentId;

    /**
     * 密码
     */
    private String password;

    /**
     * 余额(单位分)
     */
    private Long balance;

    /**
     * 冻结金额(单位分)
     */
    private Long lockAmount;

    /**
     * 提现中金额(单位分)
     */
    private Long cashingAmount;

    /**
     * 保证金(单位分)
     */
    private Long deposit;

    /**
     * 商户状态,0-停止使用,1-使用中
     */
    private Byte state;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 审核状态,0-未审核,1-已审核，2-未通过审核
     */
    private Byte auditState;

    /**
     * D0比例
     */
    private Double d0Rate;

    /**
     * 商户请求私钥
     */
    private String reqKey;

    /**
     * 响应商户私钥
     */
    private String resKey;

    /**
     * 提现单笔手续费(单位分)
     */
    private Long cashDeduction;

    /**
     * 并发版本号
     */
    private Integer version;

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

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getLockAmount() {
        return lockAmount;
    }

    public void setLockAmount(Long lockAmount) {
        this.lockAmount = lockAmount;
    }

    public Long getCashingAmount() {
        return cashingAmount;
    }

    public void setCashingAmount(Long cashingAmount) {
        this.cashingAmount = cashingAmount;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getAuditState() {
        return auditState;
    }

    public void setAuditState(Byte auditState) {
        this.auditState = auditState;
    }

    public Double getD0Rate() {
        return d0Rate;
    }

    public void setD0Rate(Double d0Rate) {
        this.d0Rate = d0Rate;
    }

    public String getReqKey() {
        return reqKey;
    }

    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Long getCashDeduction() {
        return cashDeduction;
    }

    public void setCashDeduction(Long cashDeduction) {
        this.cashDeduction = cashDeduction;
    }
}
