package com.qunar.qchat.dao.model;

public class QMsgReadFlag {

    private String msg_id;
    private long read_flag;
    private long id;
    private Double update_time;


    public void setMsg_id(String msgid) {
        this.msg_id = msgid;
    }

    public String getMsg_id () {
        return this.msg_id;
    }

    public long getRead_flag() {
        return read_flag;
    }

    public void setRead_flag(long read_flag) {  this.read_flag = read_flag;    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setUpdate_time(Double update_time) {
        this.update_time = update_time;
    }

    public Double getUpdate_time() {
        return this.update_time;
    }
}
