package org.xxpay.common.enumm;

import org.xxpay.common.constant.CashConstant;

public class CashOrderCashStateConverter {
    private CashOrderCashStateConverter() {
    }

    /**
     * 转换审核状态
     * @param status
     * @return
     */
    public static String convert2Label(byte status) {
        switch (status) {
            case CashConstant
                    .CASH_STATUS_INIT:
                return "未处理";
            case CashConstant.CASH_STATUS_APPLY:
                return "处理中";
            case CashConstant.CASH_STATUS_SUCCESS:
                return "已下发";
                case  CashConstant.CASH_STATUS_FAIL:
                    return "审核未通过";
            default:
                return "未知";
        }
    }
}
