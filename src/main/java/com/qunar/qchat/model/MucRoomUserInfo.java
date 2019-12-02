package com.qunar.qchat.model;

/**
 * MucRoomUserInfo
 *
 * @author botaom.ma
 * @date 2019/8/13
 */
public class MucRoomUserInfo {
    public String mucName;
    public String domain;
    public String username;
    public String host;

    public String getMucName() {
        return mucName;
    }

    public void setMucName(String mucName) {
        this.mucName = mucName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
