package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.NumberUtils.parseIntegers;
import static dev.mqqx.aoc.util.SplitUtils.lines;

import dev.mqqx.aoc.util.Cube;
import dev.mqqx.aoc.util.Point3D;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day22 {
  static long solvePart1(Resource input) {
    return solve(input, true);
  }

  static long solvePart2(Resource input) {
    return solve(input, false);
  }

  private static long solve(Resource input, boolean isPart1) {
    final List<RebootStep> rebootSteps = new ArrayList<>();

    lines(input)
        .map(line -> line.split(" "))
        .map(Day22::parseRebootSteps)
        .forEach(rebootStep -> getModifiedRebootSteps(rebootSteps, rebootStep));

    if (isPart1) {
      final List<RebootStep> inRange = getCubesInRange(rebootSteps);
      return totalVolume(inRange).longValue();
    } else {
      return totalVolume(rebootSteps).longValue();
    }
  }

  private static void getModifiedRebootSteps(List<RebootStep> rebootSteps, RebootStep rebootStep) {
    final List<RebootStep> update = new ArrayList<>();
    if (rebootStep.isOn) {
      update.add(rebootStep.state(1));
    }

    for (RebootStep modified : rebootSteps) {
      final Optional<Cube> overlap = modified.cube().overlapping(rebootStep.cube);
      overlap.ifPresent(c -> update.add(new RebootStep(modified.state * -1, c, modified.isOn)));
    }

    rebootSteps.addAll(update);
  }

  private static RebootStep parseRebootSteps(String[] splitLine) {
    final List<Integer> coordinates = parseIntegers(splitLine[1]);
    final Point3D lower = new Point3D(coordinates.get(0), coordinates.get(2), coordinates.get(4));
    final Point3D upper =
        new Point3D(coordinates.get(1) + 1, coordinates.get(3) + 1, coordinates.get(5) + 1);
    return new RebootStep(0, new Cube(lower, upper), splitLine[0].charAt(1) == 'n');
  }

  private static List<RebootStep> getCubesInRange(List<RebootStep> rebootSteps) {
    final List<RebootStep> inRange = new ArrayList<>();
    final Cube cubeToConsider = new Cube(new Point3D(-50, -50, -50), new Point3D(51, 51, 51));
    for (RebootStep rebootStep : rebootSteps) {
      Optional<Cube> overlap = rebootStep.cube.overlapping(cubeToConsider);
      overlap.ifPresent(c -> inRange.add(new RebootStep(rebootStep.state, c, rebootStep.isOn)));
    }
    return inRange;
  }

  private static BigInteger totalVolume(List<RebootStep> inRange) {
    BigInteger cubes = BigInteger.ZERO;
    for (RebootStep rebootStep : inRange) {
      if (rebootStep.state == 1) {
        cubes = cubes.add(rebootStep.cube.volume());
      } else if (rebootStep.state == -1) {
        cubes = cubes.subtract(rebootStep.cube.volume());
      }
    }

    return cubes;
  }

  private record RebootStep(int state, Cube cube, boolean isOn) {
    private RebootStep state(int s) {
      return new RebootStep(s, cube, isOn);
    }
  }
}
