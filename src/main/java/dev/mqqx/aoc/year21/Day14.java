package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

import com.google.common.util.concurrent.AtomicLongMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day14 {

  static long solvePart1(Resource input) {
    final List<String> lines = linesList(input);
    final Map<String, String> pairInsertionRules = initPairInsertionRules(lines);
    String polymer = lines.get(0);

    for (int step = 0; step < 10; step++) {
      polymer = populatePolymerByStringManipulation(pairInsertionRules, polymer);
    }

    return getQuantityOfMostCommonCharSubtractedByLeastCommon(polymer);
  }

  static long solvePart2(Resource input) {
    final List<String> lines = linesList(input);
    final Map<String, String> pairInsertionRules = initPairInsertionRules(lines);
    final String polymer = lines.get(0);
    AtomicLongMap<String> pairCounter = initPairCounter(polymer);

    for (int step = 0; step < 40; step++) {
      pairCounter = populatePolymerByPairCounting(pairInsertionRules, pairCounter);
    }

    return getQuantityOfMostCommonCharSubtractedByLeastCommon(pairCounter.asMap(), polymer);
  }

  private static AtomicLongMap<String> populatePolymerByPairCounting(
      Map<String, String> pairInsertionRules, AtomicLongMap<String> pairCounter) {
    final AtomicLongMap<String> nextPairCounter = AtomicLongMap.create();
    for (Entry<String, String> entry : pairInsertionRules.entrySet()) {
      final String firstNewPair = entry.getKey().charAt(0) + entry.getValue();
      final String secondNewPair = entry.getValue() + entry.getKey().charAt(1);
      final long countToMove = pairCounter.get(entry.getKey());
      nextPairCounter.addAndGet(firstNewPair, countToMove);
      nextPairCounter.addAndGet(secondNewPair, countToMove);
    }
    pairCounter = nextPairCounter;
    return pairCounter;
  }

  private static AtomicLongMap<String> initPairCounter(String polymer) {
    AtomicLongMap<String> pairCounter = AtomicLongMap.create();

    for (int i = 0; i < polymer.length() - 1; i++) {
      pairCounter.put(polymer.substring(i, i + 2), 1L);
    }
    return pairCounter;
  }

  private static Long getQuantityOfMostCommonCharSubtractedByLeastCommon(String polymer) {
    final AtomicLongMap<Character> characterCounts = AtomicLongMap.create();

    for (char c : polymer.toCharArray()) {
      characterCounts.getAndIncrement(c);
    }

    final List<Long> counts = characterCounts.asMap().values().stream().sorted().toList();

    return counts.get(counts.size() - 1) - counts.get(0);
  }

  private static Long getQuantityOfMostCommonCharSubtractedByLeastCommon(
      Map<String, Long> pairCountMap, String polymer) {
    final AtomicLongMap<Character> charCountMap = AtomicLongMap.create();
    for (Entry<String, Long> entry : pairCountMap.entrySet()) {
      charCountMap.getAndAdd(entry.getKey().charAt(0), entry.getValue());
      charCountMap.getAndAdd(entry.getKey().charAt(1), entry.getValue());
    }
    // Need to add one to the count of the first and the last char,
    // so they are double counted as all other chars
    charCountMap.getAndIncrement(polymer.charAt(0));
    charCountMap.getAndIncrement(polymer.charAt(polymer.length() - 1));

    final List<Long> counts = charCountMap.asMap().values().stream().sorted().toList();
    // need to divide result by 2 as we counted the letters in each pair and therefore each letter
    // twice
    return (counts.get(counts.size() - 1) - counts.get(0)) / 2;
  }

  private static String populatePolymerByStringManipulation(
      Map<String, String> pairInsertionRules, String polymer) {
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
        .collect(toMap(Entry::getKey, Entry::getValue));
  }
}
