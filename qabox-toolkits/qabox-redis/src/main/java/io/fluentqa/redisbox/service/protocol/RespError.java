package io.fluentqa.redisbox.service.protocol;

/**
 * RESP错误
 *
 */
public class RespError extends RespType {
    // 错误信息
    private String message;

    /**
     * 构造RESP错误
     *
     * @param message 错误信息
     */
    public RespError(String message) {
        this.message = message;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RespError{" +
                "message='" + message + '\'' +
                '}';
    }
}
