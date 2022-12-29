package dev.mqqx.aoc.year22;

import static com.google.common.collect.ImmutableRangeSet.unionOf;
import static com.google.common.collect.Range.open;
import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;

import com.google.common.collect.Range;
import dev.mqqx.aoc.util.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day15 {

  record SensorBeacon(Point sensor, Point beacon, int manDist) {
    boolean inRangeOfSensor(int x, int y) {
      return manDist >= sensor.manDist(new Point(x, y));
    }

    Range<Integer> intersectionOn(int y) {
      if (sensor.y() < y) {
        if (sensor.y() + manDist < y) {
          return null;
        }
      } else {
        if (sensor.y() - manDist > y) {
          return null;
        }
      }
      int remainingDist = manDist - sensor.yGap(y);
      return open(sensor.x() - remainingDist - 1, sensor.x() + remainingDist + 1);
    }
  }

  static int solvePart1(Resource input, int yToCheck) {
    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input);
    return calculateBlockedPositions(yToCheck, sensorBeacons);
  }

  static long solvePart2(Resource input, int yRange) {
    log.debug("Start solving part 2 with given y-range from [0-{}]", yRange);
    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input);

    int y = getBeaconY(yRange, sensorBeacons);
    log.debug("Found y={}", y);

    for (int x = yRange; x >= 0; --x) {
      boolean cannotBeReached = true;
      for (SensorBeacon sensorBeacon : sensorBeacons) {
        if (sensorBeacon.inRangeOfSensor(x, y)) {
          cannotBeReached = false;
          break;
        }
      }
      if (cannotBeReached) {
        log.debug("Found x={}", x);
        final long tuningFrequency = 4_000_000L * x + y;
        log.debug("Tuning frequency={}", tuningFrequency);
        return tuningFrequency;
      }
    }

    return -1;
  }

  private static int getBeaconY(int yRange, List<SensorBeacon> sensorBeacons) {
    for (int y = yRange - 1; y >= 0; y--) {
      // if it's more than one range, it means we found the single position which is not covered
      if (rangesCoveringY(sensorBeacons, y) > 1) {
        return y;
      }
    }
    return -1;
  }

  private static int calculateBlockedPositions(int y, List<SensorBeacon> sensorBeacons) {
    final Set<Integer> blockedX = new HashSet<>();

    for (SensorBeacon sensorBeacon : sensorBeacons) {
      final int yGap = sensorBeacon.sensor.yGap(y);
      if (yGap <= sensorBeacon.manDist) {
        final int xMin = sensorBeacon.sensor.x() - sensorBeacon.manDist + yGap;
        final int xMax = sensorBeacon.sensor.x() + sensorBeacon.manDist - yGap;

        for (int x = xMin; x <= xMax; x++) {
          final int manDistToX = sensorBeacon.sensor.xGap(x) + yGap;

          if (manDistToX <= sensorBeacon.manDist && !sensorBeacon.beacon.equals(new Point(x, y))) {
            blockedX.add(x);
          }
        }
      }
    }

    return blockedX.size();
  }

  private static List<SensorBeacon> getSensorBeacons(Resource input) {
    return lines(input)
        .map(line -> line.split(": "))
        .map(
            splitLine -> {
              final Point sensorPoint = getSensorPoint(splitLine[0]);
              final Point beaconPoint = getSensorPoint(splitLine[1]);

              return new SensorBeacon(sensorPoint, beaconPoint, sensorPoint.manDist(beaconPoint));
            })
        .toList();
  }

  private static Point getSensorPoint(String sensorSplit) {
    final String[] sensor = sensorSplit.split(",");
    final int sensorX = parseCoordinate(sensor[0]);
    final int sensorY = parseCoordinate(sensor[1]);
    return new Point(sensorX, sensorY);
  }

  private static int parseCoordinate(String coordinateToParse) {
    return parseInt(coordinateToParse.substring(coordinateToParse.indexOf("=") + 1));
  }

  private static int rangesCoveringY(List<SensorBeacon> sensorBeaconPairs, int y) {
    final List<Range<Integer>> ranges = new ArrayList<>();

    for (SensorBeacon sensorBeaconPair : sensorBeaconPairs) {
      final Range<Integer> intersection = sensorBeaconPair.intersectionOn(y);

      if (intersection != null) {
        ranges.add(intersection);
      }
    }
    return unionOf(ranges).asRanges().size();
  }
}
