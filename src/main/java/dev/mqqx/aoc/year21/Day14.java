package dev.mqqx.aoc.year21;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

import com.google.common.util.concurrent.AtomicLongMap;
import dev.mqqx.aoc.util.SplitUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day14 {

  static long solvePart1(Resource input) {
    final List<String> lines = SplitUtils.lines(input).toList();
    final Map<String, String> pairInsertionRules = initPairInsertionRules(lines);
    String polymer = lines.get(0);

    for (int step = 0; step < 10; step++) {
      polymer = populatePolymer(pairInsertionRules, polymer);
      System.out.println("After step " + (step + 1) + ": " + polymer.length());
    }

    final AtomicLongMap<Character> characterCounts = AtomicLongMap.create();

    for (char c : polymer.toCharArray()) {
      characterCounts.getAndIncrement(c);
    }

    final Collection<Long> counts = characterCounts.asMap().values();
    final Long max = counts.stream().max(Long::compareTo).orElseThrow();
    final Long min = counts.stream().min(Long::compareTo).orElseThrow();
    return max - min;
  }

  static long solvePart2(Resource input) {

    final List<String> lines = SplitUtils.lines(input).toList();
    final Map<String, String> pairInsertionRules = initPairInsertionRules(lines);
    String polymer = lines.get(0);

    return 0L;
  }

  private static String populatePolymer(Map<String, String> pairInsertionRules, String polymer) {
    final StringBuilder polymerBuilder = new StringBuilder();
    for (int i = 0; i < polymer.length() - 1; i++) {
      final String nextPairToPopulate = polymer.substring(i, i + 2);
      final String element = pairInsertionRules.get(nextPairToPopulate);
      final String populated =
          element == null
              ? nextPairToPopulate
              : new StringBuilder(nextPairToPopulate).insert(1, element).toString();
      final int startIndex = i == 0 ? 0 : polymerBuilder.length() - 1;
      polymerBuilder.replace(startIndex, startIndex + populated.length(), populated);
    }
    polymer = polymerBuilder.toString();
    return polymer;
  }

  private static Map<String, String> initPairInsertionRules(List<String> lines) {
    return lines.stream()
        .map(line -> line.split(" -> "))
        .filter(splitLine -> splitLine.length == 2)
        .map(splitLine -> entry(splitLine[0], splitLine[1]))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
