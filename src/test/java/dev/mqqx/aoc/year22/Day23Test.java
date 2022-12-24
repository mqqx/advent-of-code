package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day23Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day23/23-example");

    assertThat(Day23.solvePart1(example)).isEqualTo(110);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day23/23");

    assertThat(Day23.solvePart1(resource)).isEqualTo(4_052);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day23/23-example");

    assertThat(Day23.solvePart2(example)).isEqualTo(20);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day23/23");

    assertThat(Day23.solvePart2(resource)).isEqualTo(978);
  }
}
