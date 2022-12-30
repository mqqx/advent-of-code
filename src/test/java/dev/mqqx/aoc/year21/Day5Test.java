package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day5Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day5/5-example");

    assertThat(Day5.solvePart1(example)).isEqualTo(5);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day5/5");

    // too high 5638
    assertThat(Day5.solvePart1(resource)).isEqualTo(5_442);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day5/5-example");

    assertThat(Day5.solvePart2(example)).isEqualTo(12);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day5/5");

    assertThat(Day5.solvePart2(resource)).isEqualTo(19_571);
  }
}
