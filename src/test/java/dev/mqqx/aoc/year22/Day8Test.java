package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day8Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("year22/day8/8-example");

    assertThat(Day8.doThing(example)).isEqualTo(21);
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("year22/day8/8");
    assertThat(Day8.doThing(resource)).isEqualTo(1_782);
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("year22/day8/8-example");

    assertThat(Day8.doThingAdvanced(example)).isEqualTo(8);
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("year22/day8/8");

    assertThat(Day8.doThingAdvanced(resource)).isEqualTo(474_606);
  }
}
