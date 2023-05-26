package io.fluentqa.api.exceptions;


import io.fluentqa.api.dto.ApiCode;

/**
 * 登录了但无权限异常
 *
 */
public class NoPermissionException extends BaseException {
    private static final long serialVersionUID = 1L;

    public NoPermissionException() {
        super(ApiCode.NO_PERMISSION.getCode(), ApiCode.NO_PERMISSION.getMessage());
    }

    public NoPermissionException(Throwable throwable) {
        super(ApiCode.NO_PERMISSION.getCode(), throwable);
    }

    public NoPermissionException(String msg, Throwable throwable) {
        super(ApiCode.NO_PERMISSION.getCode(), msg, throwable);
    }

    public NoPermissionException(String msg) {
        super(ApiCode.NO_PERMISSION.getCode(), msg);
    }
}
