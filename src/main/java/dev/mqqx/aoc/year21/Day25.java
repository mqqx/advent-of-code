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
    final Character[][] seaCucumbers = toCharGrid(input);

    for (int i = 1; i < 999_999; i++) {
      int moved = moveSeaCucumbers(seaCucumbers, true);
      moved += moveSeaCucumbers(seaCucumbers, false);

      if (moved == 0) {
        return i;
      }
    }

    return Integer.MIN_VALUE;
  }

  private static int moveSeaCucumbers(Character[][] seaCucumbers, boolean moveRight) {
    final List<Point> toMove = getSeaCucumbersToMove(seaCucumbers, moveRight);
    move(seaCucumbers, moveRight, toMove);
    return toMove.size();
  }

  private static List<Point> getSeaCucumbersToMove(Character[][] seaCucumbers, boolean moveRight) {
    final List<Point> toMove = new ArrayList<>();
    for (int y = 0; y < seaCucumbers.length; y++) {
      for (int x = 0; x < seaCucumbers[0].length; x++) {
        if (shouldBeMoved(seaCucumbers, moveRight, y, x)) {
          toMove.add(new Point(x, y));
        }
      }
    }
    return toMove;
  }

  private static void move(Character[][] seaCucumbers, boolean moveRight, List<Point> toMove) {
    for (Point point : toMove) {
      seaCucumbers[point.y()][point.x()] = '.';
      if (moveRight) {
        seaCucumbers[point.y()][(point.x() + 1) % seaCucumbers[0].length] = '>';
      } else {
        seaCucumbers[(point.y() + 1) % seaCucumbers.length][point.x()] = 'v';
      }
    }
  }

  private static boolean shouldBeMoved(
      Character[][] seaCucumbers, boolean moveRight, int y, int x) {
    final char toCheck = moveRight ? '>' : 'v';
    final boolean isNextEmpty =
        (moveRight
                ? seaCucumbers[y][(x + 1) % seaCucumbers[0].length]
                : seaCucumbers[(y + 1) % seaCucumbers.length][x])
            == '.';
    return seaCucumbers[y][x] == toCheck && isNextEmpty;
  }
}
