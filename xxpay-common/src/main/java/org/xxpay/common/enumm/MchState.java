package org.xxpay.common.enumm;

/**
 * 商户状态,0-停止使用,1-使用中
 */
public enum MchState {
    /**
     * 停用
     */
    DISABLE((byte) 0, "停用"),

    /**
     * 使用中
     */
    ENABLE((byte) 1, "使用中");

    private MchState(Byte code, String description) {
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
