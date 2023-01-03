package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day12Test {

  @Test
  void testSolvePart1ExampleSmall() {
    final Resource example = new ClassPathResource("year21/day12/12-example-small");

    assertThat(Day12.solvePart1(example)).isEqualTo(10);
  }

  @Test
  void testSolvePart1ExampleMedium() {
    final Resource example = new ClassPathResource("year21/day12/12-example-medium");

    assertThat(Day12.solvePart1(example)).isEqualTo(19);
  }

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day12/12-example");

    assertThat(Day12.solvePart1(example)).isEqualTo(226);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day12/12");

    assertThat(Day12.solvePart1(resource)).isEqualTo(3_761);
  }

  @Test
  void testSolvePart2ExampleSmall() {
    final Resource example = new ClassPathResource("year21/day12/12-example-small");

    assertThat(Day12.solvePart2(example)).isEqualTo(36);
  }

  @Test
  void testSolvePart2ExampleMedium() {
    final Resource example = new ClassPathResource("year21/day12/12-example-medium");

    assertThat(Day12.solvePart2(example)).isEqualTo(103);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day12/12-example");

    assertThat(Day12.solvePart2(example)).isEqualTo(3_509);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day12/12");

    assertThat(Day12.solvePart2(resource)).isEqualTo(99_138);
  }
}
