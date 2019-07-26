package org.xxpay.mgr.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.xxpay.common.util.HolidayUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HolidayUtilTest {
    private  static final  String strWeekdayConfig="{\"2019\":[\"0428\",\"0505\",\"0929\",\"1012\"]}";
    private static  final String strHolidayConfig="{\"2019\":[\"0405\",\"0406\",\"0407\",\"0501\",\"0502\",\"0503\",\"0607\",\"0913\",\"0914\",\"0915\",\"1001\",\"1002\",\"1003\",\"1004\",\"1005\",\"1006\",\"1007\"]}";

    @Test
    public  void  testHoliday(){
        JSONObject holidayConfig= JSON.parseObject(strHolidayConfig);
        JSONObject weekdayConfig=JSON.parseObject(strWeekdayConfig);
        Calendar calendar=Calendar.getInstance();
        calendar.set(2019,4,1);
        Assert.isTrue(!HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,4,4);
        Assert.isTrue(!HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,4,5);
        Assert.isTrue(HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,4,6);
        Assert.isTrue(HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,4,11);
        Assert.isTrue(!HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,4,12);
        Assert.isTrue(!HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,5,7);
        Assert.isTrue(!HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,5,9);
        Assert.isTrue(!HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));
        calendar.set(2019,5,10);
        Assert.isTrue(HolidayUtil.isWeekday(calendar.getTime(),holidayConfig,weekdayConfig));

    }

    private  static  final  String FORMAT_YYYY_MM_DD="yyyyMMdd";

    @Test
    public  void  testGetNextWeekday(){
        JSONObject holidayConfig= JSON.parseObject(strHolidayConfig);
        JSONObject weekdayConfig=JSON.parseObject(strWeekdayConfig);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        calendar.set(2019,3,2);
        Date nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190403".equals(dateFormat.format(nextWeekday)));


        calendar.set(2019,3,3);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190404".equals(dateFormat.format(nextWeekday)));


        calendar.set(2019,3,4);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190408".equals(dateFormat.format(nextWeekday)));


        calendar.set(2019,3,5);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190408".equals(dateFormat.format(nextWeekday)));

        calendar.set(2019,3,6);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190408".equals(dateFormat.format(nextWeekday)));

        calendar.set(2019,3,7);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190408".equals(dateFormat.format(nextWeekday)));

        calendar.set(2019,3,8);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190409".equals(dateFormat.format(nextWeekday)));


        calendar.set(2019,3,26);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190428".equals(dateFormat.format(nextWeekday)));
        calendar.set(2019,3,27);
        nextWeekday= HolidayUtil.getNextWeekday(calendar.getTime(),holidayConfig,weekdayConfig);
        Assert.isTrue("20190428".equals(dateFormat.format(nextWeekday)));
    }
}
