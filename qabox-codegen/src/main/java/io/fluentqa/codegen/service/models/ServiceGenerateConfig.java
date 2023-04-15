package io.fluentqa.codegen.service.models;

import lombok.Data;

@Data
public class ServiceGenerateConfig {
  private String name;
  private String type;
  private EntityGenerateConfig entityGenerateConfig;
}
