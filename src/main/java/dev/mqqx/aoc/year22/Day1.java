package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day1 {

  public static final int NUMBER_OF_ELVES_CARRYING_THE_MOST_CALORIES = 3;

  static int calculateMostCalories(Resource elfCalories) {
    return lines(elfCalories, "\n\n").map(Day1::getCalorieCount).reduce(0, Integer::max);
  }

  static int calculateTotalOfTop3MostCalories(Resource elfCalories) {
    return lines(elfCalories, "\n\n")
        .map(Day1::getCalorieCount)
        .sorted(reverseOrder())
        .limit(NUMBER_OF_ELVES_CARRYING_THE_MOST_CALORIES)
        .reduce(0, Integer::sum);
  }

  private static Integer getCalorieCount(String calories) {
    return stream(calories.split("\n")).mapToInt(Integer::valueOf).sum();
  }
}
