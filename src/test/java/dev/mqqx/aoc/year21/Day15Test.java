package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day15Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day15/15-example");

    assertThat(Day15.solvePart1(example)).isEqualTo(40);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day15/15");

    assertThat(Day15.solvePart1(resource)).isEqualTo(748);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day15/15-example");

    assertThat(Day15.solvePart2(example)).isEqualTo(315);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day15/15");

    assertThat(Day15.solvePart2(resource)).isEqualTo(3_045);
  }
}
