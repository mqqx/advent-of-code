package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day21Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day21/21-example");

    assertThat(Day21.solvePart1(example)).isEqualTo(152);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day21/21");
    // long  1905357036
    // long  1905357036L
    // long -2389610260

    assertThat(Day21.solvePart1(resource)).isEqualTo(84244467642604L);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day21/21-example");

    assertThat(Day21.solvePart2(example)).isEqualTo(157);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day21/21");

    assertThat(Day21.solvePart2(resource)).isEqualTo(157);
  }
}
