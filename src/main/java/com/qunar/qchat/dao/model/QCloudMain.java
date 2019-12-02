package com.qunar.qchat.dao.model;

/**
 * Created by admin on 14/07/2017.
 */
public class QCloudMain {
    private long q_id;
    private String c_id;
    private String q_user;
    private int q_type;
    private String q_title;
    private String q_introduce;
    private String q_content;
    private long q_time;
    private int q_state;

    public long getQ_id() {
        return q_id;
    }

    public void setQ_id(long q_id) {
        this.q_id = q_id;
    }

    public String getQ_user() {
        return q_user;
    }

    public void setQ_user(String q_user) {
        this.q_user = q_user;
    }

    public int getQ_type() {
        return q_type;
    }

    public void setQ_type(int q_type) {
        this.q_type = q_type;
    }

    public String getQ_title() {
        return q_title;
    }

    public void setQ_title(String q_title) {
        this.q_title = q_title;
    }

    public String getQ_introduce() {
        return q_introduce;
    }

    public void setQ_introduce(String q_introduce) {
        this.q_introduce = q_introduce;
    }

    public String getQ_content() {
        return q_content;
    }

    public void setQ_content(String q_content) {
        this.q_content = q_content;
    }

    public long getQ_time() {
        return q_time;
    }

    public void setQ_time(long q_time) {
        this.q_time = q_time;
    }

    public int getQ_state() {
        return q_state;
    }

    public void setQ_state(int q_state) {
        this.q_state = q_state;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
}
