package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day25Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day25/25-example");

    assertThat(Day25.solvePart1(example)).isEqualTo("2=-1=0");
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day25/25");

    assertThat(Day25.solvePart1(resource)).isEqualTo("20-1-11==0-=0112-222");
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day25/25-example");

    assertThat(Day25.solvePart2(example)).isEqualTo("สุขสันต์วันหยุด");
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day25/25");

    assertThat(Day25.solvePart2(resource)).isEqualTo("สุขสันต์วันหยุด");
  }
}
