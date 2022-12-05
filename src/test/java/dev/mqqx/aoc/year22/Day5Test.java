package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day5Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("year22/day5/5-example");

    assertThat(Day5.doThing(example)).isEqualTo("CMZ");
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("year22/day5/5");

    assertThat(Day5.doThingREal(resource)).isEqualTo("MQSHJMWNH");
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("year22/day5/5-example");

    assertThat(Day5.doThingAdvanced(example)).isEqualTo("MCD");
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("year22/day5/5");

    assertThat(Day5.doThingAdvanced(resource)).isEqualTo("LLWJRBHVZ");
  }
}
