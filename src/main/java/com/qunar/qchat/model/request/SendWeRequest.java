package com.qunar.qchat.model.request;

public class SendWeRequest {
    private String from;
    private String to;
    private String realjid;
    private String chattype;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public String getRealjid() {
        return realjid;
    }

    public void setRealjid(String realjid) {
        this.realjid = realjid;
    }

    public String getChattype() {
        return chattype;
    }

    public void setChattype(String chattype) {
        this.chattype = chattype;
    }
}
