package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.Md5Utils;
import com.qunar.qchat.utils.RedisUtil;
import com.qunar.qchat.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * RedisAuthController
 *
 * @author binz.zhang
 * @date 2018/9/21
 */
@RequestMapping("/corp/auth/")
@Controller
public class CheckAuthTKController {
    private static final String ARGS_SPLITTER = "&";
    private static final String ARG_JOINER = "=";
    private static final String JOINER_USER_HOST = "@";
    private static final String U = "u";
    private static final String T = "t";
    private static final String K = "k";
    private static final String D = "d";
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckAuthTKController.class);
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/check_user_tkey.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> testConnection(@RequestBody String json) {
        LOGGER.info("check_user_tkey.qunar param is {}",json);
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            ObjectMapper mapper = new ObjectMapper();
            Map map = null;
            try {
                map = mapper.readValue(json, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("验证服务接受的json数据格式错误！");
                return JsonResultUtils.fail(2, "参数错误！");
            }
            String CkeyReceive = StrUtils.getStringValue(map.get("ckey"), "");
            String CKey = new String(decoder.decode(CkeyReceive));
            Map<String, String> args = getUserCkeyArgs(CKey);

            if (!args.containsKey(D)) {
                args.put(D, Config.getProperty("default_host"));
            }
            if (!args.containsKey(D) || !args.containsKey(U) || !args.containsKey(T) || !args.containsKey(K)) {
                return JsonResultUtils.fail(2, "传递参数缺失！");
            }
            Set<String> tokenRedis = redisUtil.hGetKeys(2, args.get(U) + JOINER_USER_HOST + args.get(D));
            if (checkUserAuth(args.get(U), args.get(T), args.get(K), tokenRedis)) {
                Map<String, Object> resultMap = new HashMap<>();

                resultMap.put("u", args.get(U));
                resultMap.put("d", args.get(D));
                return JsonResultUtils.success(resultMap);
            }
        }catch (Exception e){
            LOGGER.error("验证失败 异常信息",e);
            return JsonResultUtils.fail(2, "验证失败！");
        }
        return JsonResultUtils.fail(2, "验证失败！");
    }

    private Map<String, String> getUserCkeyArgs(String Ckey) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> map0 = Splitter.on(ARGS_SPLITTER).withKeyValueSeparator(ARG_JOINER).split(Ckey);
            for(String key: map0.keySet()) {
                map.put(key, map0.get(key));
            }

            return map;
        } catch (Exception e) {
            LOGGER.error("Ckey 参数分离出现异常，异常信息：{}", e);
        }
        return null;
    }

    private boolean checkUserAuth(String user, String t, String k, Set<String> redisTokens) {
        String lowKey = k.toLowerCase();
        if (redisTokens.isEmpty()) {
            return false;
        }
        for (String redisToken : redisTokens) {
            if (lowKey.equals(Md5Utils.md5Encode(redisToken + t))) {
                LOGGER.info("用户{},验证通过", user);
                return true;
            }
        }
        LOGGER.info("用户{},验证不通过", user);
        return false;
    }
}
