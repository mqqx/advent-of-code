package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day8Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day8/8-example");

    assertThat(Day8.solvePart1(example)).isEqualTo(26);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day8/8");

    assertThat(Day8.solvePart1(resource)).isEqualTo(239);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day8/8-example");

    assertThat(Day8.solvePart2(example)).isEqualTo(61_229);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day8/8");

    assertThat(Day8.solvePart2(resource)).isEqualTo(946_346);
  }
}
