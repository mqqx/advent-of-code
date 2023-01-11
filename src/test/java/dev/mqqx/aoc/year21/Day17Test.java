package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day17Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day17/17-example");

    assertThat(Day17.solvePart1(example)).isEqualTo(45);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day17/17");

    assertThat(Day17.solvePart1(resource)).isEqualTo(5_886);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day17/17-example");

    assertThat(Day17.solvePart2(example)).isEqualTo(112);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day17/17");

    assertThat(Day17.solvePart2(resource)).isEqualTo(1_806);
  }
}
