package io.fluent.qabox.mindmapping.core;

import io.fluent.qabox.mindmapping.freemind.FreeMindPath;
import io.fluent.qabox.mindmapping.freemind.FreemindAccessor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MindMappingTransformerTest {
  FreemindAccessor accessor = new FreemindAccessor("./template.mm");

  @Test
  void transfer() {
    List<FreeMindPath> allPaths = accessor.generateAllPaths().getAllPaths();

    List<TestCaseModel> testCaseModels = new ArrayList<>();
    for (FreeMindPath path : allPaths) {
      TestCaseModel testCaseModel = new TestCaseModel();
      MindMappingTransformer.transfer(path, testCaseModel);
      testCaseModels.add(testCaseModel);
    }
    System.out.println(testCaseModels);
  }
}