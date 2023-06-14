package io.fluentqa.basic.algorithms.recursion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactorialTest {

  private Factorial f;

  @BeforeEach
  void setUp() {
    f = new Factorial();
  }

  @ParameterizedTest
  @CsvSource({
    "1, 1",
    "2, 2",
    "3, 6",
    "4, 24",
    "5, 120",
    "6, 720"
  })
  void shouldReturnFactorial(int num, int expectedFactorial) {
    int factorial = f.factorial(num);
    assertEquals(expectedFactorial, factorial);
  }

}
