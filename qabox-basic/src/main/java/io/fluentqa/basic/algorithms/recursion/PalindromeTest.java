package io.fluentqa.basic.algorithms.recursion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PalindromeTest {

  private Palindrome p;

  @BeforeEach
  void setUp() {
    p = new Palindrome();
  }

  @ParameterizedTest
  @CsvSource({
    "0, 6, racecar, true",
    "0, 4, madam, true",
    "0, 4, hello, false",
    "0, 8, malayalam, true"
  })
  void shouldCheckIfAStringIsPalindromeOrNot(int l, int r, String input, boolean expected) {
    boolean actual = p.isPalindrome(l, r, input);
    assertEquals(expected, actual);
  }

}
