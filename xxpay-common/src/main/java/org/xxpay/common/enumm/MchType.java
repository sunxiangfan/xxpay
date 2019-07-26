package org.xxpay.common.enumm;

/**
 * 商户类型
 */
public enum MchType {
    /**
     * 代理
     */
    AGENT((byte) 0, "代理"),

    /**
     * 子商户
     */
    MERCHANT((byte) 1, "商户");

    private MchType(Byte code, String description) {
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
