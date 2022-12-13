package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Math.min;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day12 {

  record Grid(Character[][] map, Point start, Point end) {}

  record Elevation(Point coordinates, Character elevation) {}

  static int solvePart1(Resource input) {
    final Grid grid = readMap(linesList(input));
    return findShortestPathLength(grid);
  }

  static int solvePart2(Resource input) {
    Grid grid = readMap(linesList(input));

    int shortestPath = Integer.MAX_VALUE;

    for (int i = 0; i < grid.map.length; i++) {
      final Character[] squares = grid.map[i];
      for (int j = 0; j < squares.length; j++) {
        if ('a' == grid.map[i][j]) {
          grid = new Grid(grid.map, new Point(j, i), grid.end);
          shortestPath = min(shortestPath, findShortestPathLength(grid));
        }
      }
    }

    return shortestPath;
  }

  private static int findShortestPathLength(Grid grid) {
    final HashMap<Point, Integer> stepsCounter = new HashMap<>();
    final LinkedList<Point> queue = new LinkedList<>();
    stepsCounter.put(grid.start, 0);
    queue.add(grid.start);

    while (!queue.isEmpty()) {
      Point current = queue.poll();
      if (current.equals(grid.end)) {
        return stepsCounter.get(grid.end);
      }

      final List<Elevation> surroundingElevations = getSurroundingElevations(grid.map, current);
      for (Elevation elevation : surroundingElevations) {
        final Character currentChar = getCurrentChar(grid.map, current);

        if (elevation.elevation > currentChar + 1) {
          continue;
        }
        int nextStepCounter = stepsCounter.get(current) + 1;
        final Point coordinates = elevation.coordinates;
        if (nextStepCounter < stepsCounter.getOrDefault(coordinates, Integer.MAX_VALUE)) {
          stepsCounter.put(coordinates, nextStepCounter);
          queue.add(coordinates);
        }
      }
    }
    return Integer.MAX_VALUE;
  }

  private static Character getCurrentChar(Character[][] map, Point cur) {
    Character currentChar = map[cur.y][cur.x];
    if (currentChar == 'S') {
      currentChar = 'a' - 1;
    } else if (currentChar == 'E') {
      currentChar = 'z' + 1;
    }
    return currentChar;
  }

  private static List<Elevation> getSurroundingElevations(
      Character[][] map, Point currentPosition) {
    final Character currentChar = map[currentPosition.y][currentPosition.x];
    final List<Elevation> elevations = new ArrayList<>();

    addElevationIfIsAtMostOneHigher(
        map, currentPosition.y, currentPosition.x - 1, elevations, currentChar);
    addElevationIfIsAtMostOneHigher(
        map, currentPosition.y - 1, currentPosition.x, elevations, currentChar);
    addElevationIfIsAtMostOneHigher(
        map, currentPosition.y + 1, currentPosition.x, elevations, currentChar);
    addElevationIfIsAtMostOneHigher(
        map, currentPosition.y, currentPosition.x + 1, elevations, currentChar);
    return elevations;
  }

  private static void addElevationIfIsAtMostOneHigher(
      Character[][] map, int y, int x, List<Elevation> elevations, Character currentChar) {
    if (y > -1 && y < map.length && x < map[0].length && x > -1) {
      Character surroundingChar = map[y][x];

      if (currentChar == 'S') {
        currentChar = 'a';
      }
      if (surroundingChar == 'E') {
        surroundingChar = 'z';
      }

      final Point newCoordinates = new Point(x, y);
      if (surroundingChar <= currentChar + 1) {
        elevations.add(new Elevation(newCoordinates, surroundingChar));
      }
    }
  }

  private static Grid readMap(List<String> lines) {
    final Character[][] map = new Character[lines.size()][lines.get(0).length()];
    Point start = null;
    Point end = null;

    for (int i = 0; i < lines.size(); i++) {
      final char[] elevations = lines.get(i).toCharArray();
      for (int j = 0; j < elevations.length; j++) {
        map[i][j] = elevations[j];
        if ('S' == map[i][j]) {
          start = new Point(j, i);
        } else if ('E' == map[i][j]) {
          end = new Point(j, i);
        }
      }
    }
    return new Grid(map, start, end);
  }
}
