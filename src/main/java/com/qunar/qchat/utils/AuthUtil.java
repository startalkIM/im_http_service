package com.qunar.qchat.utils;

import com.qunar.qchat.model.result.CheckPushKeyResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/9 14:00
 */
@Component
public class AuthUtil {

    @Autowired
    private RedisUtil redisUtil;

    public static final Logger LOGGER = LoggerFactory.getLogger(AuthUtil.class);

    public CheckPushKeyResult checkMacUserToken(String key, String count) throws UnsupportedEncodingException {

        CheckPushKeyResult checkPushKeyResult = CheckPushKeyResult.builder()
                .validate(true).validateMsg("验证成功").build();

        if (StringUtils.isBlank(key) ||
                StringUtils.isBlank(count)) {
            checkPushKeyResult.setValidate(false);
            checkPushKeyResult.setValidateMsg("参数key或count为空");
            return checkPushKeyResult;
        }

        //base64解码key，得到redis key
        Base64 base64 = new Base64();
        String base64Decode = new String(base64.decode(key.getBytes("UTF-8")));
        String[] splitResult = base64Decode.split("&");

        if(splitResult.length < 2) {
            checkPushKeyResult.setValidate(false);
            checkPushKeyResult.setValidateMsg("参数key的base64解码错误");
            return checkPushKeyResult;
        }

        String[] strArr = splitResult[0].trim().split("=");

        if(strArr.length < 2) {
            checkPushKeyResult.setValidate(false);
            checkPushKeyResult.setValidateMsg("参数key中的u格式错误");
            return checkPushKeyResult;
        }

        if(strArr.length == 2 && strArr[1].indexOf('@') == -1) {
            checkPushKeyResult.setValidate(false);
            checkPushKeyResult.setValidateMsg("参数k中的u格式错误，缺少domain");
            return checkPushKeyResult;
        }

        String redisUserKey = strArr[1];



        String redisKey = redisUserKey + "_tkey";

        LOGGER.info("redis key:" + redisKey);

        Set<String> fields = redisUtil.hkeys(2, redisKey);

        if (CollectionUtils.isEmpty(fields)) {
            checkPushKeyResult.setValidate(false);
            checkPushKeyResult.setValidateMsg("待发送方未登陆");
            return checkPushKeyResult;
        }

        String strFields = fields.stream().collect(Collectors.joining(","));
        LOGGER.info("fields:" + strFields);


        for (String field : fields) {
            String v = "u=" + redisUserKey + "&k=" + DigestUtils.md5DigestAsHex(field.concat(count).getBytes("UTF-8"));//md5Hex(field.concat(count));

            byte[] bs = v.getBytes("UTF-8");
            String base64Result = base64.encodeToString(bs);
            LOGGER.info("base64 encode: " + base64Result);

            if (key.equals(base64Result)) {
                checkPushKeyResult.setValidate(true);
                checkPushKeyResult.setValidateMsg("登陆验证成功");
                return checkPushKeyResult;
            }
        }
        checkPushKeyResult.setValidate(false);
        checkPushKeyResult.setValidateMsg("接收方登陆验证失败");
        return checkPushKeyResult;
    }

}
