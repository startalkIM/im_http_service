package com.qunar.qchat.model.result;

import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 14:46
 */
@Data
@ToString
public class UpdateMucNickResult {
    private String muc_name;
    private String version;
    private String show_name;
    private String muc_title;
    private String muc_desc;
}
