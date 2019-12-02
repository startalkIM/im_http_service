package com.qunar.qchat.dao.model;

/**
 * Created by admin on 17/07/2017.
 */
public class QCloudSubHistory {

    private long qh_id;
    private long qs_id;
    private String qh_content;
    private long qh_time;
    private int qh_state;

    public long getQh_id() {
        return qh_id;
    }

    public void setQh_id(long qh_id) {
        this.qh_id = qh_id;
    }

    public long getQs_id() {
        return qs_id;
    }

    public void setQs_id(long qs_id) {
        this.qs_id = qs_id;
    }

    public String getQh_content() {
        return qh_content;
    }

    public void setQh_content(String qh_content) {
        this.qh_content = qh_content;
    }

    public long getQh_time() {
        return qh_time;
    }

    public void setQh_time(long qh_time) {
        this.qh_time = qh_time;
    }

    public int getQh_state() {
        return qh_state;
    }

    public void setQh_state(int qh_state) {
        this.qh_state = qh_state;
    }
}
