package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day18 {

  record Cube(int x, int y, int z) {}

  static int solvePart1(Resource input) {
    return getTotalSides(input, false);
  }

  private static int getTotalSides(Resource input, boolean isPart2) {
    final List<Cube> cubes = initializeCubes(input);
    final Integer[][][] grid = fillGrid(cubes);

    if (isPart2) {
      floodFill(grid);
      nullsToZeros(grid);

      int exteriorSurface = 0;
      for (Cube c : cubes) {
        exteriorSurface += grid[c.x + 1][c.y][c.z];
        exteriorSurface += grid[c.x - 1][c.y][c.z];
        exteriorSurface += grid[c.x][c.y + 1][c.z];
        exteriorSurface += grid[c.x][c.y - 1][c.z];
        exteriorSurface += grid[c.x][c.y][c.z + 1];
        exteriorSurface += grid[c.x][c.y][c.z - 1];
      }

      return exteriorSurface;
    }

    return calculateTotalSides(grid);
  }

  private static void nullsToZeros(Integer[][][] grid) {
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[0].length; y++) {
        for (int z = 0; z < grid[0][0].length; z++) {
          if (grid[x][y][z] == null) {
            grid[x][y][z] = 0;
          }
        }
      }
    }
  }

  private static void floodFill(Integer[][][] grid) {
    Queue<Cube> cubeQueue = new ArrayDeque<>();
    cubeQueue.add(new Cube(0, 0, 0));

    while (!cubeQueue.isEmpty()) {
      final Cube c = cubeQueue.poll();

      final boolean isOutsideOfGrid =
          c.x < 0
              || c.x >= grid.length
              || c.y < 0
              || c.y >= grid[0].length
              || c.z < 0
              || c.z >= grid[0][0].length;
      if (isOutsideOfGrid || grid[c.x][c.y][c.z] == null || grid[c.x][c.y][c.z] == 1) {
        continue;
      }

      grid[c.x][c.y][c.z] = 1;
      cubeQueue.add(new Cube(c.x + 1, c.y, c.z));
      cubeQueue.add(new Cube(c.x - 1, c.y, c.z));
      cubeQueue.add(new Cube(c.x, c.y + 1, c.z));
      cubeQueue.add(new Cube(c.x, c.y - 1, c.z));
      cubeQueue.add(new Cube(c.x, c.y, c.z + 1));
      cubeQueue.add(new Cube(c.x, c.y, c.z - 1));
    }
  }

  private static int calculateTotalSides(Integer[][][] grid) {
    int totalSides = 0;

    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        for (int z = 0; z < grid[x][y].length; z++) {
          if (grid[x][y][z] != null) {
            continue;
          }

          if (grid[x + 1][y][z] != null) {
            totalSides++;
          }
          if (grid[x - 1][y][z] != null) {
            totalSides++;
          }
          if (grid[x][y + 1][z] != null) {
            totalSides++;
          }
          if (grid[x][y - 1][z] != null) {
            totalSides++;
          }
          if (grid[x][y][z + 1] != null) {
            totalSides++;
          }
          if (grid[x][y][z - 1] != null) {
            totalSides++;
          }
        }
      }
    }
    return totalSides;
  }

  private static Integer[][][] fillGrid(List<Cube> cubes) {
    final Integer[][][] grid = new Integer[25][25][25];
    nullsToZeros(grid);
    cubes.forEach(cube -> grid[cube.x][cube.y][cube.z] = null);
    return grid;
  }

  private static List<Cube> initializeCubes(Resource input) {
    return lines(input)
        .map(line -> line.split(","))
        .map(
            splitLine -> {
              final int x = parseInt(splitLine[0]) + 1;
              final int y = parseInt(splitLine[1]) + 1;
              final int z = parseInt(splitLine[2]) + 1;
              return new Cube(x, y, z);
            })
        .toList();
  }

  static int solvePart2(Resource input) {
    return getTotalSides(input, true);
  }
}
