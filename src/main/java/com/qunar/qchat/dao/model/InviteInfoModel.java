package com.qunar.qchat.dao.model;

/**
 * create by hubo.hu (lex) at 2018/6/14
 */
public class InviteInfoModel {

    /**
     * inviter : xxx
     * body : xxx
     * timestamp : xxx
     */
    private String inviter;
    private String ihost;
    private String body;
    private String timestamp;

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setIhost(String ihost) {
        this.ihost = ihost;
    }

    public String getIhost() {
        return ihost;
    }

    public String getInviter() {
        return inviter;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
