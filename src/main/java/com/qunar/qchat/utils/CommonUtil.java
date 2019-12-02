package com.qunar.qchat.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/29 17:21
 */
public class CommonUtil {


    /**
     * 生成一个uuid.
     * @return String
     * */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }


    /**
     * 构建一个map.
     * @return Map
     * */
    public static Map<String, String> buildMap(String[] ... kvs){
        Map<String, String>  map = new HashMap<String, String>();
        for (String[] kv : kvs) {
            map.put(kv[0], kv[1]);
        }
        return map;
    }

}
