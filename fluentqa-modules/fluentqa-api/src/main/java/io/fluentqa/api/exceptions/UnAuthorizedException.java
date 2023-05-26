package io.fluentqa.api.exceptions;


import io.fluentqa.api.dto.ApiCode;

/**
 * 未认证 未登陆异常
 *
 */
public class UnAuthorizedException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException() {
        super(ApiCode.UNAUTHORIZED.getCode(), ApiCode.UNAUTHORIZED.getMessage());
    }

    public UnAuthorizedException(Throwable throwable) {
        super(ApiCode.UNAUTHORIZED.getCode(), throwable);
    }

    public UnAuthorizedException(String msg, Throwable throwable) {
        super(ApiCode.UNAUTHORIZED.getCode(), msg, throwable);
    }

    public UnAuthorizedException(String msg) {
        super(ApiCode.UNAUTHORIZED.getCode(), msg);
    }
}
