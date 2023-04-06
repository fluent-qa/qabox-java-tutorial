package io.fluent.qabox.util;

import io.fluent.qabox.exception.BoxPermissionException;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.processor.invoker.PermissionInvoke;
import io.fluent.qabox.view.BoxModel;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.function.Function;


public class Boxes {

    public static void powerLegal(BoxModel BoxModel, Function<PermissionObject, Boolean> function, String errorMessage) {
        powerLegal(function.apply(PermissionInvoke.getPermissionObject(BoxModel)), errorMessage);
    }

    public static void powerLegal(BoxModel BoxModel, Function<PermissionObject, Boolean> function) {
        powerLegal(BoxModel, function, null);
    }

    public static void powerLegal(Boolean bool) {
        powerLegal(bool, null);
    }

    public static void powerLegal(Boolean bool, String message) {
        if (!Boolean.TRUE.equals(bool)) throw new BoxPermissionException(message);
    }

    public static void requireFalse(boolean bool, String message) {
        requireTrue(!bool, message);
    }

    public static void requireTrue(boolean bool, String message) {
        if (!bool) throw new WebApiRuntimeException(message);
    }

    public static <T> T requireNonNull(T t, String message) {
        if (t == null) throw new WebApiRuntimeException(message);
        return t;
    }

    public static void requireNull(Object obj, String message) {
        if (obj != null) throw new WebApiRuntimeException(message);
    }

    public static String generateCode() {
        return generateCode(8);
    }

    public static String generateCode(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
