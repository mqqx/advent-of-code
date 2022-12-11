package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day11Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day11/11-example");

    assertThat(Day11.solvePart1(example)).isEqualTo(10_605L);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day11/11");

    assertThat(Day11.solvePart1(resource)).isEqualTo(56_350L);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day11/11-example");

    assertThat(Day11.solvePart2(example)).isEqualTo(2_713_310_158L);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day11/11");

    assertThat(Day11.solvePart2(resource)).isEqualTo(13_954_061_248L);
  }
}
