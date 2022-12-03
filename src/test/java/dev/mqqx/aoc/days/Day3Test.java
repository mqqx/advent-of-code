package dev.mqqx.aoc.days;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day3Test {

  @Test
  void testCalculateScoreExample() {
    final Resource rucksacksWithItems = new ClassPathResource("day3/3-example");

    assertThat(Day3.sumItemPriorities(rucksacksWithItems)).isEqualTo(157);
  }

  @Test
  void testCalculateScore() {
    final Resource rucksacksWithItems = new ClassPathResource("day3/3");

    assertThat(Day3.sumItemPriorities(rucksacksWithItems)).isEqualTo(7_863);
  }
}
