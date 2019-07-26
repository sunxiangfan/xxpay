package org.xxpay.common.enumm;

/**
 * 布尔类型(是否...)
 */
public enum BooleanType {
    /**
     * 否
     */
    FALSE((byte) 0, "否"),

    /**
     * 是
     */
    TRUE((byte) 1, "是");

    private BooleanType(Byte code, String description) {
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
