package io.fluentqa.codegen.service.models;

import lombok.Data;

@Data
public class DataSourceGenerateConfig {
  private String name;
  private String connectionUrl;
  private String driverClass;
  private String dataSourceType;
}
