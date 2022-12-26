package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Arrays.stream;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day19 {
  private static long mostGeodesCallCounter = 0;
  private static int maxTime = -1;
  private static Blueprint activeBlueprint = null;

  static int solvePart1(Resource input) {
    mostGeodesCallCounter = 0;
    maxTime = 24;

    final int totalQualityLevel =
        parseBlueprints(input, 50)
            .map(
                blueprint -> {
                  log.debug("Blueprint {}: Calculating quality level", blueprint.id);
                  activeBlueprint = blueprint;
                  final int mostGeodes = mostGeodes(0, 0, 0, 0, 1, 0, 0, 0, 0);
                  final int qualityLevel = blueprint.id * mostGeodes;
                  log.debug(
                      "Blueprint {}: {} geodes found. Quality level: {}",
                      blueprint.id,
                      mostGeodes,
                      qualityLevel);
                  return qualityLevel;
                })
            .reduce(0, Integer::sum);

    log.debug(
        "Finished checking all blueprints and called method mostGeodes {} times",
        format("%,d", mostGeodesCallCounter));
    log.info("Total quality level: {}", totalQualityLevel);

    return totalQualityLevel;
  }

  static int solvePart2(Resource input) {
    mostGeodesCallCounter = 0;
    maxTime = 32;

    final int mostGeodesProduct =
        parseBlueprints(input, 3)
            .map(
                blueprint -> {
                  log.debug("Blueprint {}: Finding most possible geodes", blueprint.id);
                  activeBlueprint = blueprint;
                  final int mostGeodes = mostGeodes(0, 0, 0, 0, 1, 0, 0, 0, 0);
                  log.debug("Blueprint {}: {} geodes found", blueprint.id, mostGeodes);
                  return mostGeodes;
                })
            .reduce(1, (a, b) -> a * b);

    log.debug(
        "Finished checking at most 3 blueprints and called method mostGeodes {} times",
        format("%,d", mostGeodesCallCounter));
    log.info("Product of most geodes of checked blueprints: {}", mostGeodesProduct);

    return mostGeodesProduct;
  }

  private static Stream<Blueprint> parseBlueprints(Resource input, int maxBlueprints) {
    return lines(input, "Blueprint")
        .map(blueprint -> blueprint.replaceAll("[^0-9]+", "#"))
        .map(blueprint -> blueprint.split("#"))
        .map(sbp -> stream(sbp).filter(part -> !part.isEmpty()).toList())
        .filter(parts -> parts.size() > 6)
        .limit(maxBlueprints)
        .map(
            parts ->
                new Blueprint(
                    parseInt(parts.get(0)),
                    parseInt(parts.get(1)),
                    parseInt(parts.get(2)),
                    parseInt(parts.get(3)),
                    parseInt(parts.get(4)),
                    parseInt(parts.get(5)),
                    parseInt(parts.get(6)),
                    new AtomicInteger()));
  }

  // ore, clay, obsidian, geodes, ore robots, clay robots, obsidian robots, geode robots, current
  // time
  public static int mostGeodes(
      int ore,
      int clay,
      int obsidian,
      int geodes,
      int oreRobots,
      int clayRobots,
      int obsidianRobots,
      int geodeRobots,
      int time) {
    mostGeodesCallCounter++;
    if (time == maxTime) {
      activeBlueprint.maxGeodes.set(max(activeBlueprint.maxGeodes.get(), geodes));
      return geodes;
    }

    // end calls that can't realistically beat the best geode score
    // using the unrealistic heuristic of producing 1 geode robot for each turn remaining
    // this gets rid of enough cases to bring runtime from days into seconds
    int minutesLeft = maxTime - time;
    int maxGeodesPossible = geodes;
    for (int i = 0; i < minutesLeft; i++) {
      maxGeodesPossible += geodeRobots + i;
    }
    if (maxGeodesPossible < activeBlueprint.maxGeodes.get()) {
      return 0;
    }

    // calculate new materials values for after this cycle
    int newOre = ore + oreRobots;
    int newClay = clay + clayRobots;
    int newObsidian = obsidian + obsidianRobots;
    int newGeodes = geodes + geodeRobots;

    // overall, we can make a few assumptions to prune and reduce the solution space:
    // 1. if we have more than the maximum ore cost of a robot, always make a robot
    // 2. we only ever need as many robots to produce the maximum cost for that material in one turn
    // for example, if the geode robot costs 5 obsidian, we never need more than 5 obsidian robots,
    // because we can only craft one robot per turn
    // between these optimizations, plus usually prioritizing making the most advanced robot
    // available, we vastly trim the recursion tree and get an answer in just a few seconds

    // two guaranteed conditions that we know will always be the best:
    // we always want to make a geode robot whenever possible
    // and if we have enough clay robots, we always want to make an obsidian robot if possible
    // we almost always want to make an obsidian robot whenever possible, but there's a few edge
    // cases where one more clay robot is better
    if (ore >= activeBlueprint.geodeCost1 && obsidian >= activeBlueprint.geodeCost2) {
      return mostGeodes(
          newOre - activeBlueprint.geodeCost1,
          newClay,
          newObsidian - activeBlueprint.geodeCost2,
          newGeodes,
          oreRobots,
          clayRobots,
          obsidianRobots,
          geodeRobots + 1,
          time + 1);
    }
    if (clayRobots >= activeBlueprint.obsidianCost2
        && obsidianRobots < activeBlueprint.geodeCost2
        && ore >= activeBlueprint.obsidianCost1
        && clay >= activeBlueprint.obsidianCost2) {
      return mostGeodes(
          newOre - activeBlueprint.obsidianCost1,
          newClay - activeBlueprint.obsidianCost2,
          newObsidian,
          newGeodes,
          oreRobots,
          clayRobots,
          obsidianRobots + 1,
          geodeRobots,
          time + 1);
    }

    // for the non-guaranteed conditions, take the maximum of any
    int best = 0;
    // if not too many obsidian robots and enough to make one, make one
    if (obsidianRobots < activeBlueprint.geodeCost2
        && ore >= activeBlueprint.obsidianCost1
        && clay >= activeBlueprint.obsidianCost2) {
      best =
          max(
              best,
              mostGeodes(
                  newOre - activeBlueprint.obsidianCost1,
                  newClay - activeBlueprint.obsidianCost2,
                  newObsidian,
                  newGeodes,
                  oreRobots,
                  clayRobots,
                  obsidianRobots + 1,
                  geodeRobots,
                  time + 1));
    }
    // if not too many clay robots and enough to make one, make one
    if (clayRobots < activeBlueprint.obsidianCost2 && ore >= activeBlueprint.clayCost) {
      best =
          max(
              best,
              mostGeodes(
                  newOre - activeBlueprint.clayCost,
                  newClay,
                  newObsidian,
                  newGeodes,
                  oreRobots,
                  clayRobots + 1,
                  obsidianRobots,
                  geodeRobots,
                  time + 1));
    }
    // if not too many ore robots and enough to make one, make one
    if (oreRobots < 4 && ore >= activeBlueprint.oreCost) {
      best =
          max(
              best,
              mostGeodes(
                  newOre - activeBlueprint.oreCost,
                  newClay,
                  newObsidian,
                  newGeodes,
                  oreRobots + 1,
                  clayRobots,
                  obsidianRobots,
                  geodeRobots,
                  time + 1));
    }
    // if not holding on to more ore than maximum robot cost, wait and see if we can make a better
    // robot later
    if (ore <= 4) {
      best =
          max(
              best,
              mostGeodes(
                  newOre,
                  newClay,
                  newObsidian,
                  newGeodes,
                  oreRobots,
                  clayRobots,
                  obsidianRobots,
                  geodeRobots,
                  time + 1));
    }

    return best;
  }

  record Blueprint(
      int id,
      int oreCost,
      int clayCost,
      int obsidianCost1,
      int obsidianCost2,
      int geodeCost1,
      int geodeCost2,
      AtomicInteger maxGeodes) {}
}
