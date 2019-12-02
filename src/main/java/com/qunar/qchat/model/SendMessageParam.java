package com.qunar.qchat.model;

import com.qunar.qchat.utils.JacksonUtils;

import java.util.List;

public class SendMessageParam {

    private String system;
    private String from;
    private String fromhost;
    private List<ToEntity> to;
    private String extendinfo;
    private String type;
    private String msgtype;
    private String content;
    private String auto_reply = "false";
    private String backupinfo = "";


    public void setSystem(String system) {
        this.system = system;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFromhost(String fromhost) {
        this.fromhost = fromhost;
    }

    public void setTo(List<ToEntity> to) {
        this.to = to;
    }

    public void setExtendinfo(String extendinfo) {
        this.extendinfo = extendinfo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuto_reply(String auto_reply) {
        this.auto_reply = auto_reply;
    }

    public String getAuto_reply() {
        return auto_reply;
    }

    public String getBackupinfo() {
        return backupinfo;
    }

    public String getSystem() {
        return system;
    }

    public String getFrom() {
        return from;
    }

    public String getFromhost() {
        return fromhost;
    }

    public List<ToEntity> getTo() {
        return to;
    }

    public String getExtendinfo() {
        return extendinfo;
    }

    public String getType() {
        return type;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public String getContent() {
        return content;
    }

    public void setBackupinfo(Object backupinfo) {
        this.backupinfo = JacksonUtils.obj2String(backupinfo);
    }

    public static class ToEntity {
        private String host;
        private String user;

        public void setHost(String host) {
            this.host = host;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getHost() {
            return host;
        }

        public String getUser() {
            return user;
        }
    }

}
