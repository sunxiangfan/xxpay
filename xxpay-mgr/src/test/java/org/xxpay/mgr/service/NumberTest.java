package org.xxpay.mgr.service;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.xxpay.common.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NumberTest {

    @Test
    public void test01() {
        int a = 358;
        double b = 0.1;
        double c = a * b;
        double d = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue();
        Assert.isTrue(d == 35.8, "精度问题");
    }

    @Test
    public void test02() {
        double a = 20.01;
        double b = 10000;
        double d = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 4, RoundingMode.HALF_DOWN).doubleValue();
        double e = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b)).doubleValue();
        Assert.isTrue(d == 0.0020, "精度问题");
        Assert.isTrue(d == 0.0020, "精度问题");
    }

    @Test
    public void test03() {
        String a = "20";
        String b = NumberUtils.toScaledBigDecimal(a, 2, RoundingMode.HALF_DOWN).toString();
        Assert.isTrue(b.equals("20.00"));
    }

    @Test
    public void test04() {
        long a=BigDecimalUtils.mul(150L,0.01);
        Assert.isTrue(a == 1);

        long b=BigDecimalUtils.mul(140L,0.01);
        Assert.isTrue(b == 1);


        long c=BigDecimalUtils.mul(5000050L,0.01);
        Assert.isTrue(c == 50000);

        long d=BigDecimalUtils.mul(1050,0.015);
        Assert.isTrue(d== 15);
    }
}
