package com.qunar.qchat.dao.model;

/**
 * Created by admin on 17/07/2017.
 */
public class QMucTime {

    private String muc_name;
    private String domain;
    private String date;

    public void  setMuc_name(String muc_name)
    {
        this.muc_name = muc_name;
    }

    public String  getMuc_name() {  return this.muc_name;  }

    public void  setDate(String date)
    {
        this.date = date;
    }

    public String  getDate()   {  return this.date;   }

    public String getDomain(){
        return this.domain;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    @Override
    public int hashCode(){
        return this.muc_name.hashCode() + this.domain.hashCode();
    }

    public boolean equals(Object obj){
        if(obj instanceof QMucTime){
            QMucTime qMucTime=(QMucTime)obj;
            return (muc_name.equals(qMucTime.muc_name) &&  domain.equals(qMucTime.domain));
        }
        return super.equals(obj);
    }
}
