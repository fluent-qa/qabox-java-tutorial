package io.fluent.qabox.upms.util;

import io.fluent.qabox.upms.enums.FunPermission;

public class UPMSUtil {

    public static String getEruptFunPermissionsCode(String name, FunPermission eruptFunPermissions) {
        return name + "@" + eruptFunPermissions.name();
    }

}
