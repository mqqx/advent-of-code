package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day10Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day10/10-example");

    assertThat(Day10.solvePart1(example)).isEqualTo(26_397);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day10/10");

    assertThat(Day10.solvePart1(resource)).isEqualTo(367_227);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day10/10-example");

    assertThat(Day10.solvePart2(example)).isEqualTo(288_957);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day10/10");

    assertThat(Day10.solvePart2(resource)).isEqualTo(3_583_341_858L);
  }
}
