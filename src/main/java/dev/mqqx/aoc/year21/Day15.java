package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.toGrid;

import dev.mqqx.aoc.util.GridDimension;
import dev.mqqx.aoc.util.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day15 {

  static int solvePart1(Resource input) {
    return solve(toGrid(input));
  }

  static int solvePart2(Resource input) {
    return solve(expand(toGrid(input)));
  }

  private static int[][] expand(int[][] grid) {
    final int expansionFactor = 5;

    final int[][] expandedGrid =
        new int[grid.length * expansionFactor][grid[0].length * expansionFactor];
    for (int newY = 0; newY < expansionFactor; newY++) {
      for (int newX = 0; newX < expansionFactor; newX++) {
        for (int y = 0; y < grid.length; y++) {
          for (int x = 0; x < grid[0].length; x++) {
            int newTile = (grid[y][x] + newY + newX);
            if (newTile > 9) {
              newTile = newTile % 10 + 1;
            }
            expandedGrid[newY * grid.length + y][newX * grid[0].length + x] = newTile;
          }
        }
      }
    }
    return expandedGrid;
  }

  private static int solve(int[][] grid) {
    final GridDimension gridDimension = GridDimension.of(grid);
    final Point start = gridDimension.start();
    final Point end = gridDimension.end();

    final PriorityQueue<Point> queue = new PriorityQueue<>();
    queue.add(start);
    final Map<Point, Integer> totalRisk = new HashMap<>();
    totalRisk.put(start, 0);

    while (!queue.isEmpty()) {
      final Point current = queue.poll();

      int risk = totalRisk.get(current);
      if (end.equals(current)) {
        return risk;
      }

      final List<Point> connectingPoints =
          current.connecting().filter(gridDimension::includes).toList();

      for (Point point : connectingPoints) {
        int newRisk = risk + grid[point.y()][point.x()];
        if (newRisk < totalRisk.getOrDefault(point, Integer.MAX_VALUE)) {
          totalRisk.put(point, newRisk);
          queue.add(point);
        }
      }
    }

    return -1;
  }
}
