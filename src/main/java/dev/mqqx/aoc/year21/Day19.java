package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.Point3D.ORIGIN;
import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import dev.mqqx.aoc.util.Point3D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day19 {

  private static final int FACE_COUNT = 6;
  private static final int ROTATION_COUNT = 4;
  private static final int BEACONS_NEEDED_TO_MATCH = 12;

  static int solvePart1(Resource input) {
    final List<Set<Point3D>> scannerResults = parseScannerResults(input);

    final Set<Point3D> beacons = scannerResults.remove(0);
    final Set<Point3D> scanners = new HashSet<>();

    // first scanner starts at origin
    scanners.add(ORIGIN);

    while (!scannerResults.isEmpty()) {
      final Set<Point3D> potentialBeacons = scannerResults.remove(0);
      final List<Point3D> transformed = transformToMatch(beacons, potentialBeacons);
      if (transformed.isEmpty()) {
        // append to end, hit later again
        scannerResults.add(potentialBeacons);
      } else {
        scanners.add(transformed.remove(0));
        beacons.addAll(transformed);
      }
    }

    log.debug("Found the following scanners: {}", scanners);
    log.info("Matching beacons: {}", beacons.size());
    return beacons.size();
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
  }

  // list needs to be modifiable, as scanner results will be removed and added
  @SuppressWarnings("squid:S6204")
  private static List<Set<Point3D>> parseScannerResults(Resource input) {
    return lines(input, "\n\n")
        .map(
            scanner ->
                scanner
                    .lines()
                    .filter(line -> '-' != line.charAt(1))
                    .map(line -> line.split(","))
                    .map(Point3D::new)
                    .collect(Collectors.toSet()))
        .collect(toList());
  }

  public static List<Point3D> transformToMatch(
      Set<Point3D> beacons, Set<Point3D> potentialBeacons) {
    for (int face = 0; face < FACE_COUNT; face++) {
      for (int rotate = 0; rotate < ROTATION_COUNT; rotate++) {
        final Set<Point3D> reorientatedBeacons = reorientateBeacons(potentialBeacons, face, rotate);
        for (Point3D beacon : beacons) {
          for (Point3D potentialBeacon : reorientatedBeacons) {
            final Point3D potentialSensor = beacon.diff(potentialBeacon);
            final Set<Point3D> repositionedBeacons =
                new HashSet<>(
                    reorientatedBeacons.stream().map(x -> x.sum(potentialSensor)).toList());
            final Set<Point3D> commonBeacons = new HashSet<>(repositionedBeacons);
            commonBeacons.retainAll(beacons);
            if (commonBeacons.size() >= BEACONS_NEEDED_TO_MATCH) {
              return matchingBeacons(potentialSensor, repositionedBeacons);
            }
          }
        }
      }
    }
    return emptyList();
  }

  private static List<Point3D> matchingBeacons(
      Point3D potentialSensor, Set<Point3D> repositionedBeacons) {
    final List<Point3D> matchingBeacons = new ArrayList<>();
    matchingBeacons.add(potentialSensor);
    matchingBeacons.addAll(repositionedBeacons);
    return matchingBeacons;
  }

  private static Set<Point3D> reorientateBeacons(
      Set<Point3D> potentialBeacons, int face, int rotate) {
    final Set<Point3D> reoriented = new HashSet<>();
    for (Point3D potentialBeacon : potentialBeacons) {
      reoriented.add(rotate(face(potentialBeacon, face), rotate));
    }
    return reoriented;
  }

  public static Point3D rotate(Point3D c, int i) {
    return switch (i) {
      case 0 -> c;
      case 1 -> new Point3D(-c.y(), c.x(), c.z());
      case 2 -> new Point3D(-c.x(), -c.y(), c.z());
      case 3 -> new Point3D(c.y(), -c.x(), c.z());
      default -> null;
    };
  }

  public static Point3D face(Point3D c, int i) {
    return switch (i) {
      case 0 -> c;
      case 1 -> new Point3D(c.x(), -c.y(), -c.z());
      case 2 -> new Point3D(c.x(), -c.z(), c.y());
      case 3 -> new Point3D(-c.y(), -c.z(), c.x());
      case 4 -> new Point3D(c.y(), -c.z(), -c.x());
      case 5 -> new Point3D(-c.x(), -c.z(), -c.y());
      default -> null;
    };
  }
}
