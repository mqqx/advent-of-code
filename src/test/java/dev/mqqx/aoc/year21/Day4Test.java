package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day4Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day4/4-example");

    assertThat(Day4.solvePart1(example)).isEqualTo(4_512);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day4/4");

    assertThat(Day4.solvePart1(resource)).isEqualTo(87_456);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day4/4-example");

    assertThat(Day4.solvePart2(example)).isEqualTo(1_924);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day4/4");

    assertThat(Day4.solvePart2(resource)).isEqualTo(15_561);
  }
}
