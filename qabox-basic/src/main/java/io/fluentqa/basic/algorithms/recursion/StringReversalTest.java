package io.fluentqa.basic.algorithms.recursion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringReversalTest {

  private StringReversal sr;

  @BeforeEach
  void setup() {
    sr = new StringReversal();
  }

  @ParameterizedTest
  @CsvSource({
    "hello, olleh",
    "world, dlrow",
    "xyzabc, cbazyx"
  })
  void shouldReturnReversedString(String input, String expected) {
    String actual = sr.reverse(input);
    assertEquals(expected, actual);
  }

}
