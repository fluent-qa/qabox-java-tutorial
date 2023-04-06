package io.fluent.qabox.testmodels;

import io.fluent.qabox.Box;


import java.util.ArrayList;
import java.util.List;


@Box(name = "测试任务集")

public class TestTask  {

  private Long testPlanId;
  private String owner;
  private List<TestCase> testCaseTab = new ArrayList<>();
  private String status;
  private Boolean isValid = true;
}