package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.ElfUtils.splitStringResourceByLineFeed;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.rangeClosed;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day4 {

  static int getFullyOverlappingPairs(Resource pairsOfElves) {
    return getOverlappingCounter(pairsOfElves, true);
  }

  static int getOverlappingPairs(Resource pairsOfElves) {
    return getOverlappingCounter(pairsOfElves, false);
  }

  private static int getOverlappingCounter(Resource pairsOfElves, boolean isFullyOverlapping) {
    return (int)
        splitStringResourceByLineFeed(pairsOfElves).stream()
            .map(pair -> pair.split(","))
            .map(splitPair -> checkOverlapping(splitPair, isFullyOverlapping))
            .filter(Boolean.TRUE::equals)
            .count();
  }

  private static boolean checkOverlapping(String[] splitPair, boolean isFullyOverlapping) {
    final Set<Integer> firstRange = getRange(splitPair[0]);
    final Set<Integer> secondRange = getRange(splitPair[1]);

    if (isFullyOverlapping) {
      return firstRange.containsAll(secondRange) || secondRange.containsAll(firstRange);
    }
    return firstRange.stream().anyMatch(secondRange::contains);
  }

  private static Set<Integer> getRange(String sections) {
    final String[] splitSections = sections.split("-");
    return rangeClosed(parseInt(splitSections[0]), parseInt(splitSections[1]))
        .boxed()
        .collect(Collectors.toSet());
  }
}
