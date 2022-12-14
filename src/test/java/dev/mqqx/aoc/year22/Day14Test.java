package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day14Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day14/14-example");

    assertThat(Day14.solvePart1(example)).isEqualTo(24);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day14/14");

    assertThat(Day14.solvePart1(resource)).isEqualTo(665);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day14/14-example");

    assertThat(Day14.solvePart2(example)).isEqualTo(93);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day14/14");

    assertThat(Day14.solvePart2(resource)).isEqualTo(25_434);
  }
}
