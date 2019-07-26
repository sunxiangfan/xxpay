package org.xxpay.dal.dao.model;

import org.xxpay.common.enumm.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Bank implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 银行名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
