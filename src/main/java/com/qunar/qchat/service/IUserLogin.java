package com.qunar.qchat.service;

import com.qunar.qchat.dao.model.UserPasswordModel;
import com.qunar.qchat.dao.model.UserPasswordRO;
import org.springframework.stereotype.Service;

@Service
public interface IUserLogin {


    /**
     * 验证用户的登录密码
     *
     * @param userInput 登录用户名输入可以是userID、手机号、邮箱
     * @return
     */
    UserPasswordModel checkUserLogin(UserPasswordRO userInput);

    /**
     * 产生此次用户登录的token
     *
     * @param userID
     * @return
     */
    String buildLoginToken(String userID, String host,String mk);

    /**
     * 校验用户token
     *
     * @return
     */
    boolean checkUserToken(String userId, String host, String token,String mk);


    /**
     * 明文密码加密接口，可根据自己的需求进行不同方式的加密
     *
     * @param originPassword
     * @return
     */
    String generatePassword(String originPassword, String salt);

    /**
     * 密码解密
     *
     * @param originPass
     * @return
     */
    String decodePassword(String originPass);

    /**
     * 对吧数据库中密码跟用户传递过来的密码
     *
     * @param cleartext_pwd
     * @param passWd_db
     * @param salt
     * @return
     */
    boolean checkPassword(String cleartext_pwd, String passWd_db, String salt);

}
