package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.toGrid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day11 {

  static int solvePart1(Resource input) {
    return solve(input, true);
  }

  static int solvePart2(Resource input) {
    return solve(input, false);
  }

  private static int solve(Resource input, boolean isPart1) {
    final int[][] octopusEnergyLevels = toGrid(input);
    int flashCounter = 0;
    int steps = isPart1 ? 100 : 1_000;

    for (int step = 0; step < steps; step++) {
      increaseAllByOne(octopusEnergyLevels);

      final int flashes = flash(octopusEnergyLevels);
      if (flashes == 100) {
        return step + 1;
      }
      flashCounter += flashes;
      reset(octopusEnergyLevels);
    }

    return flashCounter;
  }

  private static void reset(int[][] octopusEnergyLevels) {
    for (int y = 0; y < octopusEnergyLevels.length; y++) {
      for (int x = 0; x < octopusEnergyLevels[0].length; x++) {
        if (octopusEnergyLevels[y][x] > 9 || octopusEnergyLevels[y][x] < 0) {
          octopusEnergyLevels[y][x] = 0;
        }
      }
    }
  }

  private static int flash(int[][] octopusEnergyLevels) {
    int flashCounter = 0;
    boolean isFlashing;
    // in this case looping the whole array again after at least one octopus flashed is fine, as
    // there are only 100 octopus in the provided data
    do {
      isFlashing = false;
      for (int y = 0; y < octopusEnergyLevels.length; y++) {
        for (int x = 0; x < octopusEnergyLevels[0].length; x++) {
          if (octopusEnergyLevels[y][x] > 9) {
            flashCounter++;
            isFlashing = true;
            // set our own level to Integer.MIN_VALUE to prevent multiple flashes in one step
            octopusEnergyLevels[y][x] = Integer.MIN_VALUE;
            increaseSurroundingLevels(octopusEnergyLevels, y, x);
          }
        }
      }
    } while (isFlashing);
    return flashCounter;
  }

  private static void increaseSurroundingLevels(int[][] octopusEnergyLevels, int y, int x) {
    final boolean yLowerThanLength = y + 1 < octopusEnergyLevels.length;
    final boolean yHigherOrEqualsZero = y - 1 >= 0;
    final boolean xLowerThanLength = x + 1 < octopusEnergyLevels[0].length;
    final boolean xHigherOrEqualsZero = x - 1 >= 0;

    if (yLowerThanLength) {
      if (xLowerThanLength) {
        octopusEnergyLevels[y + 1][x + 1]++;
      }

      if (xHigherOrEqualsZero) {
        octopusEnergyLevels[y + 1][x - 1]++;
      }

      octopusEnergyLevels[y + 1][x]++;
    }

    if (xLowerThanLength) {
      octopusEnergyLevels[y][x + 1]++;
    }

    if (xHigherOrEqualsZero) {
      octopusEnergyLevels[y][x - 1]++;
    }

    if (yHigherOrEqualsZero) {
      if (xLowerThanLength) {
        octopusEnergyLevels[y - 1][x + 1]++;
      }

      if (xHigherOrEqualsZero) {
        octopusEnergyLevels[y - 1][x - 1]++;
      }

      octopusEnergyLevels[y - 1][x]++;
    }
  }

  private static void increaseAllByOne(int[][] octopusEnergyLevels) {
    for (int y = 0; y < octopusEnergyLevels.length; y++) {
      for (int x = 0; x < octopusEnergyLevels[0].length; x++) {
        octopusEnergyLevels[y][x]++;
      }
    }
  }
}
