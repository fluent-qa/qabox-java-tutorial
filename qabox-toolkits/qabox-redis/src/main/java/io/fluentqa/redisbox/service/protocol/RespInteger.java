package io.fluentqa.redisbox.service.protocol;

/**
 * RESP整数
 *
 * @author e1y4r
 */
public class RespInteger extends RespType {
    // 整数值
    private long value;

    /**
     * 初始化RESP整数
     *
     * @param value 整数值
     */
    public RespInteger(long value) {
        this.value = value;
    }

    /**
     * 获取整数的值
     *
     * @return 整数的值
     */
    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RespInteger{" +
                "value=" + value +
                '}';
    }
}
