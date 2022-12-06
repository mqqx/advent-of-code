package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day3Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("year21/day3/3-example");

    assertThat(Day3.doThing(example)).isEqualTo(198);
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("year21/day3/3");

    assertThat(Day3.doThing(resource)).isEqualTo(4_006_064);
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("year21/day3/3-example");

    assertThat(Day3.doThingAdvanced(example)).isEqualTo(230);
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("year21/day3/3");

    assertThat(Day3.doThingAdvanced(resource)).isEqualTo(5_941_884);
  }
}
