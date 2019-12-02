package com.qunar.qchat.model.request;

import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 15:41
 */
public class RegistRobotRequest implements IRequest {

    private String robotEnName;
    private String robotCnName;
    private String requestUrl;
    private String robotDesc;
    private String password;

    public String getRobotEnName() {
        return robotEnName;
    }

    public void setRobotEnName(String robotEnName) {
        this.robotEnName = robotEnName;
    }

    public String getRobotCnName() {
        return robotCnName;
    }

    public void setRobotCnName(String robotCnName) {
        this.robotCnName = robotCnName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRobotDesc() {
        return robotDesc;
    }

    public void setRobotDesc(String robotDesc) {
        this.robotDesc = robotDesc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegistRobotRequest{" +
                "robotEnName='" + robotEnName + '\'' +
                ", robotCnName='" + robotCnName + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", robotDesc='" + robotDesc + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean isRequestValid() {
        if( StringUtils.isBlank(this.getRobotEnName()) ||
                StringUtils.isBlank(this.getRobotCnName()) ||
                StringUtils.isBlank(this.getRequestUrl()) ||
                StringUtils.isBlank(this.getRobotDesc())) {
            return false;
        }
        return true;
    }
}
