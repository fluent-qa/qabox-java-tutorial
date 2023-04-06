package io.fluent.qabox.tpl;

import io.fluent.qabox.config.constant.MenuTypeEnum;
import io.fluent.qabox.frontend.fun.VLModel;
import io.fluent.qabox.module.BoxModule;
import io.fluent.qabox.module.BoxModuleInvoke;
import io.fluent.qabox.module.ModuleInfo;
import io.fluent.qabox.tpl.service.BoxTplService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;


@Configuration
@ComponentScan
@Component
@EnableConfigurationProperties
public class BoxTplAutoConfiguration implements BoxModule {

    static {
        BoxModuleInvoke.addEruptModule(BoxTplAutoConfiguration.class);
        MenuTypeEnum.addMenuType(new VLModel(BoxTplService.TPL, "模板", "tpl目录下文件名"));
//        MenuTypeEnum.addMenuType(new VLModel(EruptTplService.TPL_MICRO, "模板（微前端 Beta）", "tpl目录下文件名"));
    }

    @Resource
    private BoxTplService eruptTplService;

    @Override
    public void run() {
        eruptTplService.run();
    }

    @Override
    public ModuleInfo info() {
        return ModuleInfo.builder().name("box-tpl").build();
    }

}
