package io.fluent.qabox.upms.controller;

import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.module.MetaUserinfo;
import io.fluent.qabox.upms.service.BoxUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;


@RestController
public class UPMSController {

    @Resource
    private BoxUserService boxUserService;

    //用户信息
    @GetMapping(BoxRestPath.USERINFO)
    @BoxWebRouter(verifyType = BoxWebRouter.VerifyType.LOGIN)
    public MetaUserinfo userinfo() {
        MetaUserinfo metaUserinfo = boxUserService.getSimpleUserInfo();
        metaUserinfo.setId(null);
        return metaUserinfo;
    }

    //校验菜单类型值权限
    @RequestMapping(BoxRestPath.ERUPT_CODE_PERMISSION + "/{value}")
    @BoxWebRouter(verifyType = BoxWebRouter.VerifyType.LOGIN)
    public boolean eruptPermission(@PathVariable("value") String menuValue) {
        return null != boxUserService.getEruptMenuByValue(menuValue);
    }

}
