package com.qunar.qchat.dao.model;

import lombok.Data;

@Data
public class UserPasswordModel {
    private String userID;
    private String passwd;
    private String passwdSalt;
    private Integer host;
    private String token;
    private Integer errCode; //验证结果 errCode=0 验证通过; errCode =1 传递信息缺失; errCode =2 用户不存在;errCode = 3 账号或密码错误
}
