package io.fluentqa.basic.algorithms.searching;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BinarySearchTest {

  private BinarySearch bs;

  static Stream<Arguments> testData() {
    return Stream.of(
      arguments(new int[]{20, 10, 80, 70, 60, 50, 40, 30, 90, 100}, 30, 2),
      arguments(new int[]{4, 9, 10, 15, 22, 26, 27}, 27, 6),
      arguments(new int[]{4, 9, 10, 15, 22, 26}, 27, -1),
      arguments(IntStream.rangeClosed(1, 1000).toArray(), 1000, 999)
    );
  }

  @BeforeEach
  void setup() {
    bs = new BinarySearch();
  }

  @ParameterizedTest
  @MethodSource("testData")
  void shouldReturnThePositionOfTheSearchedElement(int[] arr, int elementToSearch, int expectedPosition) {
    int actualPosition = bs.search(arr, elementToSearch);
    assertEquals(expectedPosition, actualPosition);
  }

}
