package io.fluent.qabox.upms;

import io.fluent.qabox.annotation.BoxScan;
import io.fluent.qabox.config.constant.MenuStatus;
import io.fluent.qabox.config.constant.MenuTypeEnum;
import io.fluent.qabox.module.BoxModule;
import io.fluent.qabox.module.BoxModuleInvoke;
import io.fluent.qabox.module.MetaMenu;
import io.fluent.qabox.module.ModuleInfo;
import io.fluent.qabox.upms.model.*;
import io.fluent.qabox.upms.model.log.LoginLog;
import io.fluent.qabox.upms.model.log.OperationLog;
import io.fluent.qabox.upms.model.online.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import java.util.ArrayList;
import java.util.List;


@Configuration
@ComponentScan
@EntityScan
@BoxScan
@EnableConfigurationProperties
public class QaBoxUpmsAutoConfiguration implements BoxModule {

    static {
        BoxModuleInvoke.addEruptModule(QaBoxUpmsAutoConfiguration.class);
    }

    @Override
    public ModuleInfo info() {
        return ModuleInfo.builder().name("qabox-upms").build();
    }

    @Override
    public List<MetaMenu> initMenus() {
        List<MetaMenu> menus = new ArrayList<>();
        menus.add(MetaMenu.createRootMenu("$manager", "系统管理", "fa fa-cogs", 1));
        menus.add(MetaMenu.createEruptClassMenu(BoxMenu.class, menus.get(0), 0, MenuTypeEnum.TREE));
        menus.add(MetaMenu.createEruptClassMenu(BoxRole.class, menus.get(0), 10));
        menus.add(MetaMenu.createEruptClassMenu(BoxOrg.class, menus.get(0), 20, MenuTypeEnum.TREE));
        menus.add(MetaMenu.createEruptClassMenu(BoxPosition.class, menus.get(0), 30, MenuTypeEnum.TREE));
        menus.add(MetaMenu.createEruptClassMenu(BoxUser.class, menus.get(0), 40));
        menus.add(MetaMenu.createEruptClassMenu(LookupCode.class, menus.get(0), 50));
        menus.add(MetaMenu.createEruptClassMenu(LookupItem.class, menus.get(0), 60, MenuStatus.HIDE));
        menus.add(MetaMenu.createEruptClassMenu(UserOnline.class, menus.get(0), 65));
        menus.add(MetaMenu.createEruptClassMenu(LoginLog.class, menus.get(0), 70));
        menus.add(MetaMenu.createEruptClassMenu(OperationLog.class, menus.get(0), 80));
        return menus;
    }

}
