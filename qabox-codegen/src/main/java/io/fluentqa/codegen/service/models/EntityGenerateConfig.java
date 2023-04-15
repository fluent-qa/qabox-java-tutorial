package io.fluentqa.codegen.service.models;

import lombok.Data;

@Data
public class EntityGenerateConfig {
  private String name;
  private String type;
  private DataSourceGenerateConfig dsConfig;
  private String tableName;
}
