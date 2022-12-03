package dev.mqqx.aoc.day;

import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day1 {

  public static final int NUMBER_OF_ELVES_CARRYING_THE_MOST_CALORIES = 3;

  @SneakyThrows
  static int calculateMostCalories(Resource example) {
    final List<String> groupsOfCalories = splitIntoGroupsOfCalories(example);

    int mostCalories = 0;

    for (String calories : groupsOfCalories) {
      final Integer calorieCount = getCalorieCount(calories);

      if (calorieCount > mostCalories) {
        mostCalories = calorieCount;
      }
    }

    return mostCalories;
  }

  @SneakyThrows
  static int calculateTotalOfTop3MostCalories(Resource example) {
    final List<String> groupOfCalories = splitIntoGroupsOfCalories(example);

    final ArrayList<Integer> calorieList =
        groupOfCalories.stream()
            .map(Day1::getCalorieCount)
            .collect(Collectors.toCollection(ArrayList::new));

    return calorieList.stream()
        .sorted(reverseOrder())
        .limit(NUMBER_OF_ELVES_CARRYING_THE_MOST_CALORIES)
        .reduce(0, Integer::sum);
  }

  private static List<String> splitIntoGroupsOfCalories(Resource example) throws IOException {
    return stream(new String(example.getInputStream().readAllBytes()).split("\n\n")).toList();
  }

  private static Integer getCalorieCount(String calories) {
    return stream(calories.split("\n")).toList().stream()
        .map(Integer::valueOf)
        .reduce(0, Integer::sum);
  }
}
