package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day25Test {
  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day25/25-example");

    assertThat(Day25.solvePart1(example)).isEqualTo(58);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day25/25");

    assertThat(Day25.solvePart1(resource)).isEqualTo(582);
  }
}
