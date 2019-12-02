package com.qunar.qchat.dao.model;

/**
 * Created by admin on 17/07/2017.
 */
public class QGetMucMsg implements Comparable<QGetMucMsg> {

    private String muc_room_name;
    private String nick;
    private String host;
    private String create_time;
    private String packet;
    private Double date_part;

    public void setMuc_room_name(String muc_room_name) {
        this.muc_room_name = muc_room_name;
    }

    public String getMuc_room_name () {
        return this.muc_room_name;
    }

    public void setNick(String nick) {        this.nick = nick;    }

    public String getNick()    {        return this.nick;    }

    public String getHost () {
        return this.host;
    }

    public void setHost(String setHost) {        this.host= setHost;    }

    public String getPacket () {
        return this.packet;
    }

    public void setPacket(String packet) {
        this.packet = packet;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_time () {
        return this.create_time;
    }

    public double getDate_part(){
        return this.date_part;
    }
    public void setDate_part(Double date_part){
        this.date_part = date_part;
    }

    @Override
    public int compareTo(QGetMucMsg o) {
        return this.date_part.compareTo(o.getDate_part());
    }

    @Override
    public int hashCode(){
        return this.packet.hashCode();
    }

    public boolean equals(Object obj){
        if(obj instanceof QGetMucMsg){
            QGetMucMsg qGetMucMsg=(QGetMucMsg)obj;
            return packet.equals(qGetMucMsg.packet);
        }
        return super.equals(obj);
    }
}
