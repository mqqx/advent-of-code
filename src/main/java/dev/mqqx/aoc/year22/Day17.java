package dev.mqqx.aoc.year22;

import static com.google.common.collect.Sets.newHashSet;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.util.stream.Collectors.toCollection;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day17 {
  private static final long ROCKS_TO_FALL_1 = 2_022L;
  private static final long ROCKS_TO_FALL_2 = 1_000_000_000_000L;
  private static final int CHAMBER_WIDTH = 7;

  static int solvePart1(Resource input) {
    return (int) simulateFallingRocks(input, ROCKS_TO_FALL_1);
  }

  static long solvePart2(Resource input) {
    return simulateFallingRocks(input, ROCKS_TO_FALL_2);
  }

  private static long simulateFallingRocks(Resource input, long rocksToFall) {
    final List<HashSet<Point>> rocks = initializeRocks();

    // evaluate jet pattern already before using it in loop
    final List<Boolean> jetPattern = initializeJetPattern(input);

    // initialize with bottom line
    final HashSet<Point> fallenRocks = initializeFallenRocksWithBottom();

    return simulateFallingRocksWithJetPattern(rocksToFall, rocks, jetPattern, fallenRocks);
  }

  private static long simulateFallingRocksWithJetPattern(long rocksToFall, List<HashSet<Point>> rocks, List<Boolean> jetPattern, HashSet<Point> fallenRocks) {
    //cache keeps track of seen states in the form of the top 30 rows of the state, mapped to the rock it occurred on and the max Y at the time
    Map<Set<Point>,Point> cache = new HashMap<>();
    long heightFromCycleRepeat = 0L;
    int jetCounter = 0;
    boolean cycleFound = false;
    long rockCount = 0;
    for (; rockCount < rocksToFall; rockCount++) {

      int maxY = fallenRocks.stream().map(x -> x.y).max(Integer::compare).orElse(-1) + 4;

      // offset next falling rock to starting position
      HashSet<Point> currentRock = rocks.get((int) (rockCount % rocks.size())).stream().map(x -> new Point(x.x + 2, x.y + maxY)).collect(toCollection(HashSet::new));

      while (true) {
        currentRock = moveRockIfNotTouchingWallAlready(jetPattern, fallenRocks, jetCounter, currentRock);
        jetCounter++;

        if (hasLandedOnSomething(fallenRocks, currentRock)) {
          if (rocksToFall == ROCKS_TO_FALL_2) {
            int curHeight = fallenRocks.stream().map(x->x.y).max(Integer::compare).orElse(-1);
            //create cache key for current rock state
            final Set<Point> cacheKey = convertToCacheKey(fallenRocks);
            if(!cycleFound && cache.containsKey(cacheKey)) {
              //get info about cycle
              Point info = cache.get(cacheKey);
              int oldTime = info.x;
              int oldHeight = info.y;
              long cycleLength = rockCount - oldTime;
              int cycleHeightChange = curHeight - oldHeight;
              //calculate number of times we could cycle without going over LENGTH
              long numCycles = (rocksToFall - rockCount) / cycleLength;
              //add total height that we would gain from repeating cycle, and add time that cycle repeat would take
              heightFromCycleRepeat = cycleHeightChange * numCycles;
              rockCount += numCycles * cycleLength;
              //mark cycle found to avoid further repeating
              cycleFound = true;
            } else {
              Point info = new Point((int) rockCount, curHeight);
              cache.put(cacheKey,info);
            }
          }

          break;
        }

        currentRock = currentRock.stream().map(x -> new Point(x.x, x.y - 1)).collect(toCollection(HashSet::new));
      }
    }
    return fallenRocks.stream().map(x -> x.y).max(Integer::compare).orElse(-1) + heightFromCycleRepeat + 1;
  }

  private static boolean hasLandedOnSomething(HashSet<Point> fallenRocks, HashSet<Point> currentRock) {
    for (Point currentPoint : currentRock) {
      if (fallenRocks.contains(new Point(currentPoint.x, currentPoint.y - 1))) {
        fallenRocks.addAll(currentRock);
        return true;
      }
    }
    return false;
  }

  private static HashSet<Point> moveRockIfNotTouchingWallAlready(List<Boolean> jetPattern, HashSet<Point> fallenRocks, int jetCounter, HashSet<Point> currentRock) {
    if (Boolean.TRUE.equals(jetPattern.get(jetCounter % jetPattern.size()))) {
      final int highestX = currentRock.stream().map(x -> x.x).max(Integer::compare).orElse(-1);
      final HashSet<Point> tentativeRight = currentRock.stream().map(x -> new Point(x.x + 1, x.y)).collect(toCollection(HashSet::new));

      if (highestX < CHAMBER_WIDTH - 1 && notContainsAny(fallenRocks, tentativeRight)) {
        return tentativeRight;
      }
    } else {
      final int lowestX = currentRock.stream().map(x -> x.x).min(Integer::compare).orElse(-1);
      final HashSet<Point> tentativeLeft = currentRock.stream().map(x -> new Point(x.x - 1, x.y)).collect(toCollection(HashSet::new));

      if (lowestX > 0 && notContainsAny(fallenRocks, tentativeLeft)) {
        return tentativeLeft;
      }
    }
    return currentRock;
  }

  private static HashSet<Point> initializeFallenRocksWithBottom() {
    return IntStream.range(0, CHAMBER_WIDTH)
        .mapToObj(i -> new Point(i, -1))
        .collect(toCollection(HashSet::new));
  }

  private static List<Boolean> initializeJetPattern(Resource input) {
    return read(input)
        .chars()
        .mapToObj(jet -> jet == '>')
        .toList();
  }

  private static boolean notContainsAny(Set<Point> big, Set<Point> small) {
    for (Point c : small) {
      if (big.contains(c)) {
        return false;
      }
    }
    return true;
  }

  private static List<HashSet<Point>> initializeRocks() {
    final List<HashSet<Point>> rocks = new ArrayList<>();

    // horizontal line
    rocks.add(
        IntStream.range(0, 4)
            .mapToObj(i -> new Point(i, 0))
            .collect(toCollection(HashSet::new)));

    // plus sign
    rocks.add(
        newHashSet(
            new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2), new Point(1, 0)));

    // backwards L
    rocks.add(
        newHashSet(
            new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2)));

    // vertical line
    rocks.add(
        IntStream.range(0, 4)
            .mapToObj(i -> new Point(0, i))
            .collect(toCollection(HashSet::new)));

    // square
    rocks.add(newHashSet(new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0)));
    return rocks;
  }

  public static Set<Point> convertToCacheKey(Set<Point> rocks) {
    int maxY = rocks.stream().map(x -> x.y).max(Integer::compare).orElse(-1);
    return rocks.stream().filter(x -> maxY - x.y <= 30).map(x -> new Point(x.x, maxY - x.y)).collect(toCollection(HashSet::new));
  }
}
