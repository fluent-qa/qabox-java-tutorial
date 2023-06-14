package io.fluentqa.basic.algorithms.recursion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SumOfNaturalNumbersTest {

  private SumOfNaturalNumbers s;

  @BeforeEach
  void setup() {
    s = new SumOfNaturalNumbers();
  }

  @ParameterizedTest
  @CsvSource({
    "1, 1",
    "2, 3",
    "3, 6",
    "10, 55"
  })
  void shouldReturnSumOfNaturalNumbers(int n, int expected) {
    int actual = s.sum(n);
    assertEquals(expected, actual);
  }

}
