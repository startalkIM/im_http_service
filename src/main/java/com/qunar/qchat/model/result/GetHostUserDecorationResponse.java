package com.qunar.qchat.model.result;

import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2018/12/4 12:20
 */
@Data
@ToString
public class GetHostUserDecorationResponse {

    //id, user_id, host, type, url
    //private Integer id;
    private String userId;
    private String host;
    private String type;
    private String url;
    private String desc;
    private String upt;

}
