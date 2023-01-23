package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day22Test {

  @Test
  void testSolvePart1ExampleSmol() {
    final Resource example = new ClassPathResource("year21/day22/22-example-small");

    assertThat(Day22.solvePart1(example)).isEqualTo(39L);
  }

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day22/22-example");

    assertThat(Day22.solvePart1(example)).isEqualTo(590_784L);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day22/22");

    assertThat(Day22.solvePart1(resource)).isEqualTo(556_501L);
  }

  @Test
  void testSolvePart2ExampleSmol() {
    final Resource example = new ClassPathResource("year21/day22/22-example-small");

    assertThat(Day22.solvePart1(example)).isEqualTo(39L);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day22/22-example");

    assertThat(Day22.solvePart2(example)).isEqualTo(39_769_202_357_779L);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day22/22");

    assertThat(Day22.solvePart2(resource)).isEqualTo(1_217_140_271_559_773L);
  }
}
