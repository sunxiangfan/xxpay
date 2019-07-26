package org.xxpay.common.enumm;

import org.xxpay.common.constant.PayConstant;

public class PayOrderStatusConverter {
    private PayOrderStatusConverter() {
    }

    /**
     * 转换支付状态
     * @param status
     * @return
     */
    public static String convert2Label(byte status) {
        switch (status) {
            case PayConstant
                    .PAY_STATUS_INIT:
                return "订单生成";
            case PayConstant.PAY_STATUS_PAYING:
                return "支付中";
            case PayConstant.PAY_STATUS_SUCCESS:
                return "支付成功";
            case PayConstant.PAY_STATUS_COMPLETE:
                return "处理完成";
            default:
                return "未知";
        }
    }
}
