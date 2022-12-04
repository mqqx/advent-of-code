package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day1Test {

  @Test
  void testCountDepthIncreasesExample() {
    final Resource example = new ClassPathResource("year21/day1/1-example");

    assertThat(Day1.countDepthIncreases(example)).isEqualTo(7);
  }

  @Test
  void testCountDepthIncreases() {
    final Resource measurements = new ClassPathResource("year21/day1/1");

    assertThat(Day1.countDepthIncreases(measurements)).isEqualTo(1_521);
  }

  @Test
  void testCountThreeMeasurementsWindowIncreasesExample() {
    final Resource example = new ClassPathResource("year21/day1/1-example");

    assertThat(Day1.countThreeMeasurementsWindowIncreases(example)).isEqualTo(5);
  }

  @Test
  void testCountThreeMeasurementsWindowIncreases() {
    final Resource measurements = new ClassPathResource("year21/day1/1");

    assertThat(Day1.countThreeMeasurementsWindowIncreases(measurements)).isEqualTo(1_543);
  }
}
