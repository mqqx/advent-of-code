package dev.hmmr.aoc.days;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day1Test {

  @Test
  void testMostCaloriesExample() {
    final Resource example = new ClassPathResource("1-example");

    assertThat(Day1.calculateMostCalories(example)).isEqualTo(24_000);
  }

  @Test
  void testMostCalories() {
    final Resource example = new ClassPathResource("1");

    assertThat(Day1.calculateMostCalories(example)).isEqualTo(69_281);
  }
}
