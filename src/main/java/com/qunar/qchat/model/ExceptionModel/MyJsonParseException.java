package com.qunar.qchat.model.ExceptionModel;

/**
 * MyJsonParseException
 *
 * @author binz.zhang
 * @date 2019/2/12
 */
public class MyJsonParseException extends Exception {

    public MyJsonParseException() {
    }

    public MyJsonParseException(String message) {
        super(message);
    }

    public MyJsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyJsonParseException(Throwable cause) {
        super(cause);
    }

    public MyJsonParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
