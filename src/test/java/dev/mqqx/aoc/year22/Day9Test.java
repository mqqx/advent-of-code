package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day9Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day9/9-example");

    assertThat(Day9.solvePart1(example)).isEqualTo(13);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day9/9");
    assertThat(Day9.solvePart1(resource)).isEqualTo(6_209);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day9/9-example");

    assertThat(Day9.solvePart2(example)).isEqualTo(1);
  }

  @Test
  void testSolvePart2ExampleLarge() {
    final Resource example = new ClassPathResource("year22/day9/9-example-large");

    assertThat(Day9.solvePart2(example)).isEqualTo(36);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day9/9");
    // 2178 too low
    assertThat(Day9.solvePart2(resource)).isEqualTo(2460);
  }
}
