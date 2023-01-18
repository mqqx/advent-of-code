package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day18Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day18/18-example");

    assertThat(Day18.solvePart1(example)).isEqualTo(4_140);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day18/18");

    assertThat(Day18.solvePart1(resource)).isEqualTo(3_699);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day18/18-example");

    assertThat(Day18.solvePart2(example)).isEqualTo(3_993);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day18/18");

    assertThat(Day18.solvePart2(resource)).isEqualTo(4_735);
  }
}
