package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.toCharGrid;

import dev.mqqx.aoc.util.Point;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day25 {

  static int solvePart1(Resource input) {
    Character[][] seaCucumbers = toCharGrid(input);

    for (int i = 1; i < 999_999; i++) {
      int moved = 0;
      List<Point> toMove = new ArrayList<>();

      for (int y = 0; y < seaCucumbers.length; y++) {
        final Character[] line = seaCucumbers[y];
        for (int x = 0; x < line.length; x++) {
          if (seaCucumbers[y][x] == '>' && seaCucumbers[y][(x + 1) % line.length] == '.') {
            toMove.add(new Point(x, y));
          }
        }
      }

      for (Point point : toMove) {
        seaCucumbers[point.y()][point.x()] = '.';
        seaCucumbers[point.y()][(point.x() + 1) % seaCucumbers[0].length] = '>';
      }

      moved += toMove.size();
      toMove.clear();

      for (int y = 0; y < seaCucumbers.length; y++) {
        final Character[] line = seaCucumbers[y];
        for (int x = 0; x < line.length; x++) {
          if (seaCucumbers[y][x] == 'v' && seaCucumbers[(y + 1) % seaCucumbers.length][x] == '.') {
            toMove.add(new Point(x, y));
          }
        }
      }

      for (Point point : toMove) {
        seaCucumbers[point.y()][point.x()] = '.';
        seaCucumbers[(point.y() + 1) % seaCucumbers.length][point.x()] = 'v';
      }
      moved += toMove.size();

      if (moved == 0) {
        return i;
      }
    }

    return -1;
  }
}
