package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static dev.mqqx.aoc.util.SplitUtils.read;

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
    final List<String> strings = linesList(input, "\n\n");
    final JsonParser parser = JsonParserFactory.getJsonParser();

    int sum = 0;

    for (int i = 0; i < strings.size(); i++) {
      final String[] listStrings = strings.get(i).split("\n");
      final List<Object> leftList = parser.parseList(listStrings[0]);
      final List<Object> rightList = parser.parseList(listStrings[1]);

      final int isOrdered = compare(leftList, rightList);
      if (isOrdered > -1) {
        sum += i + 1;
      }
    }

    return sum;
  }

  static int solvePart2(Resource input) {
    final JsonParser parser = JsonParserFactory.getJsonParser();

    final String signalString = read(input).replace("\n\n", "\n") + "\n[[2]]\n[[6]]";
    final List<List<Object>> lists =
        signalString
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

    int index2 = 1;
    int index6 = 1;

    for (int i = 0; i < lists.size(); i++) {
      if (lists.get(i).equals(List.of(List.of(2L)))) {
        index2 += i;
      } else if (lists.get(i).equals(List.of(List.of(6L)))) {
        index6 += i;
      }
    }

    return index2 * index6;
  }

  private static int compare(Object left, Object right) {
    final boolean isLeftList = left instanceof List<?>;
    final boolean isRightList = right instanceof List<?>;

    if (isLeftList && isRightList) {
      final List<?> leftList = (List<?>) left;
      final List<?> rightList = (List<?>) right;
      final boolean isLeftEmpty = leftList.isEmpty();
      final boolean isRightEmpty = ((List<?>) right).isEmpty();
      if (isLeftEmpty && isRightEmpty) {
        return 0;
      } else if (isLeftEmpty) {
        return 1;
      } else if (isRightEmpty) {
        return -1;
      } else {
        final int comparison = compare(leftList.get(0), rightList.get(0));

        if (comparison == 0) {
          return compare(
              leftList.subList(1, leftList.size()), rightList.subList(1, rightList.size()));
        }

        return comparison;
      }

    } else if (isLeftList || isRightList) {
      return compare(toList(left), toList(right));
    } else {
      if ((long) left > (long) right) {
        return -1;
      } else if ((long) left < (long) right) {
        return 1;
      }
    }

    return 0;
  }

  private static List<Object> toList(Object leftObject) {
    if (leftObject instanceof Long) {
      return List.of(leftObject);
    }
    return (List<Object>) leftObject;
  }
}
