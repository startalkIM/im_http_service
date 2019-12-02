package com.qunar.qchat.model.result;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/21 20:21
 */
@Data
@Builder
@ToString
public class CheckPushKeyResult {
    private boolean validate;
    private String validateMsg;
}
