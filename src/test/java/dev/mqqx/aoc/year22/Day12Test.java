package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day12Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day12/12-example");

    assertThat(Day12.solvePart1(example)).isEqualTo(31);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day12/12");

    assertThat(Day12.solvePart1(resource)).isEqualTo(380);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day12/12-example");

    assertThat(Day12.solvePart2(example)).isEqualTo(29);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day12/12");

    assertThat(Day12.solvePart2(resource)).isEqualTo(375);
  }
}
