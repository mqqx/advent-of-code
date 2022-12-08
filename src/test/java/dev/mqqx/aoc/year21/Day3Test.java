package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day3Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day3/3-example");

    assertThat(Day3.solvePart1(example)).isEqualTo(198);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day3/3");

    assertThat(Day3.solvePart1(resource)).isEqualTo(4_006_064);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day3/3-example");

    assertThat(Day3.solvePart2(example)).isEqualTo(230);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day3/3");

    assertThat(Day3.solvePart2(resource)).isEqualTo(5_941_884);
  }
}
