package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static dev.mqqx.aoc.util.SplitUtils.toPoints;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toSet;

import dev.mqqx.aoc.util.Point;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day13 {

  static long solvePart1(Resource input) {
    final String[] folding = initFoldings(input).get(0);
    final Set<Point> points = toPoints(input).collect(toSet());

    return foldPoints(points, folding).size();
  }

  static String solvePart2(Resource input) {
    final List<String[]> foldings = initFoldings(input);
    Set<Point> points = toPoints(input).collect(toSet());

    for (String[] folding : foldings) {
      points = foldPoints(points, folding);
    }

    return buildOrigami(points);
  }

  private static List<String[]> initFoldings(Resource input) {
    return lines(input)
        .filter(line -> line.startsWith("f"))
        .map(line -> line.replace("fold along ", ""))
        .map(folding -> folding.split("="))
        .toList();
  }

  private static Set<Point> foldPoints(Set<Point> points, String[] folding) {
    final boolean shouldFoldAlongX = "x".equals(folding[0]);
    final int foldAt = parseInt(folding[1]);

    return points.stream()
        .map(foldPoint(shouldFoldAlongX, foldAt))
        .filter(Point::hasPositiveXAndY)
        .collect(toSet());
  }

  private static Function<Point, Point> foldPoint(boolean shouldFoldAlongX, int foldAt) {
    return point -> {
      if (shouldFoldAlongX) {
        return point.foldAtX(foldAt);
      } else {
        return point.foldAtY(foldAt);
      }
    };
  }

  // TODO: Could extract it to a PointUtils e.g.
  private static String buildOrigami(Set<Point> points) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    for (Point point : points) {
      if (minX > point.x()) {
        minX = point.x();
      }
      if (minY > point.y()) {
        minY = point.y();
      }
      if (maxX < point.x()) {
        maxX = point.x();
      }
      if (maxY < point.y()) {
        maxY = point.y();
      }
    }

    final StringBuilder origami = new StringBuilder();

    for (int y = 0; y <= maxY; y++) {
      for (int x = 0; x <= maxX; x++) {
        if (points.contains(new Point(x, y))) {
          origami.append("#");
        } else {
          origami.append(" ");
        }
      }
      origami.append("\n");
    }

    return origami.toString();
  }
}
