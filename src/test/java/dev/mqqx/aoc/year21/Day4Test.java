package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day4Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("year21/day4/4-example");

    assertThat(Day4.doThing(example)).isEqualTo(4_512);
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("year21/day4/4");

    assertThat(Day4.doThing(resource)).isEqualTo(87_456);
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("year21/day4/4-example");

    assertThat(Day4.doThingAdvanced(example)).isEqualTo(1_924);
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("year21/day4/4");

    assertThat(Day4.doThingAdvanced(resource)).isEqualTo(15_561);
  }
}
