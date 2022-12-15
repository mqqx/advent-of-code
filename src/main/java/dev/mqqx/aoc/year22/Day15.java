package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Point;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day15 {

  record SensorBeacon(Point sensor, Point beacon, int manDist) {}

  record SensorZone(int leftEdge, int rightEdge, int topEdge, int bottomEdge) {}

  static int solvePart1(Resource input, int yToCheck) {
    final SensorZone[] sensorZone = {new SensorZone(0, 0, 0, 0)};

    final List<SensorBeacon> sensorBeacons = getSensorBeacons(input, sensorZone);

    int counter = 0;
    for (int i = -1_000_000; i < 5_000_000; i++) {
      for (SensorBeacon sensorBeacon : sensorBeacons) {
        final int xToCheck = i - sensorZone[0].leftEdge;
        final int manDistToX =
            abs(xToCheck - sensorBeacon.sensor.x) + abs(yToCheck - sensorBeacon.sensor.y);

        if (sensorBeacon.beacon.equals(new Point(i, yToCheck))) {
          break;
        } else if (manDistToX <= sensorBeacon.manDist) {
          counter++;
          break;
        }
      }
    }

    return counter;
  }

  private static List<SensorBeacon> getSensorBeacons(Resource input, SensorZone[] sensorZone) {
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

              if (min(sensorX, beaconX) < sensorZone[0].leftEdge) {
                sensorZone[0] =
                    new SensorZone(
                        min(sensorX, beaconX),
                        sensorZone[0].rightEdge,
                        sensorZone[0].topEdge,
                        sensorZone[0].bottomEdge);
              } else if (max(sensorX, beaconX) > sensorZone[0].rightEdge) {
                sensorZone[0] =
                    new SensorZone(
                        sensorZone[0].leftEdge,
                        max(sensorX, beaconX),
                        sensorZone[0].topEdge,
                        sensorZone[0].bottomEdge);
              }

              if (min(sensorY, beaconY) < sensorZone[0].bottomEdge) {
                sensorZone[0] =
                    new SensorZone(
                        sensorZone[0].leftEdge,
                        sensorZone[0].rightEdge,
                        sensorZone[0].topEdge,
                        min(sensorY, beaconY));
              } else if (max(sensorY, beaconY) > sensorZone[0].topEdge) {
                sensorZone[0] =
                    new SensorZone(
                        sensorZone[0].leftEdge,
                        sensorZone[0].rightEdge,
                        max(sensorY, beaconY),
                        sensorZone[0].bottomEdge);
              }

              final int manhattenDistance = abs(sensorX - beaconX) + abs(sensorY - beaconY);

              return new SensorBeacon(
                  new Point(sensorX, sensorY), new Point(beaconX, beaconY), manhattenDistance);
            })
        .toList();
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
  }
}
