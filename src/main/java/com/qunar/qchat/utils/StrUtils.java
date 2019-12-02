package com.qunar.qchat.utils;

public class StrUtils {
    public static String getStringValue(Object o) {
        return getStringValue(o, "");
    }

    public static String getStringValue(Object o, String def) {
        if (o == null) {
            return def;
        } else {
            return o.toString();
        }
    }
}
