package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day23Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day23/23-example");

    assertThat(Day23.solvePart1(example)).isEqualTo(12_521);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day23/23");

    assertThat(Day23.solvePart1(resource)).isEqualTo(12_240);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day23/23-example");

    assertThat(Day23.solvePart2(example)).isEqualTo(44_169);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day23/23");

    assertThat(Day23.solvePart2(resource)).isEqualTo(44_618);
  }
}
