package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day18 {

  record Cube(int x, int y, int z) {}

  @Data
  @AllArgsConstructor
  private static class GridSize {
    int minX;
    int maxX;
    int minY;
    int maxY;
    int minZ;
    int maxZ;

    int getWidth() {
      return maxX - minX + 1 + 1 + 1;
    }

    int getHeight() {
      return maxY - minY + 1 + 1 + 1;
    }

    int getDepth() {
      return maxZ - minZ + 1 + 1 + 1;
    }

    int[][][] getGrid() {
      return new int[getWidth()][getHeight()][getDepth()];
    }
  }

  static int solvePart1(Resource input) {
    return getTotalSides(input, false);
  }

  private static int getTotalSides(Resource input, boolean isPart2) {
    final GridSize gridSize = new GridSize(2, 2, 2, 2, 2, 2);
    final List<Cube> cubes = initializeCubes(input, gridSize);
    final int[][][] grid = fillGridWithCubes(gridSize, cubes);

    if (isPart2) {
      return -1;
    }

    return calculateTotalSides(gridSize, grid);
  }

  private static int calculateTotalSides(GridSize gridSize, int[][][] grid) {
    int totalSides = 0;

    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        for (int z = 0; z < grid[x][y].length; z++) {

          if (grid[x][y][z] != 2) {
            continue;
          }

          if (x + 1 == gridSize.getWidth() || grid[x + 1][y][z] != 2) {
            totalSides++;
          }
          if (x - 1 < 0 || grid[x - 1][y][z] != 2) {
            totalSides++;
          }
          if (y + 1 == gridSize.getHeight() || grid[x][y + 1][z] != 2) {
            totalSides++;
          }
          if (y - 1 < 0 || grid[x][y - 1][z] != 2) {
            totalSides++;
          }
          if (z + 1 == gridSize.getDepth() || grid[x][y][z + 1] != 2) {
            totalSides++;
          }
          if (z - 1 < 0 || grid[x][y][z - 1] != 2) {
            totalSides++;
          }
        }
      }
    }
    return totalSides;
  }

  private static int[][][] fillGridWithCubes(GridSize gridSize, List<Cube> cubes) {
    final int[][][] grid = gridSize.getGrid();
    cubes.forEach(
        cube ->
            grid[cube.x - gridSize.getMinX() + 1][cube.y - gridSize.getMinY() + 1][
                    cube.z - gridSize.getMinZ() + 1] =
                2);
    return grid;
  }

  private static List<Cube> initializeCubes(Resource input, GridSize gridSize) {
    return lines(input)
        .map(line -> line.split(","))
        .map(
            splitLine -> {
              final int x = parseInt(splitLine[0]);
              final int y = parseInt(splitLine[1]);
              final int z = parseInt(splitLine[2]);

              if (gridSize.minX > x) {
                gridSize.setMinX(x);
              }
              if (gridSize.maxX < x) {
                gridSize.setMaxX(x);
              }
              if (gridSize.minY > y) {
                gridSize.setMinY(y);
              }
              if (gridSize.maxY < y) {
                gridSize.setMaxY(y);
              }
              if (gridSize.minZ > z) {
                gridSize.setMinZ(z);
              }
              if (gridSize.maxZ < z) {
                gridSize.setMaxZ(z);
              }
              return new Cube(x, y, z);
            })
        .toList();
  }

  static int solvePart2(Resource input) {
    return getTotalSides(input, true);
  }
}
