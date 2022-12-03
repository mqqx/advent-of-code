package dev.mqqx.aoc.day;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day2Test {

  @Test
  void testCalculateScoreExample() {
    final Resource strategyGuideExample = new ClassPathResource("day2/2-example");
    assertThat(Day2.calculateScore(strategyGuideExample)).isEqualTo(15);
  }

  @Test
  void testCalculateScore() {
    final Resource strategyGuideExample = new ClassPathResource("day2/2");

    assertThat(Day2.calculateScore(strategyGuideExample)).isEqualTo(10_941);
  }

  @Test
  void testCalculateScoreExample2B() {
    final Resource strategyGuideExample = new ClassPathResource("day2/2-example");

    assertThat(Day2.calculateScoreWithChangedOutcome(strategyGuideExample)).isEqualTo(12);
  }

  @Test
  void testCalculateScore2B() {
    final Resource strategyGuideExample = new ClassPathResource("day2/2");

    assertThat(Day2.calculateScoreWithChangedOutcome(strategyGuideExample)).isEqualTo(13_071);
  }
}
