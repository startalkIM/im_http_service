package com.qunar.qchat.constants;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Author : mingxing.shao
 * Date : 16-3-29
 * email : mingxing.shao@qunar.com
 */
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public static final String URL_SEND_NOTIFY = getProperty("url_send_notify");
    public static final String URL_GET_DEPS_QTALK = getProperty("url_get_deps_qtalk");

    public static final String SEND_NOTE_URL = getProperty("send_note_url");
    public static final String CREATE_QRCODE_URL = getProperty("create_qrcode_url");

    public static final String CURRENT_ENV = getProperty("current_env");

    public static final String GET_VCARD_INFO_URL = getProperty("get_vcard_info_url");

    public static final String LOGIN_VALIDATE_URL = getProperty("login_validate_url");

    public static final String PUSH_MESSAGE_NOTICE_URL = getProperty("push_message_notice_url");
    public static final String UPDATE_MUC_VCARD_MSG_URL = getProperty("update_muc_vcard_msg_url");

    public static final int TOKEN_VALID_TIME = getIntProperty("token.valid.time", 7);
    public static final String COOKIE_DOMAIN = getProperty("cookie.domain");
    public static final int COOKIE_MAX_AGE = getIntProperty("cookie.max.age", 604800);

    public static final String DEFAULT_PIC_URL = getProperty("default_pic_url");

    private static Properties props;

    private synchronized static void init() {
        if (props != null) {
            return;
        }
        InputStreamReader isr = null;
        try {
            String filename = "app.properties";
            isr = new InputStreamReader(Config.class.getClassLoader().getResourceAsStream(filename), "UTF-8");
            props = new Properties();

            props.load(isr);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Initialize the config error!");
        } finally {
            closeStream(isr);
        }
    }

    public static String getProperty(String name) {
        if (props == null) {
            init();
        }
        String val = props.getProperty(name.trim());
        if (val == null) {
            return null;
        } else {
            //去除前后端空格
            return val.trim();
        }
    }

    public static String getProperty(String name, String defaultValue) {
        if (props == null) {
            init();
        }

        String value = getProperty(name);
        if (value == null) {
            value = defaultValue;
        }
        return value.trim();
    }

    //获得整数属性值
    public static int getIntProperty(String name, int defaultVal) {
        if (props == null) {
            init();
        }

        int val = defaultVal;
        String valStr = getProperty(name);
        if (valStr != null) {
            val = Integer.parseInt(valStr);
        }
        return val;
    }

    //获得double属性值
    public static double getDoubleProperty(String name, double defaultVal) {
        if (props == null) {
            init();
        }

        double val = defaultVal;
        String valStr = getProperty(name);
        if (valStr != null) {
            val = Double.parseDouble(valStr);
        }
        return val;
    }

    public static boolean getBooleanItem(String name, boolean defaultValue) {
        if (props == null) {
            init();
        }

        boolean b = defaultValue;
        String valStr = getProperty(name);
        if (valStr != null) {
            b = Boolean.parseBoolean(valStr);
        }
        return b;
    }

    public static String getPropertyByEncoding(String name) {
        if (props == null) {
            init();
        }

        String val = getProperty(name);
        if (val == null) return null;
        try {
            return new String(val.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.info("catch error: {}", ExceptionUtils.getStackTrace(e));
            return val;
        }
    }

    public static String[] getArrayItem(String name) {
        if (props == null) {
            init();
        }

        String value = getProperty(name, "");
        if (value.trim().isEmpty()) {
            return null;
        }

        String sepChar = ",";
        if (value.contains(";")) {
            sepChar = ";";
        }
        return value.split(sepChar);

    }

    public static List<String> getListItem(String item) {
        if (props == null) {
            init();
        }

        List<String> list = new ArrayList<>();
        String value = getProperty(item, "");
        if (value.trim().isEmpty()) {
            return list;
        }

        String sepChar = ",";
        if (value.contains(";")) {
            sepChar = ";";
        }
        String[] sa = value.split(sepChar);
        for (String aSa : sa) {
            list.add(aSa.trim());
        }
        return list;
    }

    public static void setProperty(String name, String value) {
        if (props == null) {
            init();
        }

        props.setProperty(name, value);
    }

    private static void closeStream(InputStreamReader is) {
        if (is == null) {
            return;
        }

        try {
            is.close();
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Initialize the config error!");
        }
    }
}
