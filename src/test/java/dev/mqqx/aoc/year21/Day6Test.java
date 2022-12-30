package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day6Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day6/6-example");

    assertThat(Day6.solvePart1(example)).isEqualTo(5_934);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day6/6");

    assertThat(Day6.solvePart1(resource)).isEqualTo(343_441);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day6/6-example");

    assertThat(Day6.solvePart2(example)).isEqualTo(26_984_457_539L);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day6/6");

    assertThat(Day6.solvePart2(resource)).isEqualTo(1_569_108_373_832L);
  }
}
