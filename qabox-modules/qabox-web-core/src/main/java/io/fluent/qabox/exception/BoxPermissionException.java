package io.fluent.qabox.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoxPermissionException extends RuntimeException {

    private static final String NO_LEGAL_POWER = "权限不足，该操作将被记录!";

    public BoxPermissionException() {
        this(NO_LEGAL_POWER);
    }

    public BoxPermissionException(String message) {
        if (null == message) {
            throw new WebApiRuntimeException(NO_LEGAL_POWER);
        } else {
            throw new WebApiRuntimeException(message);
        }
    }
}
