package com.qunar.qchat.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/5 18:08
 */
public class GetVCardInfoResult {

    private String type;
    private String loginName;
    private String email;
    private String gender;
    private String nickname;
    private String webname;
    @JsonProperty("V")
    private String V;
    private String imageurl;
    private String uid;
    private String username;
    private String domain;
    private String commenturl;
    private String mood;
    private Boolean adminFlag;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCommenturl() {
        return commenturl;
    }

    public void setCommenturl(String commenturl) {
        this.commenturl = commenturl;
    }

    public String getWebname() {
        return webname;
    }

    public void setWebname(String webname) {
        this.webname = webname;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Boolean getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    @Override
    public String toString() {
        return "GetVCardInfoResult{" +
                "type='" + type + '\'' +
                ", loginName='" + loginName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", nickname='" + nickname + '\'' +
                ", webname='" + webname + '\'' +
                ", V='" + V + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", domain='" + domain + '\'' +
                ", commenturl='" + commenturl + '\'' +
                ", mood='" + mood + '\'' +
                ", adminFlag=" + adminFlag +
                '}';
    }
}
