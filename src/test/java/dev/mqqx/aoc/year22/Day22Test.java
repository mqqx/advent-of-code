package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day22Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day22/22-example");

    assertThat(Day22.solvePart1(example)).isEqualTo(6_032);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day22/22");

    assertThat(Day22.solvePart1(resource)).isEqualTo(103_224);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day22/22-example");

    assertThat(Day22.solvePart2(example)).isEqualTo(5_031);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day22/22");

    assertThat(Day22.solvePart2(resource)).isEqualTo(189_097);
  }
}
