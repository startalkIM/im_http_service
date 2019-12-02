package com.qunar.qchat.dao.model;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 12:22
 */
public class RobotInfoModel {

   private String enName;
   private String cnName;
   private String requestUrl;
   private String rbtDesc;
   private String rbtBody;
   private Integer rbtVersion;
   private String password;
   private String recommend;

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRbtDesc() {
        return rbtDesc;
    }

    public void setRbtDesc(String rbtDesc) {
        this.rbtDesc = rbtDesc;
    }

    public String getRbtBody() {
        return rbtBody;
    }

    public void setRbtBody(String rbtBody) {
        this.rbtBody = rbtBody;
    }

    public Integer getRbtVersion() {
        return rbtVersion;
    }

    public void setRbtVersion(Integer rbtVersion) {
        this.rbtVersion = rbtVersion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "RobotInfoModel{" +
                "enName='" + enName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", rbtDesc='" + rbtDesc + '\'' +
                ", rbtBody='" + rbtBody + '\'' +
                ", rbtVersion='" + rbtVersion + '\'' +
                ", password='" + password + '\'' +
                ", recommend='" + recommend + '\'' +
                '}';
    }
}
