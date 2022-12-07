package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day7Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("year22/day7/7-example");

    assertThat(Day7.doThing(example)).isEqualTo(95_437);
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("year22/day7/7");

    assertThat(Day7.doThing(resource)).isEqualTo(1_447_046);
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("year22/day7/7-example");

    assertThat(Day7.doThingAdvanced(example)).isEqualTo(24_933_642);
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("year22/day7/7");

    assertThat(Day7.doThingAdvanced(resource)).isEqualTo(578_710);
  }
}
