package org.xxpay.common.enumm;

import java.util.HashMap;

/**
 * 收支类型
 */
public enum AmountFlowType {
    /**
     * 收入
     */
    IN((byte) 0, "收入"),

    /**
     * 支出
     */
    OUT((byte) 1, "支出");

    private static HashMap<Byte, AmountFlowType> keyValues = new HashMap<Byte, AmountFlowType>();

    static {
        for (AmountFlowType amountFlowType : AmountFlowType.values()) {
            keyValues.put(amountFlowType.getCode(), amountFlowType);
        }
    }

    public static AmountFlowType getInstanceByCode(byte code) {
        return keyValues.get(code);
    }

    private AmountFlowType(Byte code, String description) {
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
