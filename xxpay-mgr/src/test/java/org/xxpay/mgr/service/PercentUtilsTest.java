package org.xxpay.mgr.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.xxpay.common.util.PercentUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PercentUtilsTest {
    @Test
    public void test01() {
        String a = "10";
        String b = PercentUtils.convertPercent2Decimal(a);
        Assert.isTrue(b.equals("0.1"));
        double c = Double.parseDouble(b);
        Assert.isTrue(c == 0.1);
    }

    @Test
    public void test02() {
        String a = "0.030";
        String b = PercentUtils.convertDecimal2Percent(a);
        Assert.isTrue(b.equals("3.000"));
    }


    @Test
    public void test03() {
        double a = 0.03;
        String b = PercentUtils.convertDecimal2Percent(a + "");
        Assert.isTrue(b.equals("3.00"));
        double c = Double.parseDouble(b);
        Assert.isTrue(c == 3);
    }
}
