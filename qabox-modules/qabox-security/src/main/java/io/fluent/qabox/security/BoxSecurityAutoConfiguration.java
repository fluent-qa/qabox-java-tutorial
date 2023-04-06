package io.fluent.qabox.security;

import io.fluent.qabox.module.BoxModule;
import io.fluent.qabox.module.BoxModuleInvoke;
import io.fluent.qabox.module.ModuleInfo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
@EnableConfigurationProperties
public class BoxSecurityAutoConfiguration implements BoxModule {

  static {
    BoxModuleInvoke.addEruptModule(BoxSecurityAutoConfiguration.class);
  }

  @Override
  public ModuleInfo info() {
    return ModuleInfo.builder().name("box-security").build();
  }

}
