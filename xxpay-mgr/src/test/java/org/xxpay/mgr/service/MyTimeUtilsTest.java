package org.xxpay.mgr.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.xxpay.common.util.MyTimeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTimeUtilsTest {
    @Test
    public void test01() {

        Assert.isTrue(MyTimeUtils.testIsHourAndMinuteString("00:00"));
        Assert.isTrue(MyTimeUtils.testIsHourAndMinuteString("23:59"));
        Assert.isTrue(MyTimeUtils.testIsHourAndMinuteString("08:00"));
        Assert.isTrue(MyTimeUtils.testIsHourAndMinuteString("10:00"));
        Assert.isTrue(MyTimeUtils.testIsHourAndMinuteString("20:00"));
        Assert.isTrue(MyTimeUtils.testIsHourAndMinuteString("21:11"));


        Assert.isTrue(!MyTimeUtils.testIsHourAndMinuteString("24:00"));
        Assert.isTrue(!MyTimeUtils.testIsHourAndMinuteString("-10:00"));
        Assert.isTrue(!MyTimeUtils.testIsHourAndMinuteString("000:00"));

        MyTimeUtils.HourAndMinute hourAndMinute=null;
        hourAndMinute=MyTimeUtils.convert2HourAndMinute("00:10");
        Assert.isTrue(hourAndMinute.getHour().equals(0)&&hourAndMinute.getMinute().equals(10));
        hourAndMinute=MyTimeUtils.convert2HourAndMinute("23:59");
        Assert.isTrue(hourAndMinute.getHour().equals(23)&&hourAndMinute.getMinute().equals(59));
    }
}
