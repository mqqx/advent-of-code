package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day17Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day17/17-example");

    assertThat(Day17.solvePart1(example)).isEqualTo(3_068);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day17/17");

    assertThat(Day17.solvePart1(resource)).isEqualTo(3_217);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day17/17-example");

    assertThat(Day17.solvePart2(example)).isEqualTo(1_514_285_714_288L);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day17/17");

    assertThat(Day17.solvePart2(resource)).isEqualTo(1_585_673_352_422L);
  }
}
