package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class SystemSetting implements Serializable {
    /**
     * Id
     */
    private String id;

    /**
     * name
     */
    private String paramName;

    /**
     * value
     */
    private String paramValue;

    /**
     * 描述
     */
    private String paramDesc;

    /**
     * 排序
     */
    private Integer paramOrder;

    /**
     * 是否可编辑 ,0-否,1-是
     */
    private  Byte flagEditable;

    /**
     * 是否可空 ,0-否,1-是
     */
    private  Byte flagNullable;

    /**
     * 是否为货币类型,0-否,1-是
     */
    private  Byte flagMoney;

    /**
     * 是否为普通数字类型,0-否,1-是
     */
    private  Byte flagNumber;

    /**
     * 并发版本号
     */
    private Integer version;

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

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public Integer getParamOrder() {
        return paramOrder;
    }

    public void setParamOrder(Integer paramOrder) {
        this.paramOrder = paramOrder;
    }

    public Byte getFlagEditable() {
        return flagEditable;
    }

    public void setFlagEditable(Byte flagEditable) {
        this.flagEditable = flagEditable;
    }

    public Byte getFlagNullable() {
        return flagNullable;
    }

    public void setFlagNullable(Byte flagNullable) {
        this.flagNullable = flagNullable;
    }

    public Byte getFlagMoney() {
        return flagMoney;
    }

    public void setFlagMoney(Byte flagMoney) {
        this.flagMoney = flagMoney;
    }

    public Byte getFlagNumber() {
        return flagNumber;
    }

    public void setFlagNumber(Byte flagNumber) {
        this.flagNumber = flagNumber;
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
