package dev.mqqx.aoc.year22;

import static java.lang.Long.parseLong;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day21 {

  static long solvePart1(Resource input) {
    return solve(input, false);
  }

  record Root(String monkey, long num) {}

  private static long solve(Resource input, boolean isPart2) {
    String monkeyNumbers = SplitUtils.read(input);

    long result = 0;
    List<String> list = new ArrayList<>();
    boolean rootNotFound = true;

    String lastMonkeyNumbers = null;

    while (rootNotFound) {
      list =
          monkeyNumbers
              .lines()
              .filter(line -> !line.isBlank())
              .filter(line -> line.matches(".*[a-z].*"))
              .collect(Collectors.toList());
      monkeyNumbers = String.join("\n", list);

      if (isPart2 && monkeyNumbers.equals(lastMonkeyNumbers)) {
        break;
      }

      lastMonkeyNumbers = monkeyNumbers;

      for (int i = list.size() - 1; i >= 0; i--) {
        final String line = list.get(i);
        final String[] splitLine = line.split(": ");

        try {
          if (isPart2 && "humn".equals(splitLine[0])) {
            continue;
          }
          final String numberOrExpression = splitLine[1];

          final String[] splitNumberOrExpression = numberOrExpression.split(" ");

          if (splitNumberOrExpression.length == 1) {
            result = parseLong(numberOrExpression);
          } else {
            long part1 = parseLong(splitNumberOrExpression[0]);
            long part2 = parseLong(splitNumberOrExpression[2]);

            result =
                switch (splitNumberOrExpression[1]) {
                  case "+" -> part1 + part2;
                  case "-" -> part1 - part2;
                  case "*" -> part1 * part2;
                  case "/" -> part1 / part2;
                  default -> -1;
                };
          }

          if (!isPart2 && "root".equals(splitLine[0])) {
            rootNotFound = false;
            return result;
          }

          list.remove(i);
          monkeyNumbers =
              monkeyNumbers.replaceAll(line, "").replaceAll(splitLine[0], String.valueOf(result));
        } catch (NumberFormatException ignored) {
        }
      }
    }

    final Root first =
        monkeyNumbers
            .lines()
            .filter(line -> line.startsWith("root:"))
            .map(
                line -> {
                  final String[] splitRoot = line.split(" ");

                  final Root root;
                  if (splitRoot[1].matches("\\d+")) {
                    root = new Root(splitRoot[3], parseLong(splitRoot[1]));
                  } else {
                    root = new Root(splitRoot[1], parseLong(splitRoot[3]));
                  }

                  return root;
                })
            .findFirst()
            .orElseThrow();

    monkeyNumbers = monkeyNumbers.replaceAll(first.monkey, String.valueOf(first.num));

    while (true) {
      list =
          monkeyNumbers
              .lines()
              .filter(line -> !line.isBlank())
              .filter(line -> line.matches(".*[a-z].*"))
              .collect(Collectors.toList());
      monkeyNumbers = String.join("\n", list);

      for (int i = list.size() - 1; i >= 0; i--) {
        final String line = list.get(i);
        final String[] splitLine = line.split(" ");
        try {
          final String parsedBlud = splitLine[0].substring(0, splitLine[0].length() - 1);
          long res = parseLong(parsedBlud);

          final String a = splitLine[1];
          final String b = splitLine[3];
          final String toReplace;

          if (b.matches("\\d+")) {
            toReplace = a;
            result =
                switch (splitLine[2]) {
                  case "+" -> res - parseLong(b);
                  case "-" -> res + parseLong(b);
                  case "*" -> res / parseLong(b);
                  case "/" -> res * parseLong(b);
                  default -> -1;
                };
          } else {
            toReplace = b;
            result =
                switch (splitLine[2]) {
                  case "+" -> res - parseLong(a);
                  case "-" -> parseLong(a) - res;
                  case "*" -> res / parseLong(a);
                  case "/" -> parseLong(a) / res;
                  default -> -1;
                };
          }

          if ("humn".equals(toReplace)) {
            return result;
          }

          list.remove(i);
          monkeyNumbers =
              monkeyNumbers.replaceAll(line, "").replaceAll(toReplace, String.valueOf(result));

        } catch (NumberFormatException ignored) {
        }
      }
    }
  }

  static long solvePart2(Resource input) {
    return solve(input, true);
  }
}
