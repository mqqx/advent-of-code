package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.Arrays.fill;
import static java.util.Arrays.stream;

import java.awt.Point;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day14 {

  record Waterfall(int leftEdge, int rightEdge, int depth) {}

  static int solvePart1(Resource input) {
    return solve(input, false);
  }

  private static int dropSand(String[][] waterfallGrid, int sandDropX, boolean isPart2) {
    final Point startingPoint = new Point(sandDropX, 0);
    int sandCounter = 0;
    boolean dropSand = true;
    while (dropSand) {
      dropSand = dropOneSand(waterfallGrid, startingPoint, startingPoint, startingPoint, isPart2);
      if (dropSand || isPart2) {
        sandCounter++;
      }
    }

    printBoard(waterfallGrid);

    return sandCounter;
  }

  private static boolean dropOneSand(
      String[][] waterfallGrid,
      Point currentPoint,
      Point lastPoint,
      Point startingPoint,
      boolean isPart2) {
    for (int y = currentPoint.y; y < waterfallGrid.length; y++) {
      final String currentValue = waterfallGrid[y][currentPoint.x];

      if (" ".equals(currentValue)) {
        lastPoint = new Point(currentPoint.x, y);
      } else {
        return dropOneSandIfNotOverflowing(
            waterfallGrid, currentPoint, lastPoint, startingPoint, isPart2, y);
      }
    }
    return false;
  }

  private static boolean dropOneSandIfNotOverflowing(
      String[][] waterfallGrid,
      Point currentPoint,
      Point lastPoint,
      Point startingPoint,
      boolean isPart2,
      int y) {
    boolean isOverflowing =
        currentPoint.x - 1 < 0 || currentPoint.x + 1 > waterfallGrid[0].length - 1;

    boolean isLeftBlocked =
        currentPoint.x - 1 < 0 || !" ".equals(waterfallGrid[y][currentPoint.x - 1]);
    boolean isRightBlocked =
        currentPoint.x + 1 > waterfallGrid[0].length - 1
            || !" ".equals(waterfallGrid[y][currentPoint.x + 1]);

    if (isLeftBlocked && isRightBlocked) {
      if (!isOverflowing) {
        waterfallGrid[lastPoint.y][lastPoint.x] = "o";
      }

      return !(isOverflowing || isPart2 && lastPoint.equals(startingPoint));
    }

    // check point to the left, if its blocked, check point to the right
    final int x = isLeftBlocked ? currentPoint.x + 1 : currentPoint.x - 1;

    return dropOneSand(waterfallGrid, new Point(x, y), lastPoint, startingPoint, isPart2);
  }

  private static void drawRocks(
      Waterfall[] waterfall, List<List<Point>> rocks, String[][] waterfallGrid, boolean isPart2) {
    final int extension = isPart2 ? 160 : 0;

    rocks.forEach(
        rock -> {
          Point lastPoint = null;
          for (final Point currentPoint : rock) {
            if (lastPoint == null) {
              waterfallGrid[currentPoint.y][extension + currentPoint.x - waterfall[0].leftEdge] =
                  "#";
            } else {
              drawX(waterfall, waterfallGrid, extension, lastPoint, currentPoint);
              drawY(waterfall, waterfallGrid, extension, lastPoint, currentPoint);
            }

            lastPoint = currentPoint;
          }
        });

    if (isPart2) {
      for (int i = 0; i < waterfallGrid[0].length; i++) {
        waterfallGrid[waterfallGrid.length - 1][i] = "#";
      }
    }
  }

  private static void drawY(
      Waterfall[] waterfall,
      String[][] waterfallGrid,
      int extension,
      Point lastPoint,
      Point currentPoint) {
    final int yGap = abs(currentPoint.y - lastPoint.y);
    for (int j = 0; j < yGap + 1; j++) {
      final int yTo = lastPoint.y + j;
      final int yToMark = currentPoint.y - lastPoint.y < 0 ? yTo - yGap : yTo;
      waterfallGrid[yToMark][extension + currentPoint.x - waterfall[0].leftEdge] = "#";
    }
  }

  private static void drawX(
      Waterfall[] waterfall,
      String[][] waterfallGrid,
      int extension,
      Point lastPoint,
      Point currentPoint) {
    final int xGap = abs(currentPoint.x - lastPoint.x);
    for (int j = 0; j < xGap; j++) {
      final int xTo = lastPoint.x - waterfall[0].leftEdge + j;
      final int xToMark = currentPoint.x - lastPoint.x < 0 ? xTo - xGap : xTo;
      waterfallGrid[currentPoint.y][extension + xToMark] = "#";
    }
  }

  private static List<List<Point>> getRocks(Resource input, Waterfall[] waterfall) {
    return lines(input)
        .map(
            line ->
                stream(line.split(" -> "))
                    .map(
                        point -> {
                          final String[] points = point.split(",");
                          final int x = parseInt(points[0]);
                          final int y = parseInt(points[1]);

                          if (x < waterfall[0].leftEdge) {
                            waterfall[0] =
                                new Waterfall(x, waterfall[0].rightEdge, waterfall[0].depth);
                          } else if (x > waterfall[0].rightEdge) {
                            waterfall[0] =
                                new Waterfall(waterfall[0].leftEdge, x, waterfall[0].depth);
                          }

                          if (y > waterfall[0].depth) {
                            waterfall[0] =
                                new Waterfall(waterfall[0].leftEdge, waterfall[0].rightEdge, y);
                          }

                          return new Point(x, y);
                        })
                    .toList())
        .toList();
  }

  private static void printBoard(String[][] waterfallGrid) {
    final StringBuilder board = new StringBuilder();
    for (String[] strings : waterfallGrid) {
      for (String string : strings) {
        board.append(string);
      }
      board.append(System.lineSeparator());
    }
    log.debug(board.toString());
  }

  static int solvePart2(Resource input) {
    return solve(input, true);
  }

  private static int solve(Resource input, boolean isPart2) {
    final Waterfall[] waterfall = {new Waterfall(500, 500, 0)};
    final List<List<Point>> rocks = getRocks(input, waterfall);

    int width = waterfall[0].rightEdge - waterfall[0].leftEdge + 1;
    int depth = waterfall[0].depth + 1;
    int sandDropX = 500 - waterfall[0].leftEdge;

    if (isPart2) {
      depth += 2;
      width += 320;
      sandDropX += 160;
    }

    final String[][] waterfallGrid = new String[depth][width];
    for (String[] strings : waterfallGrid) {
      fill(strings, " ");
    }
    drawRocks(waterfall, rocks, waterfallGrid, isPart2);

    return dropSand(waterfallGrid, sandDropX, isPart2);
  }
}
