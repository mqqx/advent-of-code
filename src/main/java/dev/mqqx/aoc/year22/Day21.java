package dev.mqqx.aoc.year22;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static org.apache.logging.log4j.util.Strings.join;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day21 {

  record MonkeyNumber(String monkey, long num) {}

  static long solvePart1(Resource input) {
    return solve(input, false);
  }

  static long solvePart2(Resource input) {
    return solve(input, true);
  }

  private static long solve(Resource input, boolean isPart2) {
    List<String> list = new ArrayList<>();
    final Long result = reduceBySolvingPossibleEquations(input, isPart2, list);
    if (result != null) {
      return result;
    }

    final MonkeyNumber root = getMonkeyEqualToExistingRootNumber(list);

    String bonkeyNumbers = join(list, '\n').replaceAll(root.monkey, valueOf(root.num));

    while (true) {
      list.clear();
      list.addAll(filter(bonkeyNumbers));

      for (int i = list.size() - 1; i >= 0; i--) {
        final String line = list.get(i);
        try {
          final MonkeyNumber monkeyNumber = getMonkeyToReplace(line.split(" "));

          if ("humn".equals(monkeyNumber.monkey)) {
            return monkeyNumber.num;
          }

          list.remove(i);
          bonkeyNumbers =
              bonkeyNumbers
                  .replaceAll(line, "")
                  .replaceAll(monkeyNumber.monkey, valueOf(monkeyNumber.num));

        } catch (NumberFormatException ignored) {
        }
      }
    }
  }

  private static Long reduceBySolvingPossibleEquations(
      Resource input, boolean isPart2, List<String> list) {
    String monkeyNumbers = SplitUtils.read(input);

    String lastMonkeyNumbers = null;
    long result;

    while (true) {
      list.clear();
      list.addAll(filter(monkeyNumbers));

      if (isPart2 && monkeyNumbers.equals(lastMonkeyNumbers)) {
        break;
      }

      lastMonkeyNumbers = monkeyNumbers;

      for (int i = list.size() - 1; i >= 0; i--) {
        final String line = list.get(i);
        final String[] splitLine = line.split(": ");
        final String currentMonkey = splitLine[0];

        if (isPart2 && "humn".equals(currentMonkey)) {
          continue;
        }

        try {
          result = calculateResult(splitLine[1]);

          if (!isPart2 && "root".equals(currentMonkey)) {
            return result;
          }

          list.remove(i);
          monkeyNumbers =
              monkeyNumbers.replaceAll(line, "").replaceAll(currentMonkey, valueOf(result));
        } catch (NumberFormatException ignored) {
        }
      }
    }
    return null;
  }

  private static MonkeyNumber getMonkeyToReplace(String[] splitLine) {
    final long res = parseLong(splitLine[0].substring(0, splitLine[0].length() - 1));

    final String first = splitLine[1];
    final String last = splitLine[3];
    final boolean isLastVariableNumber = last.matches("\\d+");
    final String monkeyToReplace = isLastVariableNumber ? first : last;

    final long result;

    if (isLastVariableNumber) {
      result =
          switch (splitLine[2]) {
            case "+" -> res - parseLong(last);
            case "-" -> res + parseLong(last);
            case "*" -> res / parseLong(last);
            case "/" -> res * parseLong(last);
            default -> -1;
          };
    } else {
      result =
          switch (splitLine[2]) {
            case "+" -> res - parseLong(first);
            case "-" -> parseLong(first) - res;
            case "*" -> res / parseLong(first);
            case "/" -> parseLong(first) / res;
            default -> -1;
          };
    }

    return new MonkeyNumber(monkeyToReplace, result);
  }

  private static MonkeyNumber getMonkeyEqualToExistingRootNumber(List<String> monkeyNumbers) {
    return monkeyNumbers.stream()
        .filter(line -> line.startsWith("root:"))
        .map(
            line -> {
              final String[] splitRoot = line.split(" ");

              final MonkeyNumber root;
              if (splitRoot[1].matches("\\d+")) {
                root = new MonkeyNumber(splitRoot[3], parseLong(splitRoot[1]));
              } else {
                root = new MonkeyNumber(splitRoot[1], parseLong(splitRoot[3]));
              }

              return root;
            })
        .findFirst()
        .orElseThrow();
  }

  // list needs to be modifiable, as solved monkey equations will be removed
  @java.lang.SuppressWarnings("squid:S6204")
  private static List<String> filter(String monkeyNumbers) {
    return monkeyNumbers
        .lines()
        .filter(line -> !line.isBlank())
        .filter(line -> line.matches(".*[a-z].*"))
        .collect(Collectors.toList());
  }

  private static long calculateResult(String numberOrExpression) {
    final String[] splitNumberOrExpression = numberOrExpression.split(" ");

    if (splitNumberOrExpression.length == 1) {
      return parseLong(numberOrExpression);
    } else {
      long part1 = parseLong(splitNumberOrExpression[0]);
      long part2 = parseLong(splitNumberOrExpression[2]);

      return switch (splitNumberOrExpression[1]) {
        case "+" -> part1 + part2;
        case "-" -> part1 - part2;
        case "*" -> part1 * part2;
        case "/" -> part1 / part2;
        default -> -1;
      };
    }
  }
}
