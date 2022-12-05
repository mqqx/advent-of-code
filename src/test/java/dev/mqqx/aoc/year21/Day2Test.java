package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day2Test {

  @Test
  void doThingExample() {
    final Resource example = new ClassPathResource("year21/day2/2-example");

    assertThat(Day2.doThing(example)).isEqualTo(150);
  }

  @Test
  void doThing() {
    final Resource resource = new ClassPathResource("year21/day2/2");

    assertThat(Day2.doThing(resource)).isEqualTo(2_150_351);
  }

  @Test
  void doThingAdvancedExample() {
    final Resource example = new ClassPathResource("year21/day2/2-example");

    assertThat(Day2.doThingAdvanced(example)).isEqualTo(900);
  }

  @Test
  void doThingAdvanced() {
    final Resource resource = new ClassPathResource("year21/day2/2");

    assertThat(Day2.doThingAdvanced(resource)).isEqualTo(1_842_742_223);
  }
}
