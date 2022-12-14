package dev.mqqx.aoc.year22;

import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day14 {

  record Waterfall(int leftEdge, int rightEdge, int depth) {}

  static int solvePart1(Resource input) {
    return solve(input, false);
  }

  private static int dropSand(String[][] waterfallBoard, int sandDropX, boolean isPart2) {
    final Point startingPoint = new Point(sandDropX, 0);
    int sandCounter = 0;
    boolean dropSand = true;
    while (dropSand) {
      dropSand = dropOneSand(waterfallBoard, startingPoint, startingPoint, startingPoint, isPart2);
      if (dropSand || isPart2) {
        sandCounter++;
      }
    }

    printBoard(waterfallBoard);

    return sandCounter;
  }

  private static boolean dropOneSand(
      String[][] waterfallBoard,
      Point currentPoint,
      Point lastPoint,
      Point startingPoint,
      boolean isPart2) {
    for (int i = currentPoint.y; i < waterfallBoard.length; i++) {

      final String currentValue = waterfallBoard[i][currentPoint.x];

      if (" ".equals(currentValue)) {
        lastPoint = new Point(currentPoint.x, i);
      } else {
        // maybe check for ~ if used

        // should be sand or wall, therefore check left and right

        boolean isOverflowing =
            currentPoint.x - 1 < 0 || currentPoint.x + 1 > waterfallBoard[0].length - 1;

        boolean isLeftBlocked =
            currentPoint.x - 1 < 0 || !" ".equals(waterfallBoard[i][currentPoint.x - 1]);
        boolean isRightBlocked =
            currentPoint.x + 1 > waterfallBoard[0].length - 1
                || !" ".equals(waterfallBoard[i][currentPoint.x + 1]);

        if (isLeftBlocked && isRightBlocked) {
          if (isOverflowing) {
            return false;
          } else if (isPart2 && lastPoint.equals(startingPoint)) {
            waterfallBoard[lastPoint.y][lastPoint.x] = "o";
            return false;
          }

          waterfallBoard[lastPoint.y][lastPoint.x] = "o";
          return true;
        } else if (!isLeftBlocked) {
          return dropOneSand(
              waterfallBoard, new Point(currentPoint.x - 1, i), lastPoint, startingPoint, isPart2);
        } else {
          return dropOneSand(
              waterfallBoard, new Point(currentPoint.x + 1, i), lastPoint, startingPoint, isPart2);
        }
      }
    }
    return false;
  }

  private static String[][] extendBoard(String[][] waterfallBoard, int i) {
    String[][] extendedWaterfallBoard =
        new String[waterfallBoard.length][waterfallBoard[0].length + 2];
    for (int k = 0; k < waterfallBoard.length; k++) {
      for (int j = 0; j < waterfallBoard[0].length + 2; j++) {
        if (j == 0 || j > waterfallBoard[0].length - 1) {
          if (k == waterfallBoard.length - 1) {
            extendedWaterfallBoard[i][j] = "#";
          } else {
            extendedWaterfallBoard[i][j] = " ";
          }
        } else {
          extendedWaterfallBoard[i][j + 1] = waterfallBoard[i][j];
        }
      }
    }
    return extendedWaterfallBoard;
  }

  private static void drawRocks(
      Waterfall[] waterfall, List<List<Point>> rocks, String[][] waterfallBoard, boolean isPart2) {
    final int extension = isPart2 ? 160 : 0;

    rocks.forEach(
        stone -> {
          Point lastPoint = null;
          for (final Point currentPoint : stone) {
            if (lastPoint == null) {
              waterfallBoard[currentPoint.y][extension + currentPoint.x - waterfall[0].leftEdge] =
                  "#";
            } else {

              final int xGap = Math.abs(currentPoint.x - lastPoint.x);
              for (int j = 0; j < xGap; j++) {
                final int xTo = lastPoint.x - waterfall[0].leftEdge + j;
                final int xToMark = currentPoint.x - lastPoint.x < 0 ? xTo - xGap : xTo;
                waterfallBoard[currentPoint.y][extension + xToMark] = "#";
              }

              final int yGap = Math.abs(currentPoint.y - lastPoint.y);
              for (int j = 0; j < yGap + 1; j++) {
                final int yTo = lastPoint.y + j;
                final int yToMark = currentPoint.y - lastPoint.y < 0 ? yTo - yGap : yTo;
                waterfallBoard[yToMark][extension + currentPoint.x - waterfall[0].leftEdge] = "#";
              }
            }

            lastPoint = currentPoint;
          }
        });

    if (isPart2) {
      for (int i = 0; i < waterfallBoard[0].length; i++) {
        waterfallBoard[waterfallBoard.length - 1][i] = "#";
      }
    }
  }

  private static void initializeBoard(String[][] waterfallBoard, int sandDropX) {
    for (int i = 0; i < waterfallBoard.length; i++) {
      for (int j = 0; j < waterfallBoard[i].length; j++) {
        //        if (i == 0 && j == sandDropX) {
        //          waterfallBoard[i][j] = "+";
        //        } else {
        waterfallBoard[i][j] = " ";
        //        }
      }
    }
  }

  private static List<List<Point>> getRocks(Resource input, Waterfall[] waterfall) {
    return SplitUtils.lines(input)
        .map(
            line ->
                Arrays.stream(line.split(" -> "))
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

  private static void printBoard(String[][] waterfallBoard) {
    for (int i = 0; i < waterfallBoard.length; i++) {
      for (int j = 0; j < waterfallBoard[i].length; j++) {
        System.out.print(waterfallBoard[i][j]);
      }
      System.out.println();
    }
    System.out.println();
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

    final String[][] waterfallBoard = new String[depth][width];
    initializeBoard(waterfallBoard, sandDropX);
    drawRocks(waterfall, rocks, waterfallBoard, isPart2);

    return dropSand(waterfallBoard, sandDropX, isPart2);
  }
}
