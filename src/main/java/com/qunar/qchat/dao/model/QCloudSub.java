package com.qunar.qchat.dao.model;

/**
 * Created by admin on 14/07/2017.
 */
public class QCloudSub {

    private String cs_id;
    private long q_id;
    private long qs_id;
    private String qs_user;
    private int qs_type;
    private String qs_title;
    private String qs_introduce;
    private String qs_content;
    private long qs_time;
    private int qs_state;

    public long getQ_id() {
        return q_id;
    }

    public void setQ_id(long q_id) {
        this.q_id = q_id;
    }

    public long getQs_id() {
        return qs_id;
    }

    public void setQs_id(long qs_id) {
        this.qs_id = qs_id;
    }

    public String getQs_user() {
        return qs_user;
    }

    public void setQs_user(String qs_user) {
        this.qs_user = qs_user;
    }

    public int getQs_type() {
        return qs_type;
    }

    public void setQs_type(int qs_type) {
        this.qs_type = qs_type;
    }

    public String getQs_title() {
        return qs_title;
    }

    public void setQs_title(String qs_title) {
        this.qs_title = qs_title;
    }

    public String getQs_content() {
        return qs_content;
    }

    public void setQs_content(String qs_content) {
        this.qs_content = qs_content;
    }

    public long getQs_time() {
        return qs_time;
    }

    public void setQs_time(long qs_time) {
        this.qs_time = qs_time;
    }

    public String getQs_introduce() {
        return qs_introduce;
    }

    public void setQs_introduce(String qs_introduce) {
        this.qs_introduce = qs_introduce;
    }

    public int getQs_state() {
        return qs_state;
    }

    public void setQs_state(int qs_state) {
        this.qs_state = qs_state;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }
}
