package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day4Test {

  @Test
  void testGetFullyOverlappingPairsExample() {
    final Resource example = new ClassPathResource("year22/day4/4-example");

    assertThat(Day4.getFullyOverlappingPairs(example)).isEqualTo(2);
  }

  @Test
  void testGetFullyOverlappingPairs() {
    final Resource resource = new ClassPathResource("year22/day4/4");

    assertThat(Day4.getFullyOverlappingPairs(resource)).isEqualTo(490);
  }

  @Test
  void testGetOverlappingPairsExample() {
    final Resource example = new ClassPathResource("year22/day4/4-example");

    assertThat(Day4.getOverlappingPairs(example)).isEqualTo(4);
  }

  @Test
  void testGetOverlappingPairs() {
    final Resource resource = new ClassPathResource("year22/day4/4");

    assertThat(Day4.getOverlappingPairs(resource)).isEqualTo(921);
  }
}
