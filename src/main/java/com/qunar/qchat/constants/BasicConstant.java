package com.qunar.qchat.constants;

import java.util.Arrays;
import java.util.List;

/**
 * BasicConstant
 *
 * @author binz.zhang
 * @date 2019/1/29
 */
public class BasicConstant {
    /**
     * c_key 常量
     */
    public static final String CKEY_SPLITTER = "&";
    public static final String CKEY_JOINER = "=";
    /**
     * 用户类型常量
     */
    public static final List<String> SPECIALDEPS = Arrays.asList(new String[]{"权一组", "安置组"});
    public static final String USERTYPE_HOTLINE = "H";
    public static final String USERTYPE_NORMAL = "U";
    public static final String USERTYPE_SPECILA = "S";
    public static final String USERTYPE_ROBOT = "R";

}
