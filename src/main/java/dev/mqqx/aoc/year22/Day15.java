package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import java.awt.Point;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day15 {

  record SensorBeacon(Point sensor, Point beacon, int manDist) {}

  static int solvePart1(Resource input, int yToCheck) {
    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input);

    int counter = 0;
    for (int i = -1_000_000; i < 5_000_000; i++) {
      for (SensorBeacon sensorBeacon : sensorBeacons) {
        final int manDistToX =
            abs(i - sensorBeacon.sensor.x) + abs(yToCheck - sensorBeacon.sensor.y);

        if (sensorBeacon.beacon.equals(new Point(i, yToCheck))) {
          System.out.println("break me out");
          break;
        } else if (manDistToX <= sensorBeacon.manDist) {
          counter++;
          break;
        }
      }
    }

    return counter;
  }

  private static List<SensorBeacon> getSensorBeacons(Resource input) {
    return lines(input)
        .map(line -> line.split(": "))
        .map(
            splitLine -> {
              final String[] sensor = splitLine[0].split(",");
              final int sensorX = parseInt(sensor[0].substring(sensor[0].indexOf("=") + 1));
              final int sensorY = parseInt(sensor[1].substring(sensor[1].indexOf("=") + 1));
              final String[] beacon = splitLine[1].split(",");
              final int beaconX = parseInt(beacon[0].substring(beacon[0].indexOf("=") + 1));
              final int beaconY = parseInt(beacon[1].substring(beacon[1].indexOf("=") + 1));

              return new SensorBeacon(
                  new Point(sensorX, sensorY),
                  new Point(beaconX, beaconY),
                  abs(sensorX - beaconX) + abs(sensorY - beaconY));
            })
        .toList();
  }

  static long solvePart2(Resource input) {
    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input);
    return -1L;
  }
}
