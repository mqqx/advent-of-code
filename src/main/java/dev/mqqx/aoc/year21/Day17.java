package dev.mqqx.aoc.year21;

import com.google.common.collect.Range;
import dev.mqqx.aoc.util.SplitUtils;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day17 {

  static int solvePart1(Resource input) {

    //    Arrays.stream(read(input).split(" |, ")).map(part -> part.split("=")).filter(part ->
    // part.length == 2);

    // x19,y48

    int yMax = 0;

    for (int x = 0; x < 50; x++) {
      for (int y = 0; y < 150; y++) {
        final int calcYMax = calcYMax(x, y);
        if (calcYMax > yMax) {
          yMax = calcYMax;
        }
      }
    }

    return yMax;
  }

  private static int calcYMax(int xVelocity, int yVelocity) {
    //    final Range<Integer> xTarget = Range.closed(20, 30);
    final Range<Integer> xTarget = Range.closed(179, 201);
    //    final Range<Integer> yTarget = Range.closed(-10, -5);
    final Range<Integer> yTarget = Range.closed(-109, -63);
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
      if (steps == 250) {
        return -1;
      }

      if (xVelocity != 0) {
        xVelocity--;
      }
      yVelocity--;
    } while (!xTarget.contains(x) || !yTarget.contains(y));

    return yMax;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return 157;
  }
}
