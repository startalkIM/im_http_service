package com.qunar.qchat.utils;

import org.springframework.util.DigestUtils;

/**
 * Md5Utils
 *
 * @author binz.zhang
 * @date 2018/9/21
 */

public class Md5Utils {
    public static String md5Encode(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }

    public static String md5(String content, String key) {
        String originStr = content + key;
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(originStr);
    }

    public static boolean verify(String content, String key, String md5Value) {
        String md5 = Md5Utils.md5(content, key);
        if(md5.equals(md5Value)) {
            return true;
        }
        return false;
    }

    public static String md5ForPassword(String password, String key) {
        //加密后的字符串
        String encodeStr = org.apache.commons.codec.digest.DigestUtils.md5Hex(password) + key;
        encodeStr = org.apache.commons.codec.digest.DigestUtils.md5Hex(encodeStr);
        encodeStr = org.apache.commons.codec.digest.DigestUtils.md5Hex(encodeStr);
        return encodeStr;
    }
}
