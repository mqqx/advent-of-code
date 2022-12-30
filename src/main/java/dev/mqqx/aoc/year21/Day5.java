package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day5 {

  static long solvePart1(Resource input) {
    return solve(input, Day5::inBetweenHorizontalOrVertical);
  }

  static int solvePart2(Resource input) {
    return solve(input, Day5::inBetweenHorizontalOrVerticalOrDiagonal);
  }

  private static int solve(Resource input, Function<String[], Set<Point>> inBetween) {
    final Set<Point> coveredPoints = new HashSet<>();
    final Set<Point> overlappingPoints = new HashSet<>();

    lines(input)
        .map(line -> line.split(" -> "))
        .map(inBetween)
        .forEach(
            points ->
                points.forEach(
                    p -> {
                      final boolean addResult = coveredPoints.add(p);
                      if (!addResult) {
                        overlappingPoints.add(p);
                      }
                    }));

    return overlappingPoints.size();
  }

  private static Set<Point> inBetweenHorizontalOrVertical(String[] points) {
    return toPoint(points[0]).inBetweenHorizontalOrVertical(toPoint(points[1]));
  }

  private static Set<Point> inBetweenHorizontalOrVerticalOrDiagonal(String[] points) {
    return toPoint(points[0]).inBetweenHorizontalOrVerticalOrDiagonal(toPoint(points[1]));
  }

  private static Point toPoint(String pointString) {
    final String[] pointXAndY = pointString.split(",");

    return new Point(parseInt(pointXAndY[0]), parseInt(pointXAndY[1]));
  }
}
