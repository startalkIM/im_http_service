package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/16 10:54
 */
@Data
@ToString
public class GetUserStatusRequest {
    private List<String> users;
}
