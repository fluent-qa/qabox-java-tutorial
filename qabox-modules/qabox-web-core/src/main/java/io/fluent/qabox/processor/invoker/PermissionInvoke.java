package io.fluent.qabox.processor.invoker;


import io.fluent.qabox.frontend.operation.UIPermission;
import io.fluent.qabox.fun.PermissionHandler;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.view.BoxModel;

import java.util.ArrayList;
import java.util.List;


public class PermissionInvoke {

  private static final List<Class<? extends PermissionHandler>> PermissionHandlerStack = new ArrayList<>();

  public static void registerPermissionHandler(Class<? extends PermissionHandler> PermissionHandler) {
    PermissionHandlerStack.add(PermissionHandler);
  }

  public static PermissionObject getPermissionObject(BoxModel BoxModel) {
    UIPermission permission = BoxModel.getBox().permission();
    if (BoxModel.getBox().authVerify()) {
      PermissionObject powerBean = new PermissionObject(permission);
      if (BoxModel.getBox().authVerify()) {
        PermissionHandlerStack.forEach(ph -> IocUtil.getBean(ph).handler(powerBean));
      }
      if (!permission.permissionHandler().isInterface())
        IocUtil.getBean(permission.permissionHandler()).handler(powerBean);
      return powerBean;
    } else {
      return new PermissionObject(permission);
    }
  }
}
