package io.fluentqa.agile.service.skel;

import io.fluentqa.agile.entity.TestCase;
import lombok.Data;

@Data
public class TestCaseDto extends TestCase {
  private AdditionalDataRequest additionalData;
  private SearchQueryRequest searchQueryRequest;
}
