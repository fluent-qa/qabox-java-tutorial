package io.fluent.qabox.supplement;

import org.junit.jupiter.api.Test;

class PropUtilsTest {

  @Test
  void toProperties() {
    var p = PropHelper.toProperties("test=53w5");
    assert p.containsKey("test");
    assert p.containsValue("53w5");
  }
}