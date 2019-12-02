package com.qunar.qchat.model.ExceptionModel;

/**
 * MyHttException
 *
 * @author binz.zhang
 * @date 2019/2/12
 */
public class MyHttException extends Exception {
    public MyHttException() {
    }

    public MyHttException(String message) {
        super(message);
    }

    public MyHttException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyHttException(Throwable cause) {
        super(cause);
    }

    public MyHttException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
