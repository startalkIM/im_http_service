package com.qunar.qchat.service.impl;

import com.google.common.base.Strings;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.dao.IUserInfo;
import com.qunar.qchat.dao.model.UserPasswordModel;
import com.qunar.qchat.dao.model.UserPasswordRO;
import com.qunar.qchat.service.IUserLogin;
import com.qunar.qchat.utils.Md5Utils;
import com.qunar.qchat.utils.RSAEncrypt;
import com.qunar.qchat.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IUserLoginService implements IUserLogin {
    private static final Logger LOGGER = LoggerFactory.getLogger(IUserLoginService.class);
    private static final String PASSWORD_SIGN = "p";
    private static final String USERID_SIGN = "u";
    private static final String HOST_SIGN = "h";
    private static final String MK = "mk";

    private static final char SPLITTER_USERID = '\0';
    private static final String LOGIN_SUFFIX = "login_service";

    private static final String TOKEN_KEY_FORMAT = "%s:%s:%s";// login_service:binz.zhang@ejabhost1:mk
    @Resource
    private IUserInfo iUserInfo;
    @Autowired
    private RedisUtil redisUtil;

    private static String RSA_PRIVATE = Config.getProperty("rsa_private_key");

    @Override
    public UserPasswordModel checkUserLogin(UserPasswordRO userInput) {
        UserPasswordModel userPasswordModel = new UserPasswordModel();
        String decodeUserLogin = decodePassword(userInput.getP()); //解密密码
        if (Strings.isNullOrEmpty(decodeUserLogin)) {
            userPasswordModel.setErrCode(1);
            LOGGER.info("user [{}],h:[{}] auth fail due to decode password fail", userInput.getU(), userInput.getH());
            return userPasswordModel;
        }
        String userID = userInput.getU();
        UserPasswordModel passwordDB = iUserInfo.getUserPassword(userID, userInput.getH());
        if (passwordDB == null) {
            userPasswordModel.setErrCode(2);
            LOGGER.warn("can not find user info from the db user [{}],h:[{}]", userInput.getU(), userInput.getH());
            return userPasswordModel;
        }
        if (checkPassword(decodeUserLogin, passwordDB.getPasswd(), passwordDB.getPasswdSalt())) {
            passwordDB.setToken(buildLoginToken(passwordDB.getUserID(), userInput.getH(), userInput.getMk()));
            userPasswordModel = passwordDB;
            userPasswordModel.setErrCode(0);
            LOGGER.info("login success user [{}],h:[{}]", userInput.getU(), userInput.getH());
            return userPasswordModel;
        }
        LOGGER.info("user [{}],h:[{}] auth fail due to password error", userInput.getU(), userInput.getH());
        userPasswordModel.setErrCode(3);
        return userPasswordModel;
    }

    @Override
    public String buildLoginToken(String userID, String host, String mk) {
        String key = buildLogInTokenKey(userID, host, mk);  //key的形式是 login_service:binz.zhang@ejabhost1:mk
        StringBuilder tokenSB = new StringBuilder();
        //token 生成规则 Md5(uuid + Mk)
        String tokenBuild = Md5Utils.md5Encode(tokenSB.append(UUID.randomUUID().toString().replaceAll("-", "")).append(mk).append(mk).toString());
        redisUtil.set(2, key, tokenBuild, 7, TimeUnit.DAYS);
        return tokenBuild;
    }

    @Override
    public boolean checkUserToken(String userId, String host, String token, String mk) {

        String key = buildLogInTokenKey(userId, host, mk);  //key的形式是 login_service:binz.zhang@ejabhost1:mk

        Integer hostId = iUserInfo.getHostInfo(host);
        Integer hireFlag = iUserInfo.getUserHireFlag(userId, hostId);
        if (hireFlag == null || !hireFlag.equals(1)) {
            LOGGER.info("user {} host {} no user in db ", userId, host);
            return false;
        }

        String tokenRedis = redisUtil.get(2, key, String.class);
        if (!Strings.isNullOrEmpty(tokenRedis) && tokenRedis.equals(token)) {
            redisUtil.delete(2, key);
            redisUtil.set(2, key, tokenRedis, 7, TimeUnit.DAYS);
            return true;
        }
        return false;
    }

    @Override
    public String generatePassword(String originPassword, String salt) {
        String md5Step1 = Md5Utils.md5Encode(originPassword);
        String md5Step2 = Md5Utils.md5Encode(md5Step1 + salt);
        String md5Step3 = Md5Utils.md5Encode(md5Step2);
        return "CRY:" + md5Step3;
    }

    @Override
    public String decodePassword(String encodePassword) {
        if (Strings.isNullOrEmpty(encodePassword)) {
            LOGGER.warn("encodePassword is empty");
            return null;
        }
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] baseDecode = decoder.decodeBuffer(encodePassword);
            return RSAEncrypt.decrypt(baseDecode, RSA_PRIVATE);
        } catch (Exception e) {
            LOGGER.error("decode password error encode password {}", encodePassword, e);
            return null;
        }
    }

    public static void main(String[] args) {
        String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALLZVcMCUJmWPuA8e8L+/C9ulM38bJbS2Y0KqHRQ+K11NMZmTdRwuq72VYwY9WIv/mWFuiJKiCwPEksd3Cj3UMMGNNfUdu7K46G+y9rJXthzy35qMVb/xHCW+EZLNWmW2GhT6Z32QZ5ny3vdVZZlQQHSLJbHtj9GNTBN2c6U1cPNAgMBAAECgYAeUaWuR2gugT/rd5Vrexp5V/+148Ls1pW2yUXBYjCmByaJM7Kh/vJG0s+xzlFa8dPolgD16zimb2+keE1oTHTOMmS4Bb0QItvh1kDVvqy1DLuceXt2BPXdGwSMokjcJguYrYyd6Kz105vPPQKoyB1sADjnrNDhW4yXAlnN/QFPwQJBANaKlvEx7LS2f7UjiUv7Ulh+t9BmgD52/R3xfaC3R3yK\"0kU9BhawkK3i9SXjxznwT6FZqsIGX7KXjKbPtGW2bCkCQQDVaQi/BaULKWJSmAztXSuny/EE/JNeo1nr7GKpWi1fY8hDXXp81VblPHkOgDFD9z5AAs5lcCJjsGIBn4AyXU8FAkBloBiIACIkKB6uazrqJw6GpN/lc+hjrnGP8YiUzLysHgYkjheIP/MIq218mT0SEOdngtYEOoiyTF9v1Qua8qKhAkB+jrBaH+3VZbBiTLt11Ef8VUxUabi3aeX8rA2CYvD/Xbw4fuoRt661eRxNRiZxKOFosoFV1J8AQWyNi9pJg95FAkEA0uTiMI41VCRjNKae+pSsW8M4Wr0SLknTzBp68UihqJokn068yDQC55bAej+R83PUaQJXp17iYpOyGBPl3ZXRPg==";
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] baseDecode = decoder.decodeBuffer("hWTQsK6I8cgZgjplTaow65HMUpps3q858Jx1yk6/8CRdHQHAtsihFeVZDKabGfnTvfejviz+281JcUn318cpA6xodHgrKy62ozefH0JSpK208JjFiJC0raAEMv0+VFjJ90Es6XOCLVgfa8z79hImD+jSL0YvjMxyVasV2NIVkg4=");
            String pwd =  RSAEncrypt.decrypt(baseDecode, private_key);
            System.out.println(pwd);
        } catch (Exception e) {
            LOGGER.error("decode password error encode password {}", e);
        }

    }

    @Override
    public boolean checkPassword(String cleartext_pwd, String passWd_db, String salt) {
        if (Strings.isNullOrEmpty(cleartext_pwd) || Strings.isNullOrEmpty(passWd_db)) {
            return false;
        }
        if (passWd_db.startsWith("CRY:")) {
            return generatePassword(cleartext_pwd, salt).equals(passWd_db);
        }
        return passWd_db.equals(cleartext_pwd);
    }


    private String buildLogInTokenKey(String userId, String host, String mk) {
        StringBuilder userInfo = new StringBuilder(userId).append("@").append(host);
        return String.format(TOKEN_KEY_FORMAT, LOGIN_SUFFIX, userInfo.toString(), mk);

    }

}
