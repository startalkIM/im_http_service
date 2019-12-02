package com.qunar.qchat.model.request;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 20:25
 */
public class RobotSubRequest {

    private String method;
    private String user;
    private String rbt;
    private String uhost;
    private String rhost;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRbt() {
        return rbt;
    }

    public void setRbt(String rbt) {
        this.rbt = rbt;
    }

    public String getUhost() {
        return uhost;
    }

    public void setUhost(String uhost) {
        this.uhost = uhost;
    }

    public String getRhost() {
        return rhost;
    }

    public void setRhost(String rhost) {
        this.rhost = rhost;
    }

    @Override
    public String toString() {
        return "RobotSubRequest{" +
                "method='" + method + '\'' +
                ", user='" + user + '\'' +
                ", rbt='" + rbt + '\'' +
                ", uhost='" + uhost + '\'' +
                ", rhost='" + rhost + '\'' +
                '}';
    }
}
