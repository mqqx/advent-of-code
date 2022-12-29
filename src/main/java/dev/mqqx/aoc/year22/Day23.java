package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;

import dev.mqqx.aoc.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day23 {
  static int solvePart1(Resource input) {
    final List<Point> elfLocations = initElfLocations(input);

    for (int i = 0; i < 10; i++) {
      simulateRound(elfLocations, i % 4);
    }

    final int emptyGroundTiles = numberOfEmptyGroundTiles(elfLocations);
    log.info("Number of empty ground tiles: " + emptyGroundTiles);

    return emptyGroundTiles;
  }

  static int solvePart2(Resource input) {
    final List<Point> elfLocations = initElfLocations(input);
    int round = 0;
    int numElvesMoved;
    do {
      if (round > 0 && round % 100 == 0) {
        log.debug("On round: " + round);
      }
      numElvesMoved = simulateRound(elfLocations, round % 4);
      round++;
    } while (numElvesMoved != 0);

    log.info("First round with no movement: " + round);
    return round;
  }

  private static List<Point> initElfLocations(Resource input) {
    final List<String> lines = linesList(input);
    final List<Point> elfLocations = new ArrayList<>();

    for (int y = 0; y < lines.size(); y++) {
      final String line = lines.get(y);

      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          elfLocations.add(new Point(x, y));
        }
      }
    }
    return elfLocations;
  }

  private static int numberOfEmptyGroundTiles(List<Point> elfLocations) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Point elf : elfLocations) {
      if (minX > elf.x()) {
        minX = elf.x();
      }
      if (minY > elf.y()) {
        minY = elf.y();
      }
      if (maxX < elf.x()) {
        maxX = elf.x();
      }
      if (maxY < elf.y()) {
        maxY = elf.y();
      }
    }
    return (maxX - minX + 1) * (maxY - minY + 1) - elfLocations.size();
  }

  private static int simulateRound(List<Point> elfLocations, int directionOrder) {
    final List<ProposedElf> proposedElfLocations = new ArrayList<>(elfLocations.size());
    for (Point currentElf : elfLocations) {
      if (currentElf.hasSurroundingIn(elfLocations)) {
        final ProposedElf nextLocation = ProposedElf.next(currentElf, elfLocations, directionOrder);
        if (nextLocation != null) {
          proposedElfLocations.add(nextLocation);
        }
      }
    }
    proposedElfLocations.sort(
        (o1, o2) ->
            (o1.newLocation.x() - o2.newLocation.x()) * 10_000
                + (o1.newLocation.y() - o2.newLocation.y()));

    for (int i = 0; i < proposedElfLocations.size() - 1; ++i) {
      if (proposedElfLocations.get(i).equals(proposedElfLocations.get(i + 1))) {
        while (i < proposedElfLocations.size() - 1
            && proposedElfLocations.get(i).equals(proposedElfLocations.get(i + 1))) {
          proposedElfLocations.remove(i + 1);
        }
        proposedElfLocations.remove(i);
        i--;
      }
    }

    for (ProposedElf proposedElfLocation : proposedElfLocations) {
      elfLocations.remove(proposedElfLocation.oldLocation);
      elfLocations.add(proposedElfLocation.newLocation);
    }

    return proposedElfLocations.size();
  }

  record ProposedElf(Point oldLocation, Point newLocation) {
    static ProposedElf next(Point current, List<Point> elfLocations, int directionOrder) {
      final List<Function<List<Point>, Point>> nextLocations =
          List.of(
              current::downIfNotSurrounded,
              current::upIfNotSurrounded,
              current::leftIfNotSurrounded,
              current::rightIfNotSurrounded);

      for (int i = 0; i < nextLocations.size(); i++) {
        final Point nextLocation =
            nextLocations.get((4 + directionOrder + i) % 4).apply(elfLocations);
        if (nextLocation != null) {
          return new ProposedElf(current, nextLocation);
        }
      }

      return null;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof ProposedElf otherElf)) {
        return false;
      }
      return newLocation.equals(otherElf.newLocation);
    }
  }
}
