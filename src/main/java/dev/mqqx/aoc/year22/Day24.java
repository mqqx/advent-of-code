package dev.mqqx.aoc.year22;

import static java.lang.String.valueOf;
import static java.util.Comparator.comparingInt;
import static java.util.stream.IntStream.range;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day24 {
  static int solvePart1(Resource input) {
    final BlizzardGrid blizzardGrid = initGrid(input);

    final int shortestTime =
        shortestRoute(blizzardGrid, 0, -1, blizzardGrid.width - 1, blizzardGrid.height - 1, 0) + 1;
    log.info("Shortest Route time: " + shortestTime);
    return shortestTime;
  }

  static int solvePart2(Resource input) {
    final BlizzardGrid blizzardGrid = initGrid(input);

    final int walkToGoal =
        shortestRoute(blizzardGrid, 0, -1, blizzardGrid.width - 1, blizzardGrid.height - 1, 0) + 1;
    final int walkBack =
        shortestRoute(blizzardGrid, blizzardGrid.width - 1, blizzardGrid.height, 0, 0, walkToGoal)
            + 1;
    final int walkToGoalAgain =
        shortestRoute(
                blizzardGrid, 0, -1, blizzardGrid.width - 1, blizzardGrid.height - 1, walkBack)
            + 1;
    log.info("Shortest total Route time: " + walkToGoalAgain);
    return walkToGoalAgain;
  }

  private static BlizzardGrid initGrid(Resource input) {
    final List<String> lines = SplitUtils.linesList(input);
    // exclude walls of blizzard grid input
    final BlizzardGrid blizzardGrid = new BlizzardGrid(lines.get(0).length() - 2, lines.size() - 2);
    int y = 0;

    for (String line : lines) {
      for (int i = 0; i < blizzardGrid.width + 1; i++) {
        if (line.charAt(i) != '.') {
          blizzardGrid.addBlizzard(line.charAt(i), i - 1, y - 1);
        }
      }
      y++;
    }
    return blizzardGrid;
  }

  private static int shortestRoute(
      BlizzardGrid blizzardGrid, int startX, int startY, int endX, int endY, int startTime) {
    final PriorityQueue<CurrentPoint> queue = new PriorityQueue<>(comparingInt(o -> o.timePassed));
    queue.add(new CurrentPoint(startX, startY, startTime));

    while (true) {
      CurrentPoint currentPathPoint = queue.poll();
      if (currentPathPoint.x == endX && currentPathPoint.y == endY) {
        log.trace("Printing out path: ");
        for (CurrentPoint point : currentPathPoint.previousPoints) {
          log.trace(valueOf(point));
        }
        log.trace(valueOf(currentPathPoint));
        return currentPathPoint.timePassed;
      }
      addPointToWestIfAvailable(blizzardGrid, queue, currentPathPoint);
      addPointToEastIfAvailable(blizzardGrid, queue, currentPathPoint);
      addPointToNorthIfAvailable(blizzardGrid, queue, currentPathPoint);
      addPointToSouthIfAvailable(blizzardGrid, queue, currentPathPoint);
      waitInPlaceIfPointAvailable(blizzardGrid, queue, currentPathPoint);
    }
  }

  private static void waitInPlaceIfPointAvailable(
      BlizzardGrid blizzardGrid, PriorityQueue<CurrentPoint> queue, CurrentPoint currentPathPoint) {
    if (blizzardGrid.pointAvailableAtTime(
        currentPathPoint.x, currentPathPoint.y, currentPathPoint.timePassed + 1)) {
      final CurrentPoint next =
          currentPathPoint.nextPointFromStep(currentPathPoint.x, currentPathPoint.y);
      addToQueueIfNotAlreadyThere(queue, next);
    }
  }

  private static void addPointToSouthIfAvailable(
      BlizzardGrid blizzardGrid, PriorityQueue<CurrentPoint> queue, CurrentPoint currentPathPoint) {
    if (currentPathPoint.y < blizzardGrid.height - 1
        && blizzardGrid.pointAvailableAtTime(
            currentPathPoint.x, currentPathPoint.y + 1, currentPathPoint.timePassed + 1)) {
      final CurrentPoint next =
          currentPathPoint.nextPointFromStep(currentPathPoint.x, currentPathPoint.y + 1);
      addToQueueIfNotAlreadyThere(queue, next);
    }
  }

  private static void addPointToNorthIfAvailable(
      BlizzardGrid blizzardGrid, PriorityQueue<CurrentPoint> queue, CurrentPoint currentPathPoint) {
    if (currentPathPoint.y > 0
        && blizzardGrid.pointAvailableAtTime(
            currentPathPoint.x, currentPathPoint.y - 1, currentPathPoint.timePassed + 1)) {
      final CurrentPoint next =
          currentPathPoint.nextPointFromStep(currentPathPoint.x, currentPathPoint.y - 1);
      addToQueueIfNotAlreadyThere(queue, next);
    }
  }

  private static void addPointToEastIfAvailable(
      BlizzardGrid blizzardGrid, PriorityQueue<CurrentPoint> queue, CurrentPoint currentPathPoint) {
    if (currentPathPoint.x < blizzardGrid.width - 1
        && currentPathPoint.y > -1
        && currentPathPoint.y < blizzardGrid.height
        && blizzardGrid.pointAvailableAtTime(
            currentPathPoint.x + 1, currentPathPoint.y, currentPathPoint.timePassed + 1)) {
      final CurrentPoint next =
          currentPathPoint.nextPointFromStep(currentPathPoint.x + 1, currentPathPoint.y);
      addToQueueIfNotAlreadyThere(queue, next);
    }
  }

  private static void addPointToWestIfAvailable(
      BlizzardGrid blizzardGrid, PriorityQueue<CurrentPoint> queue, CurrentPoint currentPathPoint) {
    if (currentPathPoint.x > 0
        && currentPathPoint.y > -1
        && currentPathPoint.y < blizzardGrid.height
        && blizzardGrid.pointAvailableAtTime(
            currentPathPoint.x - 1, currentPathPoint.y, currentPathPoint.timePassed + 1)) {
      final CurrentPoint next =
          currentPathPoint.nextPointFromStep(currentPathPoint.x - 1, currentPathPoint.y);
      addToQueueIfNotAlreadyThere(queue, next);
    }
  }

  private static void addToQueueIfNotAlreadyThere(
      PriorityQueue<CurrentPoint> queue, CurrentPoint candidate) {
    if (!queue.contains(candidate)) {
      queue.add(candidate);
    }
  }

  @Data
  @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
  static class CurrentPoint {
    @NonFinal @EqualsAndHashCode.Exclude List<CurrentPoint> previousPoints = new ArrayList<>();
    int x;
    int y;
    int timePassed;

    CurrentPoint nextPointFromStep(int x, int y) {
      final CurrentPoint nextPoint = new CurrentPoint(x, y, timePassed + 1);
      nextPoint.previousPoints = new ArrayList<>(previousPoints);
      nextPoint.previousPoints.add(this);
      return nextPoint;
    }

    @Override
    public String toString() {
      return "Time " + this.timePassed + ": (" + this.x + ", " + this.y + ")";
    }
  }

  static class BlizzardGrid {
    int width;
    int height;
    List<BlizzardRowOrColumn> rows;
    List<BlizzardRowOrColumn> columns;

    BlizzardGrid(int width, int height) {
      this.width = width;
      this.height = height;
      columns = range(0, width).mapToObj(i -> new BlizzardRowOrColumn(height)).toList();
      rows = range(0, height).mapToObj(i -> new BlizzardRowOrColumn(width)).toList();
    }

    boolean pointAvailableAtTime(int x, int y, int t) {
      if ((x == 0 && y == -1) || (x == width - 1 && y == height)) {
        return true;
      }
      return !columns.get(x).invalidPositionsAtTime(t).contains(y)
          && !rows.get(y).invalidPositionsAtTime(t).contains(x);
    }

    void addBlizzard(Character c, int x, int y) {
      switch (c) {
        case '^' -> columns.get(x).addBackwardsFacingBlizzard(y);
        case 'v' -> columns.get(x).addForwardsFacingBlizzard(y);
        case '<' -> rows.get(y).addBackwardsFacingBlizzard(x);
        case '>' -> rows.get(y).addForwardsFacingBlizzard(x);
        default -> log.trace("Ignored value: " + c);
      }
    }
  }

  @RequiredArgsConstructor
  static class BlizzardRowOrColumn {
    final int length;
    List<Integer> initialBackwardTraversingPositions = new ArrayList<>();
    List<Integer> initialForwardTraversingPositions = new ArrayList<>();

    void addBackwardsFacingBlizzard(int index) {
      initialBackwardTraversingPositions.add(index);
    }

    void addForwardsFacingBlizzard(int index) {
      initialForwardTraversingPositions.add(index);
    }

    Set<Integer> invalidPositionsAtTime(int minutesPassed) {
      final Set<Integer> newBlizzardPositions =
          new HashSet<>(
              initialBackwardTraversingPositions.size() + initialForwardTraversingPositions.size());
      for (int leftStartingIndex : initialBackwardTraversingPositions) {
        newBlizzardPositions.add(((leftStartingIndex - minutesPassed) % length + length) % length);
      }
      for (int rightStartingIndex : initialForwardTraversingPositions) {
        newBlizzardPositions.add((rightStartingIndex + minutesPassed) % length);
      }
      return newBlizzardPositions;
    }
  }
}
