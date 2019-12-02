package com.qunar.qchat.dao.model;

import java.util.Date;

public class MucIncrementInfo {
    private String muc_name;
    private String domain;
    private Long t;
    private int registed_flag;
    private Date created_at;

    public String getMuc_name() {
        return muc_name;
    }

    public void setMuc_name(String muc_name) {
        this.muc_name = muc_name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public Long getT() {
        return t;
    }

    public int getRegisted_flag() {
        return registed_flag;
    }

    public void setRegisted_flag(int registed_flag) {
        this.registed_flag = registed_flag;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
