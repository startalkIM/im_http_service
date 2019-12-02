package com.qunar.qchat.model.request;

/**
 * @auth dongzd.zhang
 * @Date 2018/10/31 16:48
 */
public class GetDepsRequest {

    private String v;
    private String p;
    /*private String u;
    private String k;
    private String d;*/

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    /*public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }*/

    @Override
    public String toString() {
        return "GetDepsRequest{" +
                "v='" + v + '\'' +
                ", p='" + p + '\'' +
                '}';
    }
}
