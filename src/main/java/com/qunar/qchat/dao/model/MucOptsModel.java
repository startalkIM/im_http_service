package com.qunar.qchat.dao.model;

/**
 * @auth jingyu.he
 */

public class MucOptsModel {

    private String mucName;
    private String opts;

    public String getMucName() {
        return mucName;
    }

    public void setMucName(String mucName) {
        this.mucName = mucName;
    }

    public String getOpt() {
        return opts;
    }

    public void setOpt(String opt) {
        this.opts = opt;
    }

    @Override
    public String toString() {
        return "MucInfoModel{" +
                "mucName='" + mucName + '\'' +
                ", opts='" + opts + '\'' +
                '}';
    }
}
