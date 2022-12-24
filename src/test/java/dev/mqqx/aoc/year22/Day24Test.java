package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day24Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day24/24-example");

    assertThat(Day24.solvePart1(example)).isEqualTo(18);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day24/24");

    assertThat(Day24.solvePart1(resource)).isEqualTo(266);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day24/24-example");

    assertThat(Day24.solvePart2(example)).isEqualTo(54);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day24/24");

    assertThat(Day24.solvePart2(resource)).isEqualTo(853);
  }
}
