package org.xxpay.common.util;

import java.math.BigDecimal;

public class BigDecimalUtils {
    private static final int DEF_DIV_SCALE = 10;

    private BigDecimalUtils() {
    }

    /**
     * 相加
     *
     * @param d1
     * @param d2
     * @return 相加
     */
    public static long add(long d1, long d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.add(b2).longValue();

    }

    /**
     * 相减
     *
     * @param d1
     * @param d2
     * @return 相减
     */
    public static long sub(long d1, long d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.subtract(b2).longValue();

    }

    /**
     * 相减
     *
     * @param d1
     * @param d2
     * @return 相减
     */
    public static double sub(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.subtract(b2).doubleValue();

    }


    /**
     * 相乘
     *
     * @param d1
     * @param d2
     * @return 相乘
     */
    public static long mul(long d1, long d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.multiply(b2).longValue();
    }

    /**
     * 相加
     *
     * @param d1
     * @param d2
     * @return 相加
     */
    public static double add(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.add(b2).doubleValue();

    }

    /**
     * 相乘
     *
     * @param d1
     * @param d2
     * @return 相乘
     */
    public static long mul(long d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.multiply(b2).setScale(0, BigDecimal.ROUND_DOWN).longValue();
    }

    /**
     * 相除
     *
     * @param d1
     * @param d2
     * @return 相除
     */
    public static long div(long d1, long d2) {

        return div(d1, d2, DEF_DIV_SCALE);

    }

    public static long div(long d1, long d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).longValue();

    }

    public static void main(String[] args) {
        long a=mul(1050,0.015);
        System.out.println(a);
    }
}
