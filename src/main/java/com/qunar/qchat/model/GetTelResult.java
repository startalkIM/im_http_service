package com.qunar.qchat.model;

import lombok.Data;

@Data
public class GetTelResult<T> {
    private int errcode;
    private String msg;
    private T data;

    public GetTelResult(int errcode, String msg, T data) {
        this.errcode = errcode;
        this.msg = msg;
        this.data = data;
    }

    public GetTelResult() {
    }

    @Data
    public static class Phone{
        private String phone;
    }
}
