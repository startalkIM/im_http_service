package com.qunar.qchat.controller;

import com.google.common.base.Strings;
import com.qunar.qchat.dao.IUserInfo;
import com.qunar.qchat.dao.model.UserCheckTokenModel;
import com.qunar.qchat.dao.model.UserPasswordModel;
import com.qunar.qchat.dao.model.UserPasswordRO;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.service.IUserLogin;
import com.qunar.qchat.utils.JacksonUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.Md5Utils;
import com.qunar.qchat.utils.RSAEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class CheckUserLogin {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckUserLogin.class);

    @Resource
    private IUserLogin iUserLogin;
    @Resource
    private IUserInfo iUserInfo;

    @ResponseBody
    @RequestMapping(value = "/newapi/nck/qtlogin.qunar", method = RequestMethod.POST)
    public JsonResult<?> checkLogin(@RequestBody UserPasswordRO param) {
        long start = System.currentTimeMillis();
        LOGGER.info("check user login u :[{}] h:[{}] p:[{}] mk [{}]", param.getU(), param.getH(), param.getP(), param.getMk());
        if (param == null || Strings.isNullOrEmpty(param.getH()) || Strings.isNullOrEmpty(param.getP()) || Strings.isNullOrEmpty(param.getU()) || Strings.isNullOrEmpty(param.getMk())) {
            LOGGER.info("check user login fail u :[{}]  info lock cost[{}] ms", param.getU(), System.currentTimeMillis() - start);
            return JsonResultUtils.fail(1, "Authentication failed");
        }
        UserPasswordModel userPasswordModel = iUserLogin.checkUserLogin(param);
        if (userPasswordModel == null || !userPasswordModel.getErrCode().equals(0)) {
            LOGGER.info(" u :[{}] h:[{}] check login finish cost [{}] ms", param.getU(), param.getH(), System.currentTimeMillis() - start);
            return JsonResultUtils.fail(userPasswordModel.getErrCode(), "Authentication failed");
        }
        UserCheckTokenModel userCheckTokenModel = new UserCheckTokenModel();
        userCheckTokenModel.setU(userPasswordModel.getUserID());
        UserCheckTokenModel.token token = new UserCheckTokenModel.token();
        token.setMk(param.getMk());
        token.setToken(userPasswordModel.getToken());
        userCheckTokenModel.setT(JacksonUtils.obj2String(token));
        userCheckTokenModel.setH(param.getH());
        LOGGER.info("u :[{}] h:[{}] check login finish cost [{}] ms", param.getU(), param.getH(), System.currentTimeMillis() - start);
        return JsonResultUtils.success(userCheckTokenModel);
    }

    @ResponseBody
    @RequestMapping(value = "/corp/newapi/auth/checktoken.qunar", method = RequestMethod.POST)
    public JsonResult<?> checkLogin(@RequestBody UserCheckTokenModel param) {
        LOGGER.info("check user token param is [{}]", JacksonUtils.obj2String(param));
        long start = System.currentTimeMillis();
        if (param == null || Strings.isNullOrEmpty(param.getT()) || Strings.isNullOrEmpty(param.getU()) || Strings.isNullOrEmpty(param.getH())) {
            return JsonResultUtils.fail(1, "Authentication failed");
        }
        UserCheckTokenModel.token token = null;
        try {
            token = JacksonUtils.string2Obj(param.getT(), UserCheckTokenModel.token.class);
        } catch (Exception e) {
            LOGGER.error("check user token parse token fail token is [{}]", param.getT(), e);
            return JsonResultUtils.fail(1, "Authentication failed");
        }
        if (iUserLogin.checkUserToken(param.getU(), param.getH(), token.getToken(), token.getMk())) {
            LOGGER.info("check user token u :[{}] h:[{}] token:[{}] success cost [{}] ms ", param.getU(), param.getH(), param.getT(), System.currentTimeMillis() - start);
            return JsonResultUtils.success();
        }
        LOGGER.info("check user token u :[{}] h:[{}] token:[{}] fail cost [{}] ms ", param.getU(), param.getH(), param.getT(), System.currentTimeMillis() - start);
        return JsonResultUtils.fail(1, "Authentication failed");
    }

    public static void main(String[] args) throws Exception {
        String rsaPublic = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCy2VXDAlCZlj7gPHvC/vwvbpTN/GyW0tmNCqh0UPitdTTGZk3UcLqu9lWMGPViL/5lhboiSogsDxJLHdwo91DDBjTX1HbuyuOhvsvayV7Yc8t+ajFW/8RwlvhGSzVplthoU+md9kGeZ8t73VWWZUEB0iyWx7Y/RjUwTdnOlNXDzQIDAQAB";
        String u = "admin";
        String p = "12345678";

        String encode = RSAEncrypt.encrypt(p, rsaPublic);
        String baseEncode = Base64Utils.encodeToString(encode.getBytes());
        System.out.println("encode entiy is " + baseEncode);
        String originPassword = "testpassword";
        String salt = "qtalkadmin_pwd_salt_d2bf42081aab47f4ac00697d7dd32993";
        String md5Step1 = Md5Utils.md5Encode(originPassword);
        String md5Step2 = Md5Utils.md5Encode(md5Step1 + salt);
        String md5Step3 = Md5Utils.md5Encode(md5Step2);
        System.out.println(md5Step3);
    }

}
