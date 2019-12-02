package com.qunar.qchat.dao.model;

import java.util.Date;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 11:44
 */

public class MucInfoModel {

    private String mucName;
    private String showName;
    private String showNamePinyin;
    private String mucDesc;
    private String mucTitle;
    private String mucPic;
    private String version;
    private Date updateTime;

    public String getMucName() {
        return mucName;
    }

    public void setMucName(String mucName) {
        this.mucName = mucName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getMucDesc() {
        return mucDesc;
    }

    public void setMucDesc(String mucDesc) {
        this.mucDesc = mucDesc;
    }

    public String getMucTitle() {
        return mucTitle;
    }

    public void setMucTitle(String mucTitle) {
        this.mucTitle = mucTitle;
    }

    public String getMucPic() {
        return mucPic;
    }

    public void setMucPic(String mucPic) {
        this.mucPic = mucPic;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getShowNamePinyin() {
        return showNamePinyin;
    }

    public void setShowNamePinyin(String showNamePinyin) {
        this.showNamePinyin = showNamePinyin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MucInfoModel{" +
                "mucName='" + mucName + '\'' +
                ", showName='" + showName + '\'' +
                ", showNamePinyin='" + showNamePinyin + '\'' +
                ", mucDesc='" + mucDesc + '\'' +
                ", mucTitle='" + mucTitle + '\'' +
                ", mucPic='" + mucPic + '\'' +
                ", version='" + version + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
