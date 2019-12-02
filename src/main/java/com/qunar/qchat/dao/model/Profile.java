package com.qunar.qchat.dao.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String username;
    private String host;
    private int version;
    private String mood;
    private String url;
    private Integer gender;
}
