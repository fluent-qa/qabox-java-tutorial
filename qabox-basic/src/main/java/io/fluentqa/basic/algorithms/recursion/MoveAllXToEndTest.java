package io.fluentqa.basic.algorithms.recursion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveAllXToEndTest {

  private MoveAllXToEnd m;

  @BeforeEach
  void setUp() {
    m = new MoveAllXToEnd();
  }

  @ParameterizedTest
  @CsvSource({
    "axbxcx, abcxxx",
    "xxx, xxx",
    "axxbcxexfgxxhixj, abcefghijxxxxxxx"
  })
  void shouldMoveAllXtoTheEnd(String input, String expectedOutput) {
    String actualOutput = m.moveAllXtoTheEnd(input);
    assertEquals(expectedOutput, actualOutput);
  }

}
