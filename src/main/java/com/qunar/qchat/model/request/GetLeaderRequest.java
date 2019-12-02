package com.qunar.qchat.model.request;

import lombok.Data;

@Data
public class GetLeaderRequest {
    private String user_id; //用户ID
    private String qtalk_id; //Qt ID
    private String platform;
    private String ckey;
}
