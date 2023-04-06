package io.fluent.qabox.security.tl;


import io.fluent.qabox.security.model.ReqBody;

public class RequestBodyTL {

    private static final ThreadLocal<ReqBody> threadLocal = new ThreadLocal<>();

    public static ReqBody get() {
        return threadLocal.get();
    }

    public static void set(ReqBody reqBody) {
        threadLocal.set(reqBody);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
