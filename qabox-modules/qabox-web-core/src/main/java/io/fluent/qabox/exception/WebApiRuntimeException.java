package io.fluent.qabox.exception;

import lombok.Getter;

import java.util.Map;


@Getter
public class WebApiRuntimeException extends RuntimeException {

    private boolean printStackTrace;

    //异常信息扩展
    private Map<String, Object> extraMap;

    public WebApiRuntimeException(String message) {
        this(message, true);
    }

    public WebApiRuntimeException(String message, boolean printStackTrace) {
        super(message);
        this.printStackTrace = printStackTrace;
    }

    public WebApiRuntimeException(Map<String, Object> extraMap, boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
        this.extraMap = extraMap;
    }

    public WebApiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
