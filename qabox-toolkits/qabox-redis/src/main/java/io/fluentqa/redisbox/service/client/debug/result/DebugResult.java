package io.fluentqa.redisbox.service.client.debug.result;

public abstract class DebugResult {
    private String additionalMessage;

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
