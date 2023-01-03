package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day13Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day13/13-example");

    assertThat(Day13.solvePart1(example)).isEqualTo(17);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day13/13");

    assertThat(Day13.solvePart1(resource)).isEqualTo(847);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day13/13-example");

    assertThat(Day13.solvePart2(example))
        .isEqualTo(
            """
        #####
        #   #
        #   #
        #   #
        #####
        """);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day13/13");

    assertThat(Day13.solvePart2(resource))
        .isEqualTo(
            """
        ###   ##  #### ###   ##  ####  ##  ###\s
        #  # #  #    # #  # #  # #    #  # #  #
        ###  #      #  #  # #    ###  #  # ###\s
        #  # #     #   ###  #    #    #### #  #
        #  # #  # #    # #  #  # #    #  # #  #
        ###   ##  #### #  #  ##  #### #  # ###\s
        """);
  }
}
