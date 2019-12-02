package com.qunar.qchat.dao.model;

/**
 * Created by admin on 17/07/2017.
 */
public class GetRblMsg implements Comparable<GetRblMsg>{

    private String user;
    private String xmlBody;
    private Double time;
    private String cnt;
    private String create_time;
    private String mFlag;
    private String host;


    public void setUser(String user){
        this.user = user;
    }

    public String getUser(){
        return this.user;
    }

    public String getXmlBody(){
        return this.xmlBody;
    }

    public void setXmlBody(String xmlBody){
        this.xmlBody = xmlBody;
    }


    public void setTime(Double time) {
        this.time = time;
    }

    private Double getTime () {
        return this.time;
    }

    public String getCnt() {
        return this.cnt;
    }

    public void setCnt(String cnt){
        this.cnt = cnt;
    }

    public String getmFlag(){
        return this.mFlag;
    }

    public void setmFlag(String mFlag){
        this.mFlag = mFlag;
    }

    public void setCreate_time(String create_time){
        this.create_time = create_time;
    }
    public String getCreate_time(){
        return this.create_time;
    }

    public String getHost(){
        return this.host;
    }
    public void setHost(String host){
        this.host = host;
    }

    @Override
    public int compareTo(GetRblMsg getRblMsg){
        return getRblMsg.getTime().compareTo(this.time);
    }

    @Override
    public int hashCode(){
        return this.xmlBody.hashCode();
    }

    public boolean equals(Object obj){
        if(obj instanceof GetRblMsg){
            GetRblMsg getRblMsg=(GetRblMsg)obj;
            return xmlBody.equals(getRblMsg.xmlBody);
        }
        return super.equals(obj);
    }


}
