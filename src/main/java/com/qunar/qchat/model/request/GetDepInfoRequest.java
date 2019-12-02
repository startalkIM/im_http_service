package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/16 17:03
 */
@Data
@ToString
public class GetDepInfoRequest {

    //strid=shop_323@ejabhost1&u=nigotuu7479@ejabhost2&k=djjdj
    private String strid;
    private String u;
    private String k;

    public String getStrid() {
        return strid;
    }

    public void setStrid(String strid) {
        this.strid = strid;
    }

    public String getU() {
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
}
