package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static org.springframework.util.CollectionUtils.containsAny;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day23 {
  static int solvePart1(Resource input) {
    final List<Elf> elfLocations = initElfLocations(input);

    for (int i = 0; i < 10; i++) {
      simulateRound(elfLocations, i % 4);
    }

    final int emptyGroundTiles = numberOfEmptyGroundTiles(elfLocations);
    log.info("Number of empty ground tiles: " + emptyGroundTiles);

    return emptyGroundTiles;
  }

  static int solvePart2(Resource input) {
    final List<Elf> elfLocations = initElfLocations(input);
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

  private static List<Elf> initElfLocations(Resource input) {
    final List<String> lines = linesList(input);
    final List<Elf> elfLocations = new ArrayList<>();

    for (int y = 0; y < lines.size(); y++) {
      final String line = lines.get(y);

      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          elfLocations.add(new Elf(x, y));
        }
      }
    }
    return elfLocations;
  }

  private static int numberOfEmptyGroundTiles(List<Elf> elfLocations) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Elf elf : elfLocations) {
      if (minX > elf.x) {
        minX = elf.x;
      }
      if (minY > elf.y) {
        minY = elf.y;
      }
      if (maxX < elf.x) {
        maxX = elf.x;
      }
      if (maxY < elf.y) {
        maxY = elf.y;
      }
    }
    return (maxX - minX + 1) * (maxY - minY + 1) - elfLocations.size();
  }

  private static int simulateRound(List<Elf> elfLocations, int directionOrder) {
    final List<ProposedElfLocation> proposedElfLocations = new ArrayList<>(elfLocations.size());
    for (Elf currentElf : elfLocations) {
      if (currentElf.isOtherElfInRange(elfLocations)) {
        final ProposedElfLocation nextLocation =
            currentElf.getNextProposedLocation(elfLocations, directionOrder);
        if (nextLocation != null) {
          proposedElfLocations.add(nextLocation);
        }
      }
    }
    proposedElfLocations.sort(
        (o1, o2) ->
            (o1.newLocation.x - o2.newLocation.x) * 10_000 + (o1.newLocation.y - o2.newLocation.y));

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

    for (ProposedElfLocation proposedElfLocation : proposedElfLocations) {
      elfLocations.remove(proposedElfLocation.oldLocation);
      elfLocations.add(proposedElfLocation.newLocation);
    }

    return proposedElfLocations.size();
  }

  @Data
  @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
  static class Elf {
    int x;
    int y;

    ProposedElfLocation getNextProposedLocation(List<Elf> elfLocations, int directionOrder) {
      List<Function<List<Elf>, ProposedElfLocation>> nextLocations =
          List.of(
              this::getNorthLocationOrNull,
              this::getSouthLocationOrNull,
              this::getWestLocationOrNull,
              this::getEastLocationOrNull);

      for (int i = 0; i < nextLocations.size(); i++) {
        final ProposedElfLocation nextLocation =
            nextLocations.get((4 + directionOrder + i) % 4).apply(elfLocations);
        if (nextLocation != null) {
          return nextLocation;
        }
      }

      return null;
    }

    ProposedElfLocation getNorthLocationOrNull(List<Elf> elfLocations) {
      if (elfLocations.contains(new Elf(x - 1, y - 1))
          || elfLocations.contains(new Elf(x, y - 1))
          || elfLocations.contains(new Elf(x + 1, y - 1))) {
        return null;
      }
      return new ProposedElfLocation(this, new Elf(x, y - 1));
    }

    ProposedElfLocation getSouthLocationOrNull(List<Elf> elfLocations) {
      if (elfLocations.contains(new Elf(x - 1, y + 1))
          || elfLocations.contains(new Elf(x, y + 1))
          || elfLocations.contains(new Elf(x + 1, y + 1))) {
        return null;
      }
      return new ProposedElfLocation(this, new Elf(x, y + 1));
    }

    ProposedElfLocation getWestLocationOrNull(List<Elf> elfLocations) {
      if (elfLocations.contains(new Elf(x - 1, y - 1))
          || elfLocations.contains(new Elf(x - 1, y))
          || elfLocations.contains(new Elf(x - 1, y + 1))) {
        return null;
      }
      return new ProposedElfLocation(this, new Elf(x - 1, y));
    }

    ProposedElfLocation getEastLocationOrNull(List<Elf> elfLocations) {
      if (elfLocations.contains(new Elf(x + 1, y - 1))
          || elfLocations.contains(new Elf(x + 1, y))
          || elfLocations.contains(new Elf(x + 1, y + 1))) {
        return null;
      }
      return new ProposedElfLocation(this, new Elf(x + 1, y));
    }

    // TODO refactor with own Point class which has these checks already built in
    boolean isOtherElfInRange(List<Elf> elfLocations) {
      return containsAny(
          elfLocations,
          List.of(
              new Elf(x - 1, y - 1),
              new Elf(x - 1, y),
              new Elf(x - 1, y + 1),
              new Elf(x, y - 1),
              new Elf(x, y + 1),
              new Elf(x + 1, y - 1),
              new Elf(x + 1, y),
              new Elf(x + 1, y + 1)));
    }
  }

  @AllArgsConstructor
  static class ProposedElfLocation {
    Elf oldLocation;
    Elf newLocation;

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof ProposedElfLocation otherElf)) {
        return false;
      }
      return this.newLocation.x == otherElf.newLocation.x
          && this.newLocation.y == otherElf.newLocation.y;
    }
  }
}
