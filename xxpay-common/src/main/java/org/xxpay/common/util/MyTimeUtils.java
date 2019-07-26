package org.xxpay.common.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTimeUtils {
    private MyTimeUtils() {
    }

    private static final String REG_STRING = "^((?:[01][0-9])|(?:2[0-3])):([0-5][0-9])$";

    public static boolean testIsHourAndMinuteString(String withinText) {
        Pattern pattern = Pattern.compile(REG_STRING);
        Matcher matcher = pattern.matcher(withinText);
        boolean matches = matcher.matches();
        return matches;
    }

    public static HourAndMinute convert2HourAndMinute(String withinText) {
        Pattern pattern = Pattern.compile(REG_STRING);
        Matcher matcher = pattern.matcher(withinText);
        if(!testIsHourAndMinuteString(withinText)){
            throw new  IllegalArgumentException("withinText error.");
        }
        HourAndMinute result = null;
        while (matcher.find()) {
            Integer hour = Integer.parseInt(matcher.group(1));
            Integer minute = Integer.parseInt(matcher.group(2));
            result = new HourAndMinute();
            result.setHour(hour);
            result.setMinute(minute);
        }
        return result;
    }

    public static class HourAndMinute {
        private Integer hour;
        private Integer minute;

        public Integer getHour() {
            return hour;
        }

        public void setHour(Integer hour) {
            this.hour = hour;
        }

        public Integer getMinute() {
            return minute;
        }

        public void setMinute(Integer minute) {
            this.minute = minute;
        }
    }
}
