package com.qunar.qchat.dao.model;

/**
 * Created by admin on 17/07/2017.
 */
public class QGetMsg {

    private String m_from;
    private String m_to;
    private String m_body;
    private String from_host;
    private String to_host;
    private String create_time;
    private long read_flag;
    private Double date_part;

    public void setM_from(String m_from) {
        this.m_from = m_from;
    }

    public String getM_from () {
        return this.m_from;
    }

    public void setM_to(String m_to) {
        this.m_to = m_to;
    }

    public String getM_to () {
        return this.m_to;
    }

    public void setFrom_host(String from_host) {
        this.from_host= from_host;
    }

    public String getFrom_host () {
        return this.from_host;
    }

    public void setTo_host(String to_host) {
        this.to_host = to_host;
    }

    public String getTo_host () {
        return this.to_host;
    }

    public void setM_body(String m_body) {
        this.m_body = m_body;
    }

    public String getM_body () {
        return this.m_body;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_time () {
        return this.create_time;
    }

    public long getRead_flag() {
        return read_flag;
    }

    public void setRead_flag(long read_flag) {  this.read_flag = read_flag;    }


    public double getDate_part(){
        return this.date_part;
    }
    public void setDate_part(Double date_part){
        this.date_part = date_part;
    }

}
