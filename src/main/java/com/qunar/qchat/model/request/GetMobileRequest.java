package com.qunar.qchat.model.request;

import lombok.Data;

@Data
public class GetMobileRequest {
    private String qtalk_id;  //被查人ID
    private String user_id;   //查询人ID
    private String platform; //客户端类型
}
