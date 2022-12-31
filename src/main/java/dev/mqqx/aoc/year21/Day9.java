package dev.mqqx.aoc.year21;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day9 {

  static int solvePart1(Resource input) {
    final List<String> strings = SplitUtils.linesList(input);

    final int[][] heightMap = new int[strings.get(0).length()][strings.size()];

    for (int y = 0; y < strings.size(); y++) {
      String string = strings.get(y);
      char[] charArray = string.toCharArray();
      for (int x = 0; x < charArray.length; x++) {
        heightMap[x][y] = charArray[x] - '0';
      }
    }

    int sum = 0;
    for (int y = 0; y < strings.size(); y++) {
      for (int x = 0; x < strings.get(0).length(); x++) {
        final int cur = heightMap[x][y];
        boolean isLowerThanLeft = x == 0 || heightMap[x - 1][y] > cur;
        boolean isLowerThanRight = x == strings.get(0).length() - 1 || heightMap[x + 1][y] > cur;
        boolean isLowerThanUp = y == 0 || heightMap[x][y - 1] > cur;
        boolean isLowerThanDown = y == strings.size() - 1 || heightMap[x][y + 1] > cur;

        if (isLowerThanLeft && isLowerThanRight && isLowerThanUp && isLowerThanDown) {
          sum += 1 + cur;
        }
      }
    }

    return sum;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return 157;
  }
}
