package com.qunar.qchat.model.result;

import lombok.Data;
import lombok.ToString;

/**
 * @auth dongzd.zhang
 * @Date 2019/2/25 15:55
 */
@Data
@ToString
public class GetQTalkVcardResult {

    private Boolean ret;
    private Integer errcode;
    private String errmsg;

    private GetQTalkVcardResult.Body body;

    @Data
    @ToString
    public static class Body {
        private String id;
        private String username;
        private String nickname;
        private Integer version;
        private String url;
        private String uin;
        private Integer profileVersion;
        private String mood;
        private Integer gender;
        private String host;
    }
}
