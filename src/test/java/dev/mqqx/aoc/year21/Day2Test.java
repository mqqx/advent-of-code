package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day2Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day2/2-example");

    assertThat(Day2.solvePart1(example)).isEqualTo(150);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day2/2");

    assertThat(Day2.solvePart1(resource)).isEqualTo(2_150_351);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day2/2-example");

    assertThat(Day2.solvePart2(example)).isEqualTo(900);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day2/2");

    assertThat(Day2.solvePart2(resource)).isEqualTo(1_842_742_223);
  }
}
