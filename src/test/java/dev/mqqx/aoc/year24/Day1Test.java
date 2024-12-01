package dev.mqqx.aoc.year24;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day1Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year24/day1/1-example");

    assertThat(Day1.solvePart1(example)).isEqualTo(11);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year24/day1/1");

    assertThat(Day1.solvePart1(resource)).isEqualTo(2_066_446);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year24/day1/1-example");

    assertThat(Day1.solvePart2(example)).isEqualTo(24_931_009);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year24/day1/1");

    assertThat(Day1.solvePart2(resource)).isEqualTo(-1);
  }
}
