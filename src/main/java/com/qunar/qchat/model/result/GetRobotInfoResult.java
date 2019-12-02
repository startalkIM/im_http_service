package com.qunar.qchat.model.result;

import lombok.*;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 14:31
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetRobotInfoResult {

    private String rbt_name;
    private String rbt_body;
    private String rbt_ver;

}
