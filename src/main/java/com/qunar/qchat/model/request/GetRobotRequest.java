package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 13:46
 */
@Data
@ToString
public class GetRobotRequest {
    private String robot_name;
    private String version;
}
