package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day14Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day14/14-example");

    assertThat(Day14.solvePart1(example)).isEqualTo(1_588);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day14/14");

    assertThat(Day14.solvePart1(resource)).isEqualTo(2_602);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day14/14-example");

    assertThat(Day14.solvePart2(example)).isEqualTo(2_188_189_693_529L);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day14/14");

    assertThat(Day14.solvePart2(resource)).isEqualTo(2_942_885_922_173L);
  }
}
