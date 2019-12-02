package com.qunar.qchat.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;


/**
 * Created by Administrator on 2017/7/22.
 */
public class PgTimeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PgTimeUtils.class);

    public static double getCorrectTime(String time)
    {
        DecimalFormat df = new DecimalFormat("#.######");

        if (Double.parseDouble(time) >= 1000000000000L) {
            String time2 =   df.format(Double.parseDouble(time)/1000);
            return Double.valueOf(time2);
        }else {
            return Double.valueOf(time);
        }
    }

}
