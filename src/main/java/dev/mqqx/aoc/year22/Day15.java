package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day15 {

  record SensorBeacon(Point sensor, Point beacon, int manDist) {}

  static int solvePart1(Resource input, int yToCheck) {
    final long millis = System.currentTimeMillis();
    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input);
    final Set<Integer> blockedX = new HashSet<>();

    for (SensorBeacon sensorBeacon : sensorBeacons) {

      final int yGap = abs(yToCheck - sensorBeacon.sensor.y);
      if (yGap <= sensorBeacon.manDist) {
        final int xMin = sensorBeacon.sensor.x - sensorBeacon.manDist + yGap;
        final int xMax = sensorBeacon.sensor.x + sensorBeacon.manDist - yGap;
        //        System.out.println("Range: " + xMin + ", " + xMax + " for: " + sensorBeacon);
        for (int xToCheck = xMin; xToCheck <= xMax; xToCheck++) {
          final int manDistToX = abs(xToCheck - sensorBeacon.sensor.x) + yGap;

          if (manDistToX <= sensorBeacon.manDist
              && !sensorBeacon.beacon.equals(new Point(xToCheck, yToCheck))) {
            blockedX.add(xToCheck);
          }
        }
      }
    }
    final long part1finished = System.currentTimeMillis() - millis;
    System.out.println("part 1 finished: " + part1finished);

    return blockedX.size();
  }

  private static List<SensorBeacon> getSensorBeacons(Resource input) {
    return lines(input)
        .map(line -> line.split(": "))
        .map(
            splitLine -> {
              final String[] sensor = splitLine[0].split(",");
              final int sensorX = parseCoordinate(sensor[0]);
              final int sensorY = parseCoordinate(sensor[1]);
              final String[] beacon = splitLine[1].split(",");
              final int beaconX = parseCoordinate(beacon[0]);
              final int beaconY = parseCoordinate(beacon[1]);

              return new SensorBeacon(
                  new Point(sensorX, sensorY),
                  new Point(beaconX, beaconY),
                  abs(sensorX - beaconX) + abs(sensorY - beaconY));
            })
        .toList();
  }

  private static int parseCoordinate(String coordinateToParse) {
    return parseInt(coordinateToParse.substring(coordinateToParse.indexOf("=") + 1));
  }

  static long solvePart2(Resource input) {
    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input);
    return -1L;
  }
}
