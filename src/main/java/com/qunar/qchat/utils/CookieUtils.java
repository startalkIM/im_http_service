package com.qunar.qchat.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *  * Created by MSI on 2017/9/7.
 *   */
public class CookieUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtils.class);

    public static  Map<String,Object>  getUserbyCookie(HttpServletRequest request)  {
        Map<String,Object> map = new HashMap<String, Object>();
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            LOGGER.info("the ckey is null");

            return map;
        }else{
            for(Cookie cookie : cookies){
                if (cookie.getName().equals("q_ckey")){
                    try {
                        final Base64 base64 = new Base64();
                        String val = cookie.getValue();
                        byte[] original  = base64.decode(val);
                        String originalString = new String(original, "UTF8");// 重新显示明文
                        String[] list = originalString.split("&");
                        for(String str:list){
                            String[] m = str.split("=");
                            map.put(m[0], m[1]);
                        }
                    }catch (Exception e)
                    {
                        LOGGER.info("expecit is :{}",e.toString());
                    }
                    break;
                }
            }
        }
        return map;
    }


}