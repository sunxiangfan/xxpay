package org.xxpay.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HolidayUtil {
    private static  final String FORMAT_MM_DD="MMdd";
    /**
     * 是否工作日
     * @param date
     * @param holidayConfig
     * @param weekdayConfig
     * @return 工作日返回true,周末、节假日返回false
     */
    public  static  boolean isWeekday(Date date, JSONObject holidayConfig,JSONObject weekdayConfig){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
        int year=calendar.get(Calendar.YEAR);
        boolean isWeekend=dayOfWeek==Calendar.SATURDAY|| dayOfWeek==Calendar.SUNDAY;//是否是周末
        SimpleDateFormat dateFormat=new SimpleDateFormat(FORMAT_MM_DD);
        String mmdd= dateFormat.format(date);
        if(isWeekend){
            JSONArray jsonArray=weekdayConfig.getJSONArray(year+"");
            if(jsonArray ==null || jsonArray.size()<=0){
                throw new IllegalArgumentException("配置的法定工作日有误。缺少年份："+year);
            }
            boolean isLegalWeekday=jsonArray.contains(mmdd);
            boolean result= isLegalWeekday;
            return  result;
        }
        else{
            JSONArray jsonArray=holidayConfig.getJSONArray(year+"");
            if(jsonArray ==null || jsonArray.size()<=0){
                throw new IllegalArgumentException("配置的法定节假日有误。缺少年份："+year);
            }
            boolean isLegalHoliday=jsonArray.contains(mmdd);
            boolean result= !isLegalHoliday;
            return  result;
        }

    }

    /**
     * 返回按指定日期的下一个工作日
     * @param date
     * @param holidayConfig
     * @param weekdayConfig
     * @return
     */
    public static  Date getNextWeekday(Date date,JSONObject holidayConfig,JSONObject weekdayConfig){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        Date nextDate=calendar.getTime();
        boolean nextDateIsWeekday=isWeekday(nextDate,holidayConfig,weekdayConfig);
        if(nextDateIsWeekday){
            return nextDate;
        }
        else{
            return getNextWeekday(nextDate,holidayConfig,weekdayConfig);
        }
    }
}
