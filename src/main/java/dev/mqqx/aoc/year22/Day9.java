package dev.mqqx.aoc.year22;

import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day9 {

  record HeadTail(Knot head, Knot tail) {}

  @Data
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  static class Knot {
    int x;
    int y;
    String id;

    Knot head;
    Knot tail;

    public void moveRight() {
      moveWithXFocus(tail != null ? tail::moveRight : null);
    }

    public void moveUp() {
      moveWithYFocus(tail != null ? tail::moveUp : null);
    }

    public void moveLeft() {
      moveWithXFocus(tail != null ? tail::moveLeft : null);
    }

    public void moveDown() {
      moveWithYFocus(tail != null ? tail::moveDown : null);
    }

    private void moveWithXFocus(Runnable move) {
      if (isHeadTailWithoutKnotsInBetween() && hasXGapGreaterThanOne()) {
        updateIndexWithXGapAndY();
      } else {
        updateIndexMultipleKnots(move);
      }
    }

    private void moveWithYFocus(Runnable move) {
      if (isHeadTailWithoutKnotsInBetween() && hasYGapGreaterThanOne()) {
        updateIndexWithYGapAndX();
      } else {
        updateIndexMultipleKnots(move);
      }
    }

    private void updateIndexWithXGapAndY() {
      x = (x + head.x) / 2;
      y = head.y;
    }

    private void updateIndexWithYGapAndX() {
      x = head.x;
      y = (y + head.y) / 2;
    }

    private boolean isHeadTailWithoutKnotsInBetween() {
      return tail == null && "H".equals(head.id);
    }

    private void updateIndexMultipleKnots(Runnable move) {
      if (hasYGapGreaterThanOne() && hasXGapGreaterThanOne()) {
        x = (x + head.x) / 2;
        y = (y + head.y) / 2;
      } else if (hasXGapGreaterThanOne()) {
        updateIndexWithXGapAndY();
      } else if (hasYGapGreaterThanOne()) {
        updateIndexWithYGapAndX();
      }

      if (tail != null && move != null) {
        move.run();
      }
    }

    private boolean hasYGapGreaterThanOne() {
      return Math.abs(head.y - y) > 1;
    }

    private boolean hasXGapGreaterThanOne() {
      return Math.abs(head.x - x) > 1;
    }
  }

  static int solvePart1(Resource input) {
    final List<String> moves = SplitUtils.linesList(input);

    String[][] field = walkBridge(moves, 0);

    return countVisitedPositions(field);
  }

  static int solvePart2(Resource input) {
    final List<String> moves = SplitUtils.linesList(input);

    String[][] field = walkBridge(moves, 8);

    return countVisitedPositions(field);
  }

  private static String[][] walkBridge(List<String> moves, int knotsInBetween) {
    //    final int x = 6;
    //    final int y = 5;
    //    final int x = 27;
    //    final int y = 21;
    final int x = 1000;
    final int y = 1000;

    String[][] field = new String[y][x];

    //    final int xStart = 0;
    //    final int yStart = 0;
    //    final int xStart = 11;
    //    final int yStart = 5;
    final HeadTail headTail = createHeadTail(knotsInBetween);
    final Knot head = headTail.head;
    final Knot tail = headTail.tail;

    for (String move : moves) {
      final String[] splitMove = move.split(" ");
      final String directionToMove = splitMove[0];
      int stepsToMove = parseInt(splitMove[1]);

      switch (directionToMove) {
        case "R" -> {
          while (stepsToMove > 0) {
            stepsToMove--;
            head.x++;
            head.getTail().moveRight();
            field[tail.y][tail.x] = "#";
          }
        }
        case "U" -> {
          while (stepsToMove > 0) {
            stepsToMove--;
            head.y++;
            head.getTail().moveUp();
            field[tail.y][tail.x] = "#";
          }
        }
        case "L" -> {
          while (stepsToMove > 0) {
            stepsToMove--;
            head.x--;
            head.getTail().moveLeft();
            field[tail.y][tail.x] = "#";
          }
        }
        case "D" -> {
          while (stepsToMove > 0) {
            stepsToMove--;
            head.y--;
            head.getTail().moveDown();
            field[tail.y][tail.x] = "#";
          }
        }
        default -> log.warn("Could not recognize direction: {}", directionToMove);
      }
    }
    return field;
  }

  private static HeadTail createHeadTail(int knotsInBetween) {
    final int xStart = 500;
    final int yStart = 500;
    final Knot head = new Knot(xStart, yStart, "H", null, null);
    final Knot tail = new Knot(xStart, yStart, "9", null, null);

    if (knotsInBetween > 0) {
      Knot lastKnot = null;
      for (int i = 0; i < knotsInBetween; i++) {
        final Knot knot = new Knot(xStart, yStart, String.valueOf(i + 1), lastKnot, null);

        if (i == 0) {
          head.setTail(knot);
          knot.setHead(head);
        }
        if (lastKnot != null) {
          lastKnot.setTail(knot);
        }
        lastKnot = knot;

        if (i + 1 == knotsInBetween) {
          knot.setTail(tail);
          tail.setHead(lastKnot);
        }
      }
    } else {
      head.setTail(tail);
      tail.setHead(head);
    }

    return new HeadTail(head, tail);
  }

  private static int countVisitedPositions(String[][] field) {
    int counter = 0;
    for (int i = field.length - 1; i > -1; i--) {
      String[] xRow = field[i];
      for (String element : xRow) {
        if ("#".equals(element)) {
          counter++;
        }
      }
    }
    return counter;
  }
}
