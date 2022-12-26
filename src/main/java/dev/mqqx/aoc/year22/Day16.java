package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day16 {
  private static final String START_VALVE_LOCATION = "AA";

  record Valve(String id, int flowRate, Map<String, Integer> linkedValves) {}

  record CurrentLocation(String valveId, int remainingMinutes) {}

  static int solvePart1(Resource input) {
    final Map<String, Valve> valveMap = initValveMap(input);

    return maxReleasablePressureOf(valveMap);
  }

  // FIXME: correct solution, but very slow with runtime > 1 min
  static int solvePart2(Resource input) {
    final Map<String, Valve> valveMap = initValveMap(input);

    final PathsAndMaxFlow maxReleasablePressure = maxReleasablePressureOfPair(valveMap);
    log.info("Max pressure released: {}", maxReleasablePressure.maxFlow);
    log.info("PersonPath: {}{}", START_VALVE_LOCATION, maxReleasablePressure.personPath);
    log.info("ElephantPath: {}{}", START_VALVE_LOCATION, maxReleasablePressure.elephantPath);

    return maxReleasablePressure.maxFlow;
  }

  private static Map<String, Valve> initValveMap(Resource input) {
    final Map<String, Valve> valveMap = new HashMap<>();
    populateValves(input, valveMap);
    replaceZeroFlowRateValves(valveMap);
    return valveMap;
  }

  private static int maxReleasablePressureOf(Map<String, Valve> valveMap) {
    return maxReleasablePressureForRemainingTimeOf(
        new CurrentLocation(START_VALVE_LOCATION, 30), valveMap, new HashSet<>(), new HashMap<>());
  }

  // Uses DFS and caching to compute the max releasable pressure for the remaining time
  private static int maxReleasablePressureForRemainingTimeOf(
      CurrentLocation currentLocation,
      Map<String, Valve> valveMap,
      Set<String> valvesAlreadyOpen,
      Map<String, Integer> cache) {
    if (currentLocation.remainingMinutes < 1) {
      return 0;
    }

    // Cache check
    final String cacheName = getCacheName(currentLocation, valvesAlreadyOpen);
    if (cache.get(cacheName) != null) {
      return cache.get(cacheName);
    }

    // DFS
    final Valve currentValve = valveMap.get(currentLocation.valveId);
    int maxFlow = 0;
    for (Entry<String, Integer> linkedValve : currentValve.linkedValves.entrySet()) {
      final int withoutOpeningCurrentValve =
          maxReleasablePressureForRemainingTimeOf(
              new CurrentLocation(
                  linkedValve.getKey(), currentLocation.remainingMinutes - linkedValve.getValue()),
              valveMap,
              valvesAlreadyOpen,
              cache);
      maxFlow = max(maxFlow, withoutOpeningCurrentValve);

      final boolean shouldCurrentValveBeOpened =
          currentValve.flowRate > 0 && !valvesAlreadyOpen.contains(currentLocation.valveId);
      if (shouldCurrentValveBeOpened) {
        final Set<String> newValvesAlreadyOpen = new HashSet<>(valvesAlreadyOpen);
        newValvesAlreadyOpen.add(currentLocation.valveId);

        final int withOpeningCurrentValve =
            currentValve.flowRate * (currentLocation.remainingMinutes - 1)
                + maxReleasablePressureForRemainingTimeOf(
                    new CurrentLocation(
                        linkedValve.getKey(),
                        currentLocation.remainingMinutes - linkedValve.getValue() - 1),
                    valveMap,
                    newValvesAlreadyOpen,
                    cache);

        maxFlow = max(maxFlow, withOpeningCurrentValve);
      }
    }

    cache.put(cacheName, maxFlow);
    return maxFlow;
  }

  private static PathsAndMaxFlow maxReleasablePressureOfPair(Map<String, Valve> valveMap) {
    final List<String> valvesToLink = valveMap.values().stream().map(Valve::id).toList();

    addOrReplaceLinkedValves(valveMap, valvesToLink, false);

    final CurrentLocation startLocation = new CurrentLocation(START_VALVE_LOCATION, 26);
    final AtomicInteger counter = new AtomicInteger(0);

    final PathsAndMaxFlow pathsAndMaxFlow =
        maxReleasablePressureForRemainingTimeOfPair(
            startLocation, startLocation, valveMap, new TreeSet<>(), new HashMap<>(), counter);

    log.warn("Tested {} paths", format("%,d", counter.get()));
    return pathsAndMaxFlow;
  }

  // Uses DFS and caching to compute the max releasable pressure for the remaining time
  private static PathsAndMaxFlow maxReleasablePressureForRemainingTimeOfPair(
      CurrentLocation person,
      CurrentLocation elephant,
      Map<String, Valve> valveMap,
      Set<String> valvesAlreadyOpen,
      Map<String, PathsAndMaxFlow> cache,
      AtomicInteger counter) {
    counter.getAndIncrement();
    PathsAndMaxFlow pathsAndMaxFlow = new PathsAndMaxFlow("", "", 0);
    final List<Valve> remainingValves =
        valveMap.values().stream()
            .filter(valve -> !START_VALVE_LOCATION.equals(valve.id))
            .filter(valve -> !valvesAlreadyOpen.contains(valve.id))
            .toList();

    if (remainingValves.isEmpty()
        || (person.remainingMinutes < 1 && elephant.remainingMinutes < 1)) {
      return pathsAndMaxFlow;
    }

    final String cacheName = getCacheNameForPair(person, elephant, valvesAlreadyOpen);
    if (cache.get(cacheName) != null) {
      return cache.get(cacheName);
    }
    // DFS on important points
    // Iterate through all the valves for me
    for (int i = remainingValves.size() - 1; i >= 0; i--) {
      final Valve nextValve = remainingValves.get(i);
      final int timeRemainingAtNextValve =
          person.remainingMinutes - nextValve.linkedValves.get(person.valveId) - 1;
      if (timeRemainingAtNextValve > 0) {
        final Set<String> remainingOpenValves = new TreeSet<>(valvesAlreadyOpen);
        remainingOpenValves.add(nextValve.id);
        PathsAndMaxFlow thisFlow =
            maxReleasablePressureForRemainingTimeOfPair(
                new CurrentLocation(nextValve.id, timeRemainingAtNextValve),
                elephant,
                valveMap,
                remainingOpenValves,
                cache,
                counter);
        thisFlow = thisFlow.copy();
        thisFlow.maxFlow += timeRemainingAtNextValve * nextValve.flowRate;
        thisFlow.personPath = nextValve.id + thisFlow.personPath;
        if (thisFlow.maxFlow > pathsAndMaxFlow.maxFlow) {
          if (person.remainingMinutes == 26 && elephant.remainingMinutes == 26) {
            log.debug("Candidate maxFlow computed: " + thisFlow.maxFlow);
            log.debug("Candidate personPath: " + thisFlow.personPath);
            log.debug("Candidate elephantPath: " + thisFlow.elephantPath);
          }
          pathsAndMaxFlow = thisFlow;
        }
      }
    }

    // Iterate through all the valves for the elephant
    for (int i = 0; i < remainingValves.size(); i++) {
      final Valve nextValve = remainingValves.get(i);
      final int timeRemainingAtNextValve =
          elephant.remainingMinutes - nextValve.linkedValves.get(elephant.valveId) - 1;
      if (timeRemainingAtNextValve > 0) {
        final Set<String> newValvesAlreadyOpen = new TreeSet<>(valvesAlreadyOpen);
        newValvesAlreadyOpen.add(nextValve.id);
        PathsAndMaxFlow thisFlow =
            maxReleasablePressureForRemainingTimeOfPair(
                person,
                new CurrentLocation(nextValve.id, timeRemainingAtNextValve),
                valveMap,
                newValvesAlreadyOpen,
                cache,
                counter);
        thisFlow = thisFlow.copy();
        thisFlow.maxFlow += timeRemainingAtNextValve * nextValve.flowRate;
        thisFlow.elephantPath = nextValve.id + thisFlow.elephantPath;
        if (thisFlow.maxFlow > pathsAndMaxFlow.maxFlow) {
          if (person.remainingMinutes == 26 && elephant.remainingMinutes == 26) {
            log.debug("Elephant - Candidate maxFlow computed: " + thisFlow.maxFlow);
            log.debug("Elephant - Candidate personPath: " + thisFlow.personPath);
            log.debug("Elephant - Candidate elephantPath: " + thisFlow.elephantPath);
          }
          pathsAndMaxFlow = thisFlow;
        }
      }
    }

    cache.put(cacheName, pathsAndMaxFlow);
    return pathsAndMaxFlow;
  }

  private static void replaceZeroFlowRateValves(Map<String, Valve> valveMap) {
    addOrReplaceLinkedValves(valveMap, getValveIdsToRemove(valveMap), true);
  }

  private static void addOrReplaceLinkedValves(
      Map<String, Valve> valveMap, List<String> valveIdsToAddOrReplace, boolean shouldReplace) {
    for (String valveId : valveIdsToAddOrReplace) {
      final Valve valveToRemove = valveMap.get(valveId);

      for (String linkedValveId : valveToRemove.linkedValves.keySet()) {
        final Valve linkedValve = valveMap.get(linkedValveId);

        if (linkedValve.linkedValves.size() >= valveMap.size() - 1) {
          continue;
        }

        final Map<String, Integer> newLinkedValues = new HashMap<>();

        if (shouldReplace) {
          // take all existing valves except the one to be removed
          newLinkedValues.putAll(
              linkedValve.linkedValves.entrySet().stream()
                  .filter(vt -> !vt.getKey().equals(valveToRemove.id))
                  .collect(toMap(Entry::getKey, Entry::getValue)));
        }

        final Integer minutesToGetTo = getMinutesToGetToValve(valveToRemove, linkedValve);

        // add all valves from the one to be removed/merged with minutes to get to + the current
        // minutes
        // it has in our existing list
        final HashMap<String, Integer> test =
            valveToRemove.linkedValves.entrySet().stream()
                .filter(vt -> !vt.getKey().equals(linkedValve.id))
                .filter(vt -> !newLinkedValues.containsKey(vt.getKey()))
                .filter(vt -> shouldReplace || !linkedValve.linkedValves.containsKey(vt.getKey()))
                .map(vt -> Map.entry(vt.getKey(), vt.getValue() + minutesToGetTo))
                .collect(toMap(Entry::getKey, Entry::getValue, (prev, next) -> next, HashMap::new));

        newLinkedValues.putAll(test);

        if (shouldReplace) {
          linkedValve.linkedValves.clear();
        }
        linkedValve.linkedValves.putAll(newLinkedValues);
      }

      if (shouldReplace) {
        valveMap.remove(valveToRemove.id);
      }
    }
  }

  private static Integer getMinutesToGetToValve(Valve valveToRemove, Valve linkedValve) {
    return linkedValve.linkedValves.entrySet().stream()
        .filter(vt -> vt.getKey().equals(valveToRemove.id))
        .map(Entry::getValue)
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
                      valveId, flowRate, stream(tunnels).collect(toMap(String::toString, v -> 1))));
            });
  }

  private static List<String> getValveIdsToRemove(Map<String, Valve> valveMap) {
    return valveMap.values().stream()
        .filter(valve -> valve.flowRate == 0 && !START_VALVE_LOCATION.equals(valve.id))
        .map(Valve::id)
        .toList();
  }

  private static String getCacheName(CurrentLocation currentLocation, Set<String> alreadyOpen) {
    final StringBuilder builder = new StringBuilder().append(currentLocation.valveId);
    builder.append("/");
    for (String open : alreadyOpen) {
      builder.append(open);
    }
    builder.append("/");
    builder.append(currentLocation.remainingMinutes);

    return builder.toString();
  }

  @AllArgsConstructor
  private static class PathsAndMaxFlow {
    String personPath;
    String elephantPath;
    int maxFlow;

    PathsAndMaxFlow copy() {
      return new PathsAndMaxFlow(personPath, elephantPath, maxFlow);
    }
  }

  private static String getCacheNameForPair(
      CurrentLocation person, CurrentLocation elephant, Set<String> openedValves) {

    StringBuilder builder =
        new StringBuilder().append(person.valveId).append("/").append(elephant.valveId).append("/");
    for (String open : openedValves) {
      builder.append(open);
    }
    builder.append("/");
    builder.append(person.remainingMinutes);
    builder.append("/");
    builder.append(elephant.remainingMinutes);
    return builder.toString();
  }
}
