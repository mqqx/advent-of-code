package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.toGrid;
import static java.util.Comparator.reverseOrder;

import com.google.common.util.concurrent.AtomicLongMap;
import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day9 {

  static int solvePart1(Resource input) {
    final int[][] heightMap = toGrid(input);

    return sumOfLowPoints(heightMap);
  }

  static long solvePart2(Resource input) {
    final HeightBasin[][] heightMap = initHeightMapWithBasin(input);
    final HeightBasin borderBasin = new HeightBasin(Integer.MAX_VALUE, null);
    final AtomicLongMap<Integer> basinMap = AtomicLongMap.create();

    int basin = 0;

    for (int y = 0; y < heightMap[0].length; y++) {
      for (int x = 0; x < heightMap.length; x++) {
        final HeightBasin current = heightMap[x][y];
        final int height = current.height();
        final HeightBasin left = x == 0 ? borderBasin : heightMap[x - 1][y];
        final HeightBasin top = y == 0 ? borderBasin : heightMap[x][y - 1];

        int basinToSet;

        // ignore all fields with a 9 as there won't be any flow
        if (height == 9) {
          continue;
          // merge left basin with top basin, as they are always one basin if top, left, as well as
          // current are < 9
        } else if (top.height() < 9 && left.height() < 9) {
          basinToSet = top.basin();
          final int basinToRemove = left.basin();

          if (basinToSet != basinToRemove) {
            final long remove = basinMap.remove(basinToRemove);
            basinMap.addAndGet(basinToSet, remove);
            updateBasin(heightMap, basinToRemove, basinToSet);
          }
        } else if (left.height() < 9) {
          basinToSet = left.basin();
        } else if (top.height() < 9) {
          basinToSet = top.basin();
        } else {
          basinToSet = basin;
          basin++;
        }

        heightMap[x][y] = new HeightBasin(current.height(), basinToSet);
        basinMap.getAndIncrement(basinToSet);
      }
    }

    return basinMap.asMap().values().stream()
        .sorted(reverseOrder())
        .limit(3)
        .reduce(1L, (a, b) -> a * b);
  }

  record HeightBasin(int height, Integer basin) {}

  private static int sumOfLowPoints(int[][] heightMap) {
    int sum = 0;
    for (int y = 0; y < heightMap[0].length; y++) {
      for (int x = 0; x < heightMap.length; x++) {
        final int cur = heightMap[x][y];
        boolean isLowerThanLeft = x == 0 || heightMap[x - 1][y] > cur;
        boolean isLowerThanRight = x == heightMap.length - 1 || heightMap[x + 1][y] > cur;
        boolean isLowerThanUp = y == 0 || heightMap[x][y - 1] > cur;
        boolean isLowerThanDown = y == heightMap[0].length - 1 || heightMap[x][y + 1] > cur;

        if (isLowerThanLeft && isLowerThanRight && isLowerThanUp && isLowerThanDown) {
          sum += 1 + cur;
        }
      }
    }

    return sum;
  }

  // TODO could use points instead of an array and directly update a list of linked points instead
  // of iterating over the whole map
  private static void updateBasin(HeightBasin[][] heightBasins, int basinToReplace, int newBasin) {
    for (int y = 0; y < heightBasins[0].length; y++) {
      for (int x = 0; x < heightBasins.length; x++) {
        final HeightBasin heightBasin = heightBasins[x][y];
        if (heightBasin.basin() != null && heightBasin.basin() == basinToReplace) {
          heightBasins[x][y] = new HeightBasin(heightBasin.height(), newBasin);
        }
      }
    }
  }

  private static HeightBasin[][] initHeightMapWithBasin(Resource input) {
    final List<String> heights = SplitUtils.linesList(input);

    final HeightBasin[][] heightMap = new HeightBasin[heights.get(0).length()][heights.size()];

    for (int y = 0; y < heights.size(); y++) {
      String string = heights.get(y);
      char[] charArray = string.toCharArray();
      for (int x = 0; x < charArray.length; x++) {
        heightMap[x][y] = new HeightBasin(charArray[x] - '0', null);
      }
    }
    return heightMap;
  }
}
