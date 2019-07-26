package org.xxpay.common.enumm;

import org.xxpay.common.constant.PayConstant;

public class CashOrderStateConverter {
    private CashOrderStateConverter() {
    }

    /**
     * 转换审核状态
     * @param status
     * @return
     */
    public static String convert2Label(byte status) {
        switch (status) {
            case 0:
                return "未审核";
            case 1:
                return "审核通过";
            case 2:
                return "审核未通过";
            default:
                return "未知";
        }
    }
}
