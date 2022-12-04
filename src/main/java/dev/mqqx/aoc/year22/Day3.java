package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day3 {

  private static final int LOWER_CASE_OFFSET_FOR_PRIORITY_CALCULATION = -9;
  private static final int UPPER_CASE_OFFSET_FOR_PRIORITY_CALCULATION = 17;
  private static final int RUCKSACKS_PER_GROUP = 3;

  static int sumItemPriorities(Resource rucksacksWithItems) {
    int sumOfPriorities = 0;

    for (String rucksack : linesList(rucksacksWithItems)) {
      final String[] compartments = getCompartments(rucksack);
      final String firstCompartment = compartments[0];

      for (int i = 0; i < firstCompartment.length(); i++) {
        final char itemInTwoCompartments = firstCompartment.charAt(i);
        final String secondCompartment = compartments[1];
        final boolean hasItemInBothCompartments =
            secondCompartment.contains(Character.toString(itemInTwoCompartments));

        if (hasItemInBothCompartments) {
          sumOfPriorities += getPriorityOfItem(itemInTwoCompartments);
          break;
        }
      }
    }
    return sumOfPriorities;
  }

  public static int sumBadgePriorities(Resource rucksacksWithItems) {
    int sumOfPriorities = 0;
    final List<String> rucksacks = linesList(rucksacksWithItems);

    for (int j = 0; j < rucksacks.size(); j += RUCKSACKS_PER_GROUP) {
      String firstRucksack = rucksacks.get(j);
      String secondRucksack = rucksacks.get(j + 1);
      String thirdRucksack = rucksacks.get(j + 2);

      for (int i = 0; i < firstRucksack.length(); i++) {
        final char possibleBadgeInAllRucksacks = firstRucksack.charAt(i);
        final String possibleBadge = Character.toString(possibleBadgeInAllRucksacks);
        final boolean hasBadgeInAllRucksacks =
            secondRucksack.contains(possibleBadge) && thirdRucksack.contains(possibleBadge);

        if (hasBadgeInAllRucksacks) {
          sumOfPriorities += getPriorityOfItem(possibleBadgeInAllRucksacks);
          break;
        }
      }
    }

    return sumOfPriorities;
  }

  private static String[] getCompartments(String rucksack) {
    final int mid = rucksack.length() / 2;
    return new String[] {rucksack.substring(0, mid), rucksack.substring(mid)};
  }

  private static int getPriorityOfItem(char item) {
    return Character.isLowerCase(item)
        ? Character.getNumericValue(item) + LOWER_CASE_OFFSET_FOR_PRIORITY_CALCULATION
        : Character.getNumericValue(item) + UPPER_CASE_OFFSET_FOR_PRIORITY_CALCULATION;
  }
}
