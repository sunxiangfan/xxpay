package org.xxpay.common.enumm;

/**
 * 用户类型
 */
public enum UserType {
    /**
     * 代理商户
     */
    MERCHANT((byte) 0, "代理商户"),

    /**
     * 子商户
     */
    SUB_MERCHANT((byte) 1, "子商户"),

    /**
     * 平台用户
     */
    PLATFORM((byte) 2, "平台用户");

    private UserType(Byte code, String description) {
        this.code = code;
        this.description = description;
    }

    private Byte code;
    private String description;

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
