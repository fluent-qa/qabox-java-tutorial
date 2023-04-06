package io.fluent.qabox.testmodels;

import lombok.Data;
@Data
public class TestCase  {

  private Long productId;
  private Long moduleId;
  private String feature;
  private String summary;
  private String priority;
  private String precondition;
  private String steps;
  private String expectedResult;

//  //Test Case Status
//  private String status;
}