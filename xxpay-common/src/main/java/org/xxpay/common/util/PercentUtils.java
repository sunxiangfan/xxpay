package org.xxpay.common.util;


import java.math.BigDecimal;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description: 百分数
 * @date 2019-02-27
 */
public class PercentUtils {
    private PercentUtils() {
    }

    private static final BigDecimal DIVISOR = new BigDecimal(100);

    /**
     * 将字符百分数换算成小数
     * @param str 百分数，如20%，实参为20
     * @return 小数。如20%，返回0.2
     */
    public static String convertPercent2Decimal(String str) {
        BigDecimal bigDecimal = new BigDecimal(str);
        String result = bigDecimal.divide(DIVISOR).toString();
        return result;
    }

    /**
     * 将字符小数换算成百分数
     * @param str 小数。如0.2，实参为0.2
     * @return 百分数。如0.2，返回20
     */
    public static String convertDecimal2Percent(String str) {
        BigDecimal bigDecimal = new BigDecimal(str);
        String result = bigDecimal.multiply(DIVISOR).toString();
        return result;
    }
}
