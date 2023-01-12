package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Integer.parseInt;

import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day17 {

  static int solvePart1(Resource input) {
    final TargetArea targetArea = TargetArea.parse(input);
    // x19,y48
    int yMax = 0;

    // FIXME implement proper ranges
    for (int x = 0; x < 50; x++) {
      for (int y = 0; y < 150; y++) {
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

    for (int x = 0; x < 250; x++) {
      for (int y = -109; y < 600; y++) {
        totalCount += calcYMax2(x, y, targetArea);
      }
    }

    return totalCount;
  }

  private static int calcYMax(int xVelocity, int yVelocity, TargetArea targetArea) {
    int x = 0;
    int y = 0;

    int yMax = 0;
    int steps = 0;

    do {
      x += xVelocity;
      y += yVelocity;

      if (y > yMax) {
        yMax = y;
      }
      steps++;
      // FIXME implement proper end condition
      if (steps == 250) {
        return -1;
      }

      if (xVelocity != 0) {
        xVelocity--;
      }
      yVelocity--;
    } while (targetArea.doesNotContain(x, y));

    return yMax;
  }

  private static int calcYMax2(int xVelocity, int yVelocity, TargetArea targetArea) {
    int x = 0;
    int y = 0;

    int steps = 0;

    do {
      x += xVelocity;
      y += yVelocity;

      steps++;
      // FIXME implement proper end condition
      if (steps == 2_500) {
        return 0;
      }

      if (xVelocity != 0) {
        xVelocity--;
      }
      yVelocity--;
    } while (targetArea.doesNotContain(x, y));

    return 1;
  }

  private record TargetArea(Range<Integer> x, Range<Integer> y) {
    TargetArea(int xMin, int xMax, int yMin, int yMax) {
      this(Range.closed(xMin, xMax), Range.closed(yMin, yMax));
    }

    boolean doesNotContain(int x, int y) {
      return !this.x.contains(x) || !this.y.contains(y);
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
