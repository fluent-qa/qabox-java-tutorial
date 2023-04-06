package io.fluent.qabox.util;


public class TimeRecorder {

    private Long current;

    public TimeRecorder() {
        this.current = System.currentTimeMillis();
    }

    public synchronized long recorder() {
        try {
            return System.currentTimeMillis() - this.current;
        } finally {
            this.current = System.currentTimeMillis();
        }
    }

}
