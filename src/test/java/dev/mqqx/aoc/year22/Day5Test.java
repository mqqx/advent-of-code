package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day5Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day5/5-example");

    assertThat(Day5.getTopContainersSingle(example)).isEqualTo("CMZ");
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day5/5");

    assertThat(Day5.getTopContainersSingle(resource)).isEqualTo("MQSHJMWNH");
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day5/5-example");

    assertThat(Day5.getTopContainersMulti(example)).isEqualTo("MCD");
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day5/5");

    assertThat(Day5.getTopContainersMulti(resource)).isEqualTo("LLWJRBHVZ");
  }
}
