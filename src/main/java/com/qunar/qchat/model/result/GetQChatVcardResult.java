package com.qunar.qchat.model.result;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2019/2/25 14:30
 */
@Data
@ToString
public class GetQChatVcardResult {

    private boolean ret;
    private String msg;
    private List<Body> data;


    @Data
    @ToString
    public static class Body{
        private int displaytype;
        private int gender;
        private String imageurl;
        private String loginName;
        private String mobile;
        private String nickname;
        private String webname;
        private int type;
        private Object[]  extentInfo;
        private String email;
        private String username;
    }
}
