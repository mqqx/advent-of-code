package dev.mqqx.aoc.day;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day3 {
  @SneakyThrows
  static int sumItemPriorities(Resource rucksacksWithItems) {

    int sumOfPriorities = 0;
    for (String rucksack : splitIntoRucksacks(rucksacksWithItems)) {
      final int mid = rucksack.length() / 2;
      final String[] compartments = {rucksack.substring(0, mid), rucksack.substring(mid)};
      for (int i = 0; i < compartments[0].length(); i++) {
        final char itemInTwoCompartments = compartments[0].charAt(i);
        if (compartments[1].contains(Character.toString(itemInTwoCompartments))) {
          sumOfPriorities += getPriorityOfItem(itemInTwoCompartments);
          break;
        }
      }
    }
    return sumOfPriorities;
  }

  @SneakyThrows
  public static int sumBadgePriorities(Resource rucksacksWithItems) {
    int sumOfPriorities = 0;
    List<String> splitIntoRucksacks = splitIntoRucksacks(rucksacksWithItems);
    for (int j = 0; j < splitIntoRucksacks.size(); j += 3) {
      String rucksackA = splitIntoRucksacks.get(j);
      String rucksackB = splitIntoRucksacks.get(j + 1);
      String rucksackC = splitIntoRucksacks.get(j + 2);
      for (int i = 0; i < rucksackA.length(); i++) {
        final char possibleBadge = rucksackA.charAt(i);
        if (rucksackB.contains(Character.toString(possibleBadge))
            && rucksackC.contains(Character.toString(possibleBadge))) {
          sumOfPriorities += getPriorityOfItem(possibleBadge);
          break;
        }
      }
    }

    return sumOfPriorities;
  }

  private static int getPriorityOfItem(char a) {
    int magicNumber;
    if (Character.isLowerCase(a)) {
      magicNumber = -9;
    } else {
      magicNumber = 17;
    }

    return Character.getNumericValue(a) + magicNumber;
  }

  private static List<String> splitIntoRucksacks(Resource rucksacksWithItems) throws IOException {
    return stream(new String(rucksacksWithItems.getInputStream().readAllBytes()).split("\n"))
        .toList();
  }
}
