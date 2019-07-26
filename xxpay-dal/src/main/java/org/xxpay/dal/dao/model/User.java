package org.xxpay.dal.dao.model;

import org.xxpay.common.enumm.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class User implements Serializable {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 登录账号
     */
    private String loginAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 部门
     */
    private String department;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 商户状态,0-停止使用,1-使用中
     *
     * @mbggenerated
     */
    private Byte state;

    /**
     * 用户类型,0-代理商户,1-子商户,2-平台用户
     *
     * @mbggenerated
     */
    private Byte type;

    /**
     * 业务id（如商户号）
     */
    private String bizId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建userId
     */
    public void generateUserId() {
        this.userId = UUID.randomUUID().toString();
    }

    public static User build(UserType userType, String bizId) {
        User user = new User();
        user.generateUserId();
        user.setType(userType.getCode());
        user.setBizId(bizId);
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
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
