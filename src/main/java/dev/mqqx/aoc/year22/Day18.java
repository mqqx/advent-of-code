package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;

import java.util.List;
import java.util.stream.Stream;
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
    int xMin;
    int xMax;
    int yMin;
    int yMax;
    int zMin;
    int zMax;

    int getWidth() {
      return xMax - xMin + 1;
    }

    int getHeight() {
      return yMax - yMin + 1;
    }

    int getDepth() {
      return zMax - zMin + 1;
    }

    int[][][] getGrid() {
      return new int[getWidth()][getHeight()][getDepth()];
    }
  }

  static int solvePart1(Resource input) {
    final GridSize gridSize = new GridSize(2, 2, 2, 2, 2, 2);

    final List<Cube> cubes =
        lines(input)
            .map(line -> line.split(","))
            .map(
                splitLine -> {
                  final int x = parseInt(splitLine[0]);
                  final int y = parseInt(splitLine[1]);
                  final int z = parseInt(splitLine[2]);

                  if (gridSize.xMin > x) {
                    gridSize.setXMin(x);
                  }
                  if (gridSize.xMax < x) {
                    gridSize.setXMax(x);
                  }
                  if (gridSize.yMin > y) {
                    gridSize.setYMin(y);
                  }
                  if (gridSize.yMax < y) {
                    gridSize.setYMax(y);
                  }
                  if (gridSize.zMin > z) {
                    gridSize.setZMin(z);
                  }
                  if (gridSize.zMax < z) {
                    gridSize.setZMax(z);
                  }
                  return new Cube(x, y, z);
                })
            .toList();

    final int[][][] grid = gridSize.getGrid();
    cubes.forEach(
        cube ->
            grid[cube.x - gridSize.getXMin()][cube.y - gridSize.getYMin()][
                    cube.z - gridSize.getZMin()] =
                1);

    int totalSides = 0;
    for (int curX = 0; curX < grid.length; curX++) {
      for (int curY = 0; curY < grid[curX].length; curY++) {
        for (int curZ = 0; curZ < grid[curX][curY].length; curZ++) {
          if (grid[curX][curY][curZ] != 1) {
            continue;
          }

          if (curX + 1 == gridSize.getWidth() || grid[curX + 1][curY][curZ] != 1) {
            totalSides++;
          }
          if (curX - 1 < 0 || grid[curX - 1][curY][curZ] != 1) {
            totalSides++;
          }
          if (curY + 1 == gridSize.getHeight() || grid[curX][curY + 1][curZ] != 1) {
            totalSides++;
          }
          if (curY - 1 < 0 || grid[curX][curY - 1][curZ] != 1) {
            totalSides++;
          }
          if (curZ + 1 == gridSize.getDepth() || grid[curX][curY][curZ + 1] != 1) {
            totalSides++;
          }
          if (curZ - 1 < 0 || grid[curX][curY][curZ - 1] != 1) {
            totalSides++;
          }
        }
      }
    }

    return totalSides;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
  }
}
