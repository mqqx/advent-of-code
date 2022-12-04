package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.ElfUtils.splitStringResource;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day1 {

  public static final int NUMBER_OF_ELVES_CARRYING_THE_MOST_CALORIES = 3;

  static int calculateMostCalories(Resource elfCalories) {
    final List<String> groupsOfCalories = splitStringResource(elfCalories, "\n\n");

    int mostCalories = 0;

    for (String calories : groupsOfCalories) {
      final Integer calorieCount = getCalorieCount(calories);

      if (calorieCount > mostCalories) {
        mostCalories = calorieCount;
      }
    }

    return mostCalories;
  }

  static int calculateTotalOfTop3MostCalories(Resource elfCalories) {
    final List<String> groupOfCalories = splitStringResource(elfCalories, "\n\n");

    final ArrayList<Integer> calorieList =
        groupOfCalories.stream()
            .map(Day1::getCalorieCount)
            .collect(Collectors.toCollection(ArrayList::new));

    return calorieList.stream()
        .sorted(reverseOrder())
        .limit(NUMBER_OF_ELVES_CARRYING_THE_MOST_CALORIES)
        .reduce(0, Integer::sum);
  }

  private static Integer getCalorieCount(String calories) {
    return stream(calories.split("\n")).toList().stream()
        .map(Integer::valueOf)
        .reduce(0, Integer::sum);
  }
}
