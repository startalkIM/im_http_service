package com.qunar.qchat.dao.model;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 20:41
 */
public class RobotPubSubModel {

    /*rbt_name  | text | not null
    user_name | text | not null
    user_host | text | default 'ejabhost1'::text
    rbt_host  | text | default 'ejabhost1'::text*/

    private String rbtName;
    private String userName;
    private String userHost;
    private String rbtHost;

    public String getRbtName() {
        return rbtName;
    }

    public void setRbtName(String rbtName) {
        this.rbtName = rbtName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHost() {
        return userHost;
    }

    public void setUserHost(String userHost) {
        this.userHost = userHost;
    }

    public String getRbtHost() {
        return rbtHost;
    }

    public void setRbtHost(String rbtHost) {
        this.rbtHost = rbtHost;
    }

    @Override
    public String toString() {
        return "RobotPubSubModel{" +
                "rbtName='" + rbtName + '\'' +
                ", userName='" + userName + '\'' +
                ", userHost='" + userHost + '\'' +
                ", rbtHost='" + rbtHost + '\'' +
                '}';
    }
}
