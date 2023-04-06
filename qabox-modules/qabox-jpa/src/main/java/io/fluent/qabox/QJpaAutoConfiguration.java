package io.fluent.qabox;

import io.fluent.qabox.module.BoxModule;
import io.fluent.qabox.module.BoxModuleInvoke;
import io.fluent.qabox.module.ModuleInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
public class QJpaAutoConfiguration implements BoxModule {

  static {
    BoxModuleInvoke.addEruptModule(QJpaAutoConfiguration.class);
  }

  @Override
  public ModuleInfo info() {
    return ModuleInfo.builder().name("box-jpa").build();
  }

}
