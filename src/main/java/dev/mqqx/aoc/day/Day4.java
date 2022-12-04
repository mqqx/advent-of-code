package dev.mqqx.aoc.day;

import static dev.mqqx.aoc.util.ElfUtils.splitStringResourceByLineFeed;

import java.time.temporal.ValueRange;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day4 {

  static int getOverlappingPairs(Resource pairsOfElves) {
    final List<String> elfPairs = splitStringResourceByLineFeed(pairsOfElves);

    int overlappingCounter = 0;
    for (String pair : elfPairs) {
      final String[] splitPair = pair.split(",");

      final String[] firstElfSections = splitPair[0].split("-");
      final String[] secondElfSections = splitPair[1].split("-");

      if (checkOverlapping(firstElfSections, secondElfSections)) {
        overlappingCounter++;
      } else if (checkOverlapping(secondElfSections, firstElfSections)) {
        overlappingCounter++;
      }
    }

    return overlappingCounter;
  }

  private static boolean checkOverlapping(String[] firstElfSections, String[] secondElfSections) {
    final ValueRange of =
        ValueRange.of(Long.parseLong(firstElfSections[0]), Long.parseLong(firstElfSections[1]));

    final boolean includesLower = of.isValidIntValue(Integer.parseInt(secondElfSections[0]));
    final boolean includesHigher = of.isValidIntValue(Integer.parseInt(secondElfSections[1]));

    return includesLower && includesHigher;
  }

  static int doThingAdvanced(Resource input) {
    final List<String> elfPairs = splitStringResourceByLineFeed(input);

    int overlappingCounter = 0;
    for (String pair : elfPairs) {
      final String[] splitPair = pair.split(",");

      final String[] firstElfSections = splitPair[0].split("-");
      final String[] secondElfSections = splitPair[1].split("-");

      if (checkOverlapping2(firstElfSections, secondElfSections)) {
        overlappingCounter++;
      } else if (checkOverlapping2(secondElfSections, firstElfSections)) {
        overlappingCounter++;
      }
    }

    return overlappingCounter;
  }

  private static boolean checkOverlapping2(String[] firstElfSections, String[] secondElfSections) {
    final ValueRange of =
        ValueRange.of(Long.parseLong(firstElfSections[0]), Long.parseLong(firstElfSections[1]));

    final boolean includesLower = of.isValidIntValue(Integer.parseInt(secondElfSections[0]));
    final boolean includesHigher = of.isValidIntValue(Integer.parseInt(secondElfSections[1]));

    return includesLower || includesHigher;
  }
}
