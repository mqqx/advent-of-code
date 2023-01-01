package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day9Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day9/9-example");

    assertThat(Day9.solvePart1(example)).isEqualTo(15);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day9/9");

    assertThat(Day9.solvePart1(resource)).isEqualTo(562);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day9/9-example");

    assertThat(Day9.solvePart2(example)).isEqualTo(1_134);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day9/9");

    assertThat(Day9.solvePart2(resource)).isEqualTo(1_076_922L);
  }
}
