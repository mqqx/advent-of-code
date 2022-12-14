package dev.mqqx.aoc.year22;

import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day14 {

  record Waterfall(int leftEdge, int rightEdge, int depth) {}

  static int solvePart1(Resource input) {
    final Waterfall[] waterfall = {new Waterfall(500, 500, 0)};

    final List<List<Point>> stones = getStones(input, waterfall);
    final int width = waterfall[0].rightEdge - waterfall[0].leftEdge + 1;
    final int depth = waterfall[0].depth + 1;
    final String[][] waterfallBoard = new String[depth][width];

    final int sandDropX = 500 - waterfall[0].leftEdge;

    initializeBoard(waterfallBoard, sandDropX);
    drawStones(waterfall, stones, waterfallBoard);

    printBoard(waterfallBoard);

    return dropSand(waterfallBoard, sandDropX);
  }

  private static int dropSand(String[][] waterfallBoard, int sandDropX) {
    final Point startingPoint = new Point(sandDropX, 1);
    int sandCounter = 0;
    boolean dropSand = true;
    while (dropSand) {
      // drop one sand
      dropSand = dropOneSand(waterfallBoard, startingPoint, null);
      if (dropSand) {
        sandCounter++;
      }
    }

    printBoard(waterfallBoard);

    return sandCounter;
  }

  private static boolean dropOneSand(
      String[][] waterfallBoard, Point currentPoint, Point lastPoint) {
    for (int i = currentPoint.y; i < waterfallBoard.length; i++) {

      final String currentValue = waterfallBoard[i][currentPoint.x];

      if (i == 1 && "~".equals(currentValue)) {
        return false;
      }

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
          }

          waterfallBoard[lastPoint.y][lastPoint.x] = "o";
          return true;
        } else if (!isLeftBlocked) {
          return dropOneSand(waterfallBoard, new Point(currentPoint.x - 1, i), lastPoint);
        } else {
          return dropOneSand(waterfallBoard, new Point(currentPoint.x + 1, i), lastPoint);
        }
      }
    }
    return false;
  }

  private static void drawStones(
      Waterfall[] waterfall, List<List<Point>> stones, String[][] waterfallBoard) {
    stones.forEach(
        stone -> {
          Point lastPoint = null;
          for (final Point currentPoint : stone) {

            if (lastPoint == null) {
              waterfallBoard[currentPoint.y][currentPoint.x - waterfall[0].leftEdge] = "#";
            } else {

              final int xGap = Math.abs(currentPoint.x - lastPoint.x);
              for (int j = 0; j < xGap; j++) {
                final int xTo = lastPoint.x - waterfall[0].leftEdge + j;
                final int xToMark = currentPoint.x - lastPoint.x < 0 ? xTo - xGap : xTo;
                waterfallBoard[currentPoint.y][xToMark] = "#";
              }

              final int yGap = Math.abs(currentPoint.y - lastPoint.y);
              for (int j = 0; j < yGap + 1; j++) {
                final int yTo = lastPoint.y + j;
                final int yToMark = currentPoint.y - lastPoint.y < 0 ? yTo - yGap : yTo;
                waterfallBoard[yToMark][currentPoint.x - waterfall[0].leftEdge] = "#";
              }
            }

            lastPoint = currentPoint;
          }
        });
  }

  private static void initializeBoard(String[][] waterfallBoard, int sandDropX) {
    for (int i = 0; i < waterfallBoard.length; i++) {
      for (int j = 0; j < waterfallBoard[i].length; j++) {
        if (i == 0 && j == sandDropX) {
          waterfallBoard[i][j] = "+";
        } else {
          waterfallBoard[i][j] = " ";
        }
      }
    }
  }

  private static List<List<Point>> getStones(Resource input, Waterfall[] waterfall) {
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
    final Stream<String> strings = SplitUtils.lines(input);

    return 157;
  }
}
