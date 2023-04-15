package io.fluentqa.codegen.service;

import io.fluentqa.codegen.service.models.ColumnCodeConfig;

import java.util.List;

public interface ColumnInfoService {

  public List<ColumnCodeConfig> getColumnInformation();
}
