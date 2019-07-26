package org.xxpay.common.enumm;

import org.xxpay.common.constant.PayConstant;

public class PayTypeConverter {
    private PayTypeConverter() {
    }

    /**
     * 转换支付类型
     *
     * @param payType
     * @return
     */
    public static String convert2Label(String payType) {
        if (PayConstant
                .PAY_TYPE_GATEWAY.equals(payType)) {
            return "网关";
        } else if (PayConstant.PAY_TYPE_FAST_PAY.equals(payType)) {
            return "快捷";
        } else if (PayConstant.PAY_TYPE_TRANSFER.equals(payType)) {
            return "转账";
        }  else if (PayConstant.PAY_TYPE_ALIPAY_WAP.equals(payType)) {
            return "支付宝H5";
        } else {
            return "未知";
        }
    }
}
