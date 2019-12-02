package com.qunar.qchat.utils;

import java.util.UUID;

/**
 * @auth dongzd.zhang
 * @Date 2019/6/27 18:24
 */
public class KeyGenerator {

    public static String smsValidateCodeKey(String tel) {
        return "im_http_service:sms:" + (tel == null ? "" : tel) + "_sms_code";
    }

    public static String getPwdSalt() { return UUID.randomUUID().toString().replaceAll("-",""); }

}
