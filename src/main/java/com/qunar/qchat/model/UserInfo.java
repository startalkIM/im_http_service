package com.qunar.qchat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qunar.qchat.constants.Config;

public class UserInfo {
    private String user_id;
    private String user_name;
    private String gender;
    private String mood;
    private String url;

    @JsonIgnore
    private String dep1;
    @JsonIgnore
    private String user_type;


    public UserInfo(String user_id, String dep1, String user_type) {
        this.user_id = user_id;
        this.dep1 = dep1;
        this.user_type = user_type;
    }

    public String getDep1() {
        return dep1;
    }

    public UserInfo(String user_id, String user_name, Integer gender, String mood, String url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.gender = gender.toString();
        this.mood = mood;
        this.url = url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMood() {
        return mood == null ? "" : mood;
    }

    public void setMood(String mood) {
        this.mood = (mood == null) ? "" : mood;
    }

    public String getUser_type() {
        return user_type;
    }
    public String getUrl() {
        if (url == null) {
            if (this.gender.equals("2")) {
                return Config.getProperty("famalePhoto");
            } else {
                return Config.getProperty("malePhoto");
            }
        } else {
            return url;
        }
    }

    public void setUrl(String url) {
        this.url = (url == null) ? "" : url;
    }

}
