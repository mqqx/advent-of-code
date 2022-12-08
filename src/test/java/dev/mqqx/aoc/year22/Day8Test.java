package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day8Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day8/8-example");

    assertThat(Day8.solvePart1(example)).isEqualTo(21);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day8/8");
    assertThat(Day8.solvePart1(resource)).isEqualTo(1_782);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day8/8-example");

    assertThat(Day8.solvePart2(example)).isEqualTo(8);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day8/8");

    assertThat(Day8.solvePart2(resource)).isEqualTo(474_606);
  }
}
