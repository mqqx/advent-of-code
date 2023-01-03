package dev.mqqx.aoc.year21;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static dev.mqqx.aoc.util.SplitUtils.lines;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day12 {

  private static final String START_CAVE = "start";
  private static final String END_CAVE = "end";

  static int solvePart1(Resource input) {
    final Map<String, Set<String>> caveMap = initCaveMap(input);
    final List<List<String>> validPaths = newArrayList();

    walkCaves(caveMap, START_CAVE, newArrayList(), validPaths);

    return validPaths.size();
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
  }

  private static Map<String, Set<String>> initCaveMap(Resource input) {
    final Map<String, Set<String>> caveMap = new HashMap<>();

    lines(input)
        .forEach(
            line -> {
              final String[] splitCaves = line.split("-");
              addConnectedCaves(caveMap, splitCaves[0], splitCaves[1]);
              addConnectedCaves(caveMap, splitCaves[1], splitCaves[0]);
            });

    filterCavesWhichCanNeverBeReached(caveMap);
    return caveMap;
  }

  private static void walkCaves(
      Map<String, Set<String>> caveMap,
      String current,
      List<String> passedCaves,
      List<List<String>> validPaths) {
    // add the current cave to the path
    passedCaves.add(current);

    // if the current cave is the end cave, add the path to list of valid paths
    if (END_CAVE.equals(current)) {
      validPaths.add(passedCaves);
    } else {
      // recursive case: for each connected cave of the current one,
      // find all paths from the connected cave to the end cave
      for (String next : caveMap.get(current)) {
        // check if the next cave should only be visited once
        if (!next.toLowerCase().equals(next) || !passedCaves.contains(next)) {
          walkCaves(caveMap, next, passedCaves, validPaths);
        }
      }
    }

    // remove the current cave from the path before returning
    passedCaves.remove(current);
  }

  private static void filterCavesWhichCanNeverBeReached(Map<String, Set<String>> caveMap) {
    final HashSet<String> cavesToRemove = newHashSet();
    for (Entry<String, Set<String>> cave : caveMap.entrySet()) {
      // if a cave is lowercase and has only one lowercase cave adjacent it cannot be reached,
      // because the adjacent cave would need to be passed twice, which is not allowed
      if (cave.getKey().toLowerCase().equals(cave.getKey()) && cave.getValue().size() == 1) {
        final String value = cave.getValue().stream().findFirst().orElseThrow();
        if (value.toLowerCase().equals(value)) {
          cavesToRemove.add(cave.getKey());
        }
      }
    }
    cavesToRemove.forEach(caveMap::remove);
    cavesToRemove.forEach(toRemove -> caveMap.values().forEach(list -> list.remove(toRemove)));
  }

  private static void addConnectedCaves(
      Map<String, Set<String>> caveMap, String fromCave, String toCave) {
    if (END_CAVE.equals(fromCave) || START_CAVE.equals(toCave)) {
      return;
    }
    final Set<String> linkedCaves = caveMap.get(fromCave);
    if (linkedCaves == null) {
      caveMap.put(fromCave, newHashSet(toCave));
    } else {
      linkedCaves.add(toCave);
    }
  }
}
