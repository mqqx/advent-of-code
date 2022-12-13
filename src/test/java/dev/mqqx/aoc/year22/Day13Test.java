package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day13Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day13/13-example");

    assertThat(Day13.solvePart1(example)).isEqualTo(13);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day13/13");

    assertThat(Day13.solvePart1(resource)).isEqualTo(5_252);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day13/13-example");

    assertThat(Day13.solvePart2(example)).isEqualTo(140);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day13/13");

    assertThat(Day13.solvePart2(resource)).isEqualTo(20_592);
  }
}
