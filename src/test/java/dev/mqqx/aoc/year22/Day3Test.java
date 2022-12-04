package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day3Test {

  @Test
  void testSumItemPrioritiesExample() {
    final Resource rucksacksWithItems = new ClassPathResource("year22/day3/3-example");

    assertThat(Day3.sumItemPriorities(rucksacksWithItems)).isEqualTo(157);
  }

  @Test
  void testSumItemPriorities() {
    final Resource rucksacksWithItems = new ClassPathResource("year22/day3/3");

    assertThat(Day3.sumItemPriorities(rucksacksWithItems)).isEqualTo(7_863);
  }

  @Test
  void testSumBadgePrioritiesExample() {
    final Resource rucksacksWithItems = new ClassPathResource("year22/day3/3-example");

    assertThat(Day3.sumBadgePriorities(rucksacksWithItems)).isEqualTo(70);
  }

  @Test
  void testSumBadgePriorities() {
    final Resource rucksacksWithItems = new ClassPathResource("year22/day3/3");

    assertThat(Day3.sumBadgePriorities(rucksacksWithItems)).isEqualTo(2_488);
  }
}
