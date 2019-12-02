package com.qunar.qchat.model;

public class SendWhiteModel {

    private Integer id;
    private String appcode;
    private String fromUser;
    private String ownerUser;
    private Integer reviewFlag;
    private String[] ip;

    public Integer getId() {
        return id;
    }

    public String getAppcode() {
        return appcode;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public Integer getReviewFlag() {
        return reviewFlag;
    }

    public String[] getIp() {
        return ip;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setOwnerUser(String ownerUser) {
        this.ownerUser = ownerUser;
    }

    public void setReviewFlag(Integer reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public void setIp(String[] ip) {
        this.ip = ip;
    }
}
