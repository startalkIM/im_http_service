package com.qunar.qchat.dao.model;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/5 17:55
 */
public class VCardInfoModel {

    private String id;
    private String username;
    private String nickname;
    private Integer version;
    private String url;
    private String uin;
    private Integer profileVersion;
    private String mood;
    private Integer gender;
    private String host;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public Integer getProfileVersion() {
        return profileVersion;
    }

    public void setProfileVersion(Integer profileVersion) {
        this.profileVersion = profileVersion;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "VCardInfoModel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", version=" + version +
                ", url='" + url + '\'' +
                ", uin='" + uin + '\'' +
                ", profileVersion=" + profileVersion +
                ", mood='" + mood + '\'' +
                ", gender=" + gender +
                ", host='" + host + '\'' +
                '}';
    }
}
