package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day8 {

  static long solvePart1(Resource input) {
    return lines(input)
        .map(line -> line.split(" \\| ")[1])
        .map(rightPart -> rightPart.split(" "))
        .mapToLong(
            rightNumbers ->
                stream(rightNumbers)
                    .filter(rightNumber -> Set.of(2, 3, 4, 7).contains(rightNumber.length()))
                    .count())
        .sum();
  }

  static int solvePart2(Resource input) {
    // 0 = length 6 - 1, 3, 8 already parsed, contains all from 1, 8 contains all from 0
    // 1 = length 2
    // 2 = length 5 - last with length 5 after 3 and 5 are parsed
    // 3 = length 5 - contains 1
    // 4 = length 4
    // 5 = length 5 - 6 contains all of it
    // 6 = length 6 - last with length 6 after 0 and 9 are parsed
    // 7 = length 3
    // 8 = length 7
    // 9 = length 6 - contains 3

    return lines(input)
        .map(line -> line.split(" \\| "))
        .mapToInt(
            parts -> {
              final Map<String, Integer> stringToInt = new HashMap<>();
              final Map<Integer, String> intToString = new HashMap<>();
              final List<String> numbersToParse = stream(parts[0].split(" ")).collect(toList());

              while (stringToInt.size() < 10) {
                for (int i = numbersToParse.size() - 1; i >= 0; i--) {
                  final String numberToParse = numbersToParse.get(i);
                  int number = -1;

                  switch (numberToParse.length()) {
                    case 2 -> number = 1;
                    case 3 -> number = 7;
                    case 4 -> number = 4;
                      // parses 2, 3 and 5
                    case 5 -> number = parseLength5(intToString, numberToParse, number);
                      // parses 0, 6 and 9
                    case 6 -> number = parseLength6(intToString, numberToParse, number);
                    case 7 -> number = 8;
                    default -> throw new IllegalStateException(
                        "Unexpected value: " + numberToParse);
                  }

                  if (number > -1) {
                    stringToInt.put(numberToParse, number);
                    intToString.put(number, numberToParse);
                    numbersToParse.remove(i);
                  }
                }
              }

              return parseAndSumRightNumbers(parts[1], stringToInt);
            })
        .sum();
  }

  private static int parseAndSumRightNumbers(String rightPart, Map<String, Integer> stringToInt) {
    final String[] numbersToAdd = rightPart.split(" ");

    final StringBuilder numberString = new StringBuilder();
    for (String s : numbersToAdd) {
      for (Entry<String, Integer> entry : stringToInt.entrySet()) {
        if (entry.getKey().length() == s.length() && containsAllChars(entry.getKey(), s)) {
          numberString.append(entry.getValue());
        }
      }
    }

    return parseInt(numberString.toString());
  }

  private static int parseLength5(
      Map<Integer, String> intToString, String numberToParse, int number) {
    final String six = intToString.get(6);
    final String one = intToString.get(1);

    if (intToString.containsKey(3) && intToString.containsKey(5)) {
      number = 2;
    } else if (one != null && containsAllChars(numberToParse, one)) {
      number = 3;
    } else if (six != null && containsAllChars(six, numberToParse)) {
      number = 5;
    }
    return number;
  }

  private static int parseLength6(
      Map<Integer, String> intToString, String numberToParse, int number) {
    final String three = intToString.get(3);
    final String eight = intToString.get(8);
    final String one = intToString.get(1);

    if (three != null && containsAllChars(numberToParse, three)) {
      number = 9;
    } else if (intToString.containsKey(0) && intToString.containsKey(9)) {
      number = 6;
    } else if (one != null
        && three != null
        && eight != null
        && containsAllChars(numberToParse, one)
        && containsAllChars(eight, numberToParse)) {
      number = 0;
    }
    return number;
  }

  public static Set<Character> stringToCharacterSet(String s) {
    Set<Character> set = new HashSet<>();
    for (char c : s.toCharArray()) {
      set.add(c);
    }
    return set;
  }

  public static boolean containsAllChars(String container, String containee) {
    return stringToCharacterSet(container).containsAll(stringToCharacterSet(containee));
  }
}
