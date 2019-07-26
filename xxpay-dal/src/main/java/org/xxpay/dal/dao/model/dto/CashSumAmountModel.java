package org.xxpay.dal.dao.model.dto;

/**
 * 代付统计model
 */
public class CashSumAmountModel {

    /**
     * 成功金额
     */
    private Long successAmount;

    /**
     * 待处理金额
     */
    private Long todoAmount;

    /**
     * 成功单数
     */
    private Integer successCount;

    /**
     * 失败单数
     */
    private Integer failCount;

    public Long getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(Long successAmount) {
        this.successAmount = successAmount;
    }

    public Long getTodoAmount() {
        return todoAmount;
    }

    public void setTodoAmount(Long todoAmount) {
        this.todoAmount = todoAmount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}
