package dev.mqqx.aoc.day;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day4Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("day4/4-example");

    assertThat(Day4.getOverlappingPairs(example)).isEqualTo(2);
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("day4/4");

    assertThat(Day4.getOverlappingPairs(resource)).isEqualTo(490);
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("day4/4-example");

    assertThat(Day4.doThingAdvanced(example)).isEqualTo(4);
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("day4/4");

    assertThat(Day4.doThingAdvanced(resource)).isEqualTo(157);
  }
}
