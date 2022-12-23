package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day16Test {
  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day16/16-example");

    assertThat(Day16.solvePart1(example)).isEqualTo(1_651);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day16/16");

    assertThat(Day16.solvePart1(resource)).isEqualTo(1_641);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day16/16-example");

    assertThat(Day16.solvePart2(example)).isEqualTo(1_707);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day16/16");

    assertThat(Day16.solvePart2(resource)).isEqualTo(2_261);
  }
}
