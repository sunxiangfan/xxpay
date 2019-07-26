package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class Agent implements Serializable ,ICleanBackgroundInfo{

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
     * password
     */
    private String password;

    /**
     * 余额(单位分)
     */
    private Long balance;

    /**
     * 锁定金额(单位分)
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
     * 状态,0-停止使用,1-使用中
     */
    private Byte state;

    /**
     * 姓名
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
     * 请求私钥
     */
    private String reqKey;

    /**
     * 响应私钥
     */
    private String resKey;

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
}
