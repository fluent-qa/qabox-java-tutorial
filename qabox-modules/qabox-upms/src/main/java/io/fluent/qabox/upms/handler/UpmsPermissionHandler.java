package io.fluent.qabox.upms.handler;

import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.fun.PermissionHandler;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.processor.invoker.PermissionInvoke;
import io.fluent.qabox.upms.enums.FunPermission;
import io.fluent.qabox.upms.service.BoxUserService;
import io.fluent.qabox.upms.util.UPMSUtil;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Map;

/**
 * 全局菜单权限控制
 */
@Service
public class UpmsPermissionHandler implements PermissionHandler {

    static {
        PermissionInvoke.registerPermissionHandler(UpmsPermissionHandler.class);
    }

    @Resource
    private BoxUserService boxUserService;

    @Override
    public void handler(PermissionObject power) {
        Map<String, Boolean> permissionMap = boxUserService.getEruptMenuValuesMap();
        if (power.isAdd()) {
            power.setAdd(powerOff(FunPermission.ADD, permissionMap));
        }
        if (power.isDelete()) {
            power.setDelete(powerOff(FunPermission.DELETE, permissionMap));
        }
        if (power.isEdit()) {
            power.setEdit(powerOff(FunPermission.EDIT, permissionMap));
        }
        if (power.isViewDetails()) {
            power.setViewDetails(powerOff(FunPermission.VIEW_DETAIL, permissionMap));
        }
        if (power.isExport()) {
            power.setExport(powerOff(FunPermission.EXPORT, permissionMap));
        }
        if (power.isImportable()) {
            power.setImportable(powerOff(FunPermission.IMPORTABLE, permissionMap));
        }
    }

    private boolean powerOff(FunPermission eruptFunPermissions, Map<String, Boolean> permissionMap) {
        return permissionMap.containsKey(UPMSUtil.getEruptFunPermissionsCode(UserMenuContext.getMenuContext().getName(), eruptFunPermissions).toLowerCase());
    }

}
