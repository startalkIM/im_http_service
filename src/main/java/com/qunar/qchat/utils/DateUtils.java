package com.qunar.qchat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    public static String getFormatTime(String time) {
        try {
            String str = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(str, Locale.CHINA);
            Date date = sdf.parse(time);
            return sdf.format(date);
        } catch (Exception e) {
            LOGGER.error("getFormatTime error for {}", e);
            return "";
        }
    }

    public static String getTimestamp(Date time) {
        String updateTimeStr = "";
        if(time != null) {
            Long timestamp = time.getTime();
            if(timestamp != null) {
                updateTimeStr = String.valueOf(timestamp);
            }
        }
        return updateTimeStr;
    }
}
