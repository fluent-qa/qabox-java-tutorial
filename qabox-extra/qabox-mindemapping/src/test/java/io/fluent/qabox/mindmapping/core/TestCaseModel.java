package io.fluent.qabox.mindmapping.core;


import io.fluent.qabox.mindmapping.core.MindMappingLevel;
import lombok.Data;

@Data
public class TestCaseModel {

  @MindMappingLevel(value = 1)
  private String moduleName;
  @MindMappingLevel(value = 2)
  private String featureName;
  @MindMappingLevel(value = 3)
  private String description;
  @MindMappingLevel(value = 4)
  private String expectedResult;
}
