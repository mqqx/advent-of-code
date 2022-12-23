package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day16 {

  private static final String START_VALVE_LOCATION = "AA";

  record LinkedValveMeta(String id, int minutesToGetTo) {}

  record Valve(String id, int flowRate, Set<LinkedValveMeta> linkedValvesMeta) {}

  static int solvePart1(Resource input) {
    final Map<String, Valve> valveMap = initValveMap(input);

    return getMaxReleasablePressure(valveMap);
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return -1;
  }

  private static Map<String, Valve> initValveMap(Resource input) {
    final Map<String, Valve> valveMap = new HashMap<>();
    populateValves(input, valveMap);
    replaceZeroFlowRateValves(valveMap);
    return valveMap;
  }

  private static int getMaxReleasablePressure(Map<String, Valve> valveMap) {
    return getMaxPressureForRemainingTime(
        START_VALVE_LOCATION, valveMap, new HashSet<>(), new HashMap<>(), 30);
  }

  // Uses DFS and caching to compute the max releasable pressure for the remaining time
  private static int getMaxPressureForRemainingTime(
      String currentValveId,
      Map<String, Valve> valveMap,
      Set<String> valvesAlreadyOpen,
      Map<String, Integer> cache,
      int minutesRemaining) {
    if (minutesRemaining < 1) {
      return 0;
    }

    // Cache check
    final String cacheName = getCacheName(currentValveId, valvesAlreadyOpen, minutesRemaining);
    if (cache.get(cacheName) != null) {
      return cache.get(cacheName);
    }

    // DFS
    final Valve currentValve = valveMap.get(currentValveId);
    int maxFlow = 0;
    for (LinkedValveMeta linkedValve : currentValve.linkedValvesMeta) {
      int withoutOpeningCurrentValve =
          getMaxPressureForRemainingTime(
              linkedValve.id,
              valveMap,
              valvesAlreadyOpen,
              cache,
              minutesRemaining - linkedValve.minutesToGetTo);
      maxFlow = max(maxFlow, withoutOpeningCurrentValve);

      final boolean shouldCurrentValveBeOpened =
          currentValve.flowRate > 0 && !valvesAlreadyOpen.contains(currentValveId);
      if (shouldCurrentValveBeOpened) {
        Set<String> newValvesAlreadyOpen = new HashSet<>(valvesAlreadyOpen);
        newValvesAlreadyOpen.add(currentValveId);

        int withOpeningCurrentValve =
            currentValve.flowRate * (minutesRemaining - 1)
                + getMaxPressureForRemainingTime(
                    linkedValve.id,
                    valveMap,
                    newValvesAlreadyOpen,
                    cache,
                    minutesRemaining - 1 - linkedValve.minutesToGetTo);

        maxFlow = max(maxFlow, withOpeningCurrentValve);
      }
    }
    // Cache update
    cache.put(cacheName, maxFlow);
    return maxFlow;
  }

  private static void replaceZeroFlowRateValves(Map<String, Valve> valveMap) {
    final List<String> valveIdsToRemove = getValveIdsToRemove(valveMap);
    for (String valveIdToRemove : valveIdsToRemove) {
      final Valve valveToRemove = valveMap.get(valveIdToRemove);

      for (LinkedValveMeta linkedValveMeta : valveToRemove.linkedValvesMeta) {
        final Valve linkedValve = valveMap.get(linkedValveMeta.id);
        final Set<LinkedValveMeta> newLinkedValues =
            getUpdatedLinkedValvesMeta(valveToRemove, linkedValve);

        linkedValve.linkedValvesMeta.clear();
        linkedValve.linkedValvesMeta.addAll(newLinkedValues);
      }

      valveMap.remove(valveToRemove.id);
    }
  }

  private static Set<LinkedValveMeta> getUpdatedLinkedValvesMeta(
      Valve valveToRemove, Valve linkedValve) {
    // take all existing valves except the one to be removed
    final Set<LinkedValveMeta> newLinkedValues =
        linkedValve.linkedValvesMeta().stream()
            .filter(vt -> !vt.id.equals(valveToRemove.id))
            .collect(Collectors.toSet());

    final Integer minutesToGetTo = getMinutesToGetToValve(valveToRemove, linkedValve);

    // add all valves from the one to be removed with minutes to get to + the current minutes it
    // has in our existing list
    newLinkedValues.addAll(
        valveToRemove.linkedValvesMeta.stream()
            .filter(vt -> !vt.id.equals(linkedValve.id))
            .map(vt -> new LinkedValveMeta(vt.id, vt.minutesToGetTo + minutesToGetTo))
            .filter(
                vt ->
                    !newLinkedValues.stream()
                        .map(LinkedValveMeta::id)
                        .collect(Collectors.toSet())
                        .contains(vt.id))
            .collect(Collectors.toSet()));
    return newLinkedValues;
  }

  private static Integer getMinutesToGetToValve(Valve valveToRemove, Valve linkedValve) {
    return linkedValve.linkedValvesMeta.stream()
        .filter(vt -> vt.id.equals(valveToRemove.id))
        .map(LinkedValveMeta::minutesToGetTo)
        .findFirst()
        .orElseThrow();
  }

  private static void populateValves(Resource input, Map<String, Valve> valveMap) {
    lines(input)
        .map(valveString -> valveString.split("; "))
        .forEach(
            splitLine -> {
              final String[] valve = splitLine[0].split(" has flow rate=");
              final String valveId = valve[0].substring(valve[0].length() - 2);
              final int flowRate = parseInt(valve[1]);
              final String[] tunnels =
                  splitLine[1].split("tunnel[s]? lead[s]? to valve[s]? ")[1].split(", ");

              valveMap.put(
                  valveId,
                  new Valve(
                      valveId,
                      flowRate,
                      stream(tunnels)
                          .map(linkedValveId -> new LinkedValveMeta(linkedValveId, 1))
                          .collect(Collectors.toSet())));
            });
  }

  private static List<String> getValveIdsToRemove(Map<String, Valve> valveMap) {
    return valveMap.values().stream()
        .filter(valve -> valve.flowRate == 0 && !START_VALVE_LOCATION.equals(valve.id))
        .map(Valve::id)
        .toList();
  }

  private static String getCacheName(
      String currentValveId, Set<String> alreadyOpen, Integer minutesRemaining) {
    final StringBuilder builder = new StringBuilder().append(currentValveId);
    builder.append("/");
    for (String open : alreadyOpen) {
      builder.append(open);
    }
    builder.append("/");
    builder.append(minutesRemaining);

    return builder.toString();
  }
}
