package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 16:47
 */
@Data
@ToString
public class SearchRobotRequest {
    private String type;
    private String keyword;
}
