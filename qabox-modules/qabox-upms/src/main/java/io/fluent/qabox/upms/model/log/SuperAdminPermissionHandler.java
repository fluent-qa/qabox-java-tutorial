package io.fluent.qabox.upms.model.log;

import io.fluent.qabox.fun.PermissionHandler;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.upms.service.BoxUserService;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;


@Component
public class SuperAdminPermissionHandler implements PermissionHandler {

    @Resource
    private BoxUserService boxUserService;

    @Override
    public void handler(PermissionObject power) {
        if (boxUserService.getCurrentEruptUser().getIsAdmin()) {
            power.setDelete(true);
        }
    }
}
