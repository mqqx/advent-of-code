package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.util.stream.IntStream.range;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day13 {

  static int solvePart1(Resource input) {
    final JsonParser parser = JsonParserFactory.getJsonParser();
    final String[][] pairs =
        lines(input, "\n\n").map(pairString -> pairString.split("\n")).toArray(String[][]::new);

    return range(0, pairs.length)
        .filter(i -> compare(parser.parseList(pairs[i][0]), parser.parseList(pairs[i][1])) > -1)
        .map(i -> i + 1)
        .sum();
  }

  static int solvePart2(Resource input) {
    final JsonParser parser = JsonParserFactory.getJsonParser();
    final String signalsString = read(input).replace("\n\n", "\n") + "\n[[2]]\n[[6]]";
    final List<List<Object>> orderedSignalLists =
        signalsString
            .lines()
            .map(parser::parseList)
            .sorted(
                (a, b) ->
                    switch (compare(a, b)) {
                      case 1 -> -1;
                      case -1 -> 1;
                      default -> 0;
                    })
            .toList();

    final int index2 = 1 + orderedSignalLists.indexOf(List.of(List.of(2L)));
    final int index6 = 1 + orderedSignalLists.indexOf(List.of(List.of(6L)));
    return index2 * index6;
  }

  private static int compare(Object left, Object right) {
    final boolean isLeftList = left instanceof List<?>;
    final boolean isRightList = right instanceof List<?>;

    if (isLeftList && isRightList) {
      return compareLists((List<?>) left, (List<?>) right);
    } else if (isLeftList || isRightList) {
      return compareLists(toList(left), toList(right));
    } else {
      if ((long) left > (long) right) {
        return -1;
      } else if ((long) left < (long) right) {
        return 1;
      }
    }

    return 0;
  }

  private static int compareLists(List<?> left, List<?> right) {
    final boolean isLeftEmpty = left.isEmpty();
    final boolean isRightEmpty = right.isEmpty();

    if (isLeftEmpty && isRightEmpty) {
      return 0;
    } else if (isLeftEmpty) {
      return 1;
    } else if (isRightEmpty) {
      return -1;
    } else {
      final int comparison = compare(left.get(0), right.get(0));

      if (comparison == 0) {
        return compare(left.subList(1, left.size()), right.subList(1, right.size()));
      }

      return comparison;
    }
  }

  private static List<Object> toList(Object leftObject) {
    if (leftObject instanceof Long) {
      return List.of(leftObject);
    }
    return (List<Object>) leftObject;
  }
}
