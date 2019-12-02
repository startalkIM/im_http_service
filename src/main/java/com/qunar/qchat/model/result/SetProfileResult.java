package com.qunar.qchat.model.result;

import lombok.*;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/17 14:43
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SetProfileResult {

    private String user;
    private String domain;
    private String version;
    private String mood;
    private String url;
}
