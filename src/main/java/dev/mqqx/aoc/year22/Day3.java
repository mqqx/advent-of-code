package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Character.getNumericValue;
import static java.lang.Character.isLowerCase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day3 {

  private static final int LOWER_CASE_OFFSET_FOR_PRIORITY_CALCULATION = -9;
  private static final int UPPER_CASE_OFFSET_FOR_PRIORITY_CALCULATION = 17;
  private static final int RUCKSACKS_PER_GROUP = 3;

  static int sumItemPriorities(Resource rucksacksWithItems) {
    return lines(rucksacksWithItems)
        .map(Day3::getCompartments)
        .map(Day3::findPriority)
        .reduce(0, Integer::sum);
  }

  private static int findPriority(String[] compartments) {
    return compartments[0]
        .chars()
        .filter(item -> compartments[1].contains(Character.toString(item)))
        .mapToObj(item -> Character.toChars(item)[0])
        .map(Day3::getPriorityOfItem)
        .findFirst()
        .orElse(0);
  }

  public static int sumBadgePriorities(Resource rucksacksWithItems) {
    final AtomicInteger counter = new AtomicInteger(0);

    return lines(rucksacksWithItems)
        .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / RUCKSACKS_PER_GROUP))
        .values()
        .stream()
        .map(Day3::findBadgePriority)
        .reduce(0, Integer::sum);
  }

  private static Integer findBadgePriority(List<String> groupOfRucksacks) {
    String firstRucksack = groupOfRucksacks.get(0);
    String secondRucksack = groupOfRucksacks.get(1);
    String thirdRucksack = groupOfRucksacks.get(2);

    return firstRucksack
        .chars()
        .mapToObj(Character::toString)
        .filter(secondRucksack::contains)
        .filter(thirdRucksack::contains)
        .map(item -> item.charAt(0))
        .map(Day3::getPriorityOfItem)
        .findFirst()
        .orElse(0);
  }

  private static String[] getCompartments(String rucksack) {
    final int mid = rucksack.length() / 2;
    return new String[] {rucksack.substring(0, mid), rucksack.substring(mid)};
  }

  private static int getPriorityOfItem(char item) {
    return isLowerCase(item)
        ? getNumericValue(item) + LOWER_CASE_OFFSET_FOR_PRIORITY_CALCULATION
        : getNumericValue(item) + UPPER_CASE_OFFSET_FOR_PRIORITY_CALCULATION;
  }
}
