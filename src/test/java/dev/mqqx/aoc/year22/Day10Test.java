package dev.mqqx.aoc.year22;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day10Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year22/day10/10-example");

    assertThat(Day10.solvePart1(example)).isEqualTo(13_140);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year22/day10/10");

    assertThat(Day10.solvePart1(resource)).isEqualTo(14_760);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year22/day10/10-example");

    assertThat(Day10.solvePart2(example))
        .isEqualTo(
            """
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....""");
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year22/day10/10");

    assertThat(Day10.solvePart2(resource))
        .isEqualTo(
            """
                ####.####..##..####.###..#..#.###..####.
                #....#....#..#.#....#..#.#..#.#..#.#....
                ###..###..#....###..#..#.#..#.#..#.###..
                #....#....#.##.#....###..#..#.###..#....
                #....#....#..#.#....#.#..#..#.#.#..#....
                ####.#.....###.####.#..#..##..#..#.####.""");
  }
}
