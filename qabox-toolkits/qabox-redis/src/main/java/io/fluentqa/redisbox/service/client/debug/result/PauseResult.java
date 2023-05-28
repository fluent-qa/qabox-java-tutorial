package io.fluentqa.redisbox.service.client.debug.result;


import io.fluentqa.redisbox.service.client.debug.PauseReason;

public class PauseResult extends DebugResult {
    private final int stoppedLineNo;
    private final PauseReason pauseReason;

    public PauseResult(int stoppedLineNo, PauseReason pauseReason) {
        this.stoppedLineNo = stoppedLineNo;
        this.pauseReason = pauseReason;
    }

    public int getStoppedLineNo() {
        return stoppedLineNo;
    }

    public PauseReason getPauseReason() {
        return pauseReason;
    }

    @Override
    public String toString() {
        return "PauseResult{" +
                "stoppedLineNo=" + stoppedLineNo +
                ", pauseReason=" + pauseReason +
                '}';
    }
}