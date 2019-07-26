package org.xxpay.common.enumm;

import java.util.HashMap;

/**
 * 账户金额变动类型
 */
public enum AmountType {
    /**
     * 交易金额
     */
    TRANSACTION_AMOUNT("001", "交易金额", AmountFlowType.IN),


    /**
     * 代理分润
     */
    AGENT_COMMISSION("002", "代理分润", AmountFlowType.IN),


    /**
     * 手工补入账
     */
    ADMIN_ADJUST_INCREASE("003", "手工补入账", AmountFlowType.IN),


    /**
     * 解冻金额
     */
    UNLOCK_AMOUNT("004", "解冻金额", AmountFlowType.IN),


    /**
     * 返还提现手续费(如申请被拒绝)
     */
    RE_CASH_FEE("005", "返还提现手续费", AmountFlowType.IN),


    /**
     * 返还提现金额(如申请被拒绝)
     */
    RE_CASH("006", "返还提现金额", AmountFlowType.IN),

    /**
     * 交易手续费
     */
    TRANSACTION_FEE("101", "交易手续费", AmountFlowType.OUT),


    /**
     * 商户提现
     */
    MCH_CASH("102", "商户提现", AmountFlowType.OUT),


    /**
     * 代理商提现
     */
    AGENT_CASH("103", "代理商提现", AmountFlowType.OUT),


    /**
     * 冻结金额
     */
    LOCK_AMOUNT("104", "冻结金额", AmountFlowType.OUT),


    /**
     * 手工补出账
     */
    ADMIN_ADJUST_DECREASE("105", "手工补出账", AmountFlowType.OUT),

    /**
     * 提现手续费
     */
    CASH_FEE("106", "提现手续费", AmountFlowType.OUT);

    private  static HashMap<String,AmountType> keyValues=new HashMap<String, AmountType>();
    static {
        for(AmountType amountType: AmountType.values()){
            keyValues.put(amountType.getCode(),amountType);
        }
    }
    public  static  AmountType getInstanceByCode(String code){
        return  keyValues.get(code);
    }
    private AmountType(String code, String description, AmountFlowType flowType) {
        this.code = code;
        this.description = description;
        this.flowType = flowType;
    }

    private String code;
    private String description;
    private AmountFlowType flowType;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AmountFlowType getFlowType() {
        return flowType;
    }

    public void setFlowType(AmountFlowType flowType) {
        this.flowType = flowType;
    }}
