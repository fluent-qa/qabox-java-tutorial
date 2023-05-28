package io.fluentqa.redisbox.service.client.debug.result;


import io.fluentqa.redisbox.service.protocol.RespType;

public class EndResult extends DebugResult {
    private final RespType returnedResult;

    public EndResult(RespType returnedResult) {
        this.returnedResult = returnedResult;
    }

    public RespType getReturnedResult() {
        return returnedResult;
    }

    @Override
    public String toString() {
        return "EndResult{" +
                "returnedResult=" + returnedResult +
                '}';
    }
}