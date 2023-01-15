package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day20Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day20/20-example");

    assertThat(Day20.solvePart1(example)).isEqualTo(35);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day20/20");

    assertThat(Day20.solvePart1(resource)).isEqualTo(5_437);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day20/20-example");

    assertThat(Day20.solvePart2(example)).isEqualTo(3_351);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day20/20");

    assertThat(Day20.solvePart2(resource)).isEqualTo(19_340);
  }
}
