package com.qunar.qchat.dao.model;

import java.util.Date;

/**
 * @auth dongzd.zhang
 * @Date 2019/5/22 11:52
 */
public class FloginUserModel {

    private String userName;
    private Date createTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "FloginUserModel{" +
                "userName='" + userName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
