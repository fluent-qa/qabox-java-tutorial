package io.fluent.qabox.config;

import com.google.gson.Gson;
import io.fluent.qabox.testmodels.TestTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GsonFactoryTest {

  private final Gson gson = GsonFactory.getGson();

  @Test
  public void testJsonIssue() {
    String jsonStr = "{\"testPlanId\":\"731\",\"owner\":\"xxx\",\"testCaseTab\":[{\"id\":\"552\"},{\"id\":\"553\"}],\"status\":\"yyy\",\"isValid\":true,\"id\":751}";
    TestTask ts = gson.fromJson(jsonStr, TestTask.class);
    System.out.println(ts);

  }


}