package com.qunar.qchat.service;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/9 10:35
 */
public interface IUserRegistService {

    /**
     * 发送短信注册验证码.
     * @param tel
     * @return boolean
     * */
    boolean sendRegistSmsValidateCode(String tel, String domain);

    /**
     * 验证短息验证码有效性.
     * @param tel
     * @param code
     * @return boolean
     * */
    boolean checkSmsValidateCode(String tel, String code);

    /**
     * 用户注册.
     * @param tel 用户id
     * @param name
     * @param password
     * @return boolean
     * @param desc
     * @return boolean
     * */
    boolean regist(String tel, String name, String password, String domain, String desc);


    /**
     * 发送文件传输助手消息.
     * @param domain
     * @param toUser
     * @return boolean
     * */
    boolean sendFileTransforMsg(String domain, String toUser);
}
