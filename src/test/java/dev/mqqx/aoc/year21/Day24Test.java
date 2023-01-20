package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day24Test {

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day24/24");

    assertThat(Day24.solvePart1(resource)).isEqualTo(79_997_391_969_649L);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day24/24");

    assertThat(Day24.solvePart2(resource)).isEqualTo(157);
  }
}
