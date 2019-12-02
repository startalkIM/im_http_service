package com.qunar.qchat.model.result;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 16:58
 */
public class SearchRobotResult {

    private String rbt_name;
    private String rbt_body;
    private String rbt_ver;

    public String getRbt_name() {
        return rbt_name;
    }

    public void setRbt_name(String rbt_name) {
        this.rbt_name = rbt_name;
    }

    public String getRbt_body() {
        return rbt_body;
    }

    public void setRbt_body(String rbt_body) {
        this.rbt_body = rbt_body;
    }

    public String getRbt_ver() {
        return rbt_ver;
    }

    public void setRbt_ver(String rbt_ver) {
        this.rbt_ver = rbt_ver;
    }

    @Override
    public String toString() {
        return "SearchRobotResult{" +
                "rbt_name='" + rbt_name + '\'' +
                ", rbt_body='" + rbt_body + '\'' +
                ", rbt_ver='" + rbt_ver + '\'' +
                '}';
    }
}
