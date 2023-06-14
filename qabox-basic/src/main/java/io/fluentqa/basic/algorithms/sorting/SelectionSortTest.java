package io.fluentqa.basic.algorithms.sorting;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SelectionSortTest {

  private SelectionSort ss;

  static Stream<Arguments> testData() {
    return Stream.of(
      arguments(new int[]{27, 26}, new int[]{26, 27}),
      arguments(new int[]{20, 10, 80, 70, 60, 50, 40, 30, 90, 100}, new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100})
    );
  }

  @BeforeEach
  void setup() {
    ss = new SelectionSort();
  }

  @ParameterizedTest
  @MethodSource("testData")
  void shouldSortInputArray(int[] inputArr, int[] expectedSortedArr) {
    int[] actualSortedArr = ss.sort(inputArr);
    assertArrayEquals(expectedSortedArr, actualSortedArr);
  }

}
