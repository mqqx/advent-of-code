package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.NumberUtils.minNForSumToNBySum;
import static dev.mqqx.aoc.util.NumberUtils.sumToN;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day17 {

  static int solvePart1(Resource input) {
    final TargetArea targetArea = TargetArea.parse(input);
    int yMax = 0;
    int x = minNForSumToNBySum(targetArea.x().lowerEndpoint());
    for (; x <= targetArea.x().upperEndpoint(); x++) {
      for (int y = 0; y < abs(targetArea.y().lowerEndpoint()); y++) {
        final int calcYMax = calcYMax(x, y, targetArea);
        if (calcYMax > yMax) {
          yMax = calcYMax;
        }
      }
    }

    return yMax;
  }

  static int solvePart2(Resource input) {
    final TargetArea targetArea = TargetArea.parse(input);
    int totalCount = 0;

    int x = minNForSumToNBySum(targetArea.x().lowerEndpoint());
    for (; x <= targetArea.x().upperEndpoint(); x++) {
      for (int y = targetArea.y().lowerEndpoint(); y < abs(targetArea.y().lowerEndpoint()); y++) {
        if (canReachTarget(x, y, targetArea)) {
          totalCount++;
        }
      }
    }

    return totalCount;
  }

  private static int calcYMax(int xVelocity, int yVelocity, TargetArea targetArea) {
    int x = 0;
    int y = 0;

    int yMax = 0;

    do {
      x += xVelocity;
      y += yVelocity;

      if (y > yMax) {
        yMax = y;
      }

      if (targetArea.doesContain(x, y)) {
        return yMax;
      }

      if (xVelocity != 0) {
        xVelocity--;
      }
      yVelocity--;
    } while (targetArea.canBeReached(x, y, xVelocity));

    return 0;
  }

  private static boolean canReachTarget(int xVelocity, int yVelocity, TargetArea targetArea) {
    int x = 0;
    int y = 0;

    do {
      x += xVelocity;
      y += yVelocity;

      if (targetArea.doesContain(x, y)) {
        return true;
      }

      if (xVelocity != 0) {
        xVelocity--;
      }
      yVelocity--;
    } while (targetArea.canBeReached(x, y, xVelocity));

    return false;
  }

  private record TargetArea(Range<Integer> x, Range<Integer> y) {
    TargetArea(int xMin, int xMax, int yMin, int yMax) {
      this(Range.closed(xMin, xMax), Range.closed(yMin, yMax));
    }

    boolean doesContain(int x, int y) {
      return this.x.contains(x) && this.y.contains(y);
    }

    boolean canBeReached(int x, int y, int xVelocity) {
      final boolean isInReach = x <= this.x.upperEndpoint() && y >= this.y.lowerEndpoint();
      final boolean canBeReached = x + sumToN(xVelocity) >= this.x.lowerEndpoint();

      return isInReach && canBeReached;
    }

    static TargetArea parse(Resource input) {
      final String[] parts = read(input).split(" |, ");
      final String xPart = parts[2];
      final String yPart = parts[3];
      return new TargetArea(parseMin(xPart), parseMax(xPart), parseMin(yPart), parseMax(yPart));
    }

    private static int parseMax(String part) {
      return parseInt(part.substring(part.lastIndexOf('.') + 1));
    }

    private static int parseMin(String part) {
      return parseInt(part.substring(2, part.indexOf('.')));
    }
  }
}
