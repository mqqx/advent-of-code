package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day18Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day18/18-example");

    assertThat(Day18.solvePart1(example)).isEqualTo(64);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day18/18");

    assertThat(Day18.solvePart1(resource)).isEqualTo(4_608);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day18/18-example");

    assertThat(Day18.solvePart2(example)).isEqualTo(58);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day18/18");

    assertThat(Day18.solvePart2(resource)).isEqualTo(2_652);
  }
}
