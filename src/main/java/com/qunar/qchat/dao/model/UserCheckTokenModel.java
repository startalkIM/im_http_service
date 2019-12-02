package com.qunar.qchat.dao.model;

import lombok.Data;

@Data
public class UserCheckTokenModel {
    private String u;
    private String t;
    private String h;
    @Data
    public static class token {
        private String token;
        private String mk;
    }
}
