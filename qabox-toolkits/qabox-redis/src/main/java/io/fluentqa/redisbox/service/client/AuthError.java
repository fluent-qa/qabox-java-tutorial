package io.fluentqa.redisbox.service.client;

public class AuthError extends Exception {
    public AuthError(String message) {
        super(message);
    }
}
