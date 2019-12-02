package com.qunar.qchat.dao.model;

import lombok.*;

import java.util.Date;

/**
 * 用户勋章
 * @auth dongzd.zhang
 * @Date 2018/12/3 20:20
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HostUserDecorationModel {

    private Integer id;
    private String userId;
    private String host;
    private String type;
    private String url;
    private String urlDesc;
    private String provider;
    private Date createTime;
    private Date updateTime;

}
