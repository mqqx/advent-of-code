package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day20Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day20/20-example");

    assertThat(Day20.solvePart1(example)).isEqualTo(3);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day20/20");

    assertThat(Day20.solvePart1(resource)).isEqualTo(7_395);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day20/20-example");

    assertThat(Day20.solvePart2(example)).isEqualTo(1_623_178_306);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day20/20");

    assertThat(Day20.solvePart2(resource)).isEqualTo(157);
  }
}
