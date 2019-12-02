package com.qunar.qchat.model;

public class MucInfoParam {

    private String muc_name;
    private String nick;
    private String desc;
    private String title;
    private String pic;

    public String getMuc_name() {
        return muc_name;
    }

    public void setMuc_name(String muc_name) {
        this.muc_name = muc_name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "MucInfoParam{" +
                "muc_name='" + muc_name + '\'' +
                ", nick='" + nick + '\'' +
                ", desc='" + desc + '\'' +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
