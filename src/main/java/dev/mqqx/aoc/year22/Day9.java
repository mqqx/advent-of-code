package dev.mqqx.aoc.year22;

import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day9 {

  @Data
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  static class Knot {
    int x;
    int y;

    Knot head;
    Knot tail;

    public void moveRight() {
      if (Math.abs(head.x - x) > 1) {
        x = head.x - 1;
        y = head.y;
      }
      if (tail != null) {
        tail.moveRight();
      }
    }

    public void moveUp() {
      if (Math.abs(head.y - y) > 1) {
        x = head.x;
        y = head.y - 1;
      }
      if (tail != null) {
        tail.moveUp();
      }
    }

    public void moveLeft() {
      if (Math.abs(head.x - x) > 1) {
        x = head.x + 1;
        y = head.y;
      }
      if (tail != null) {
        tail.moveLeft();
      }
    }

    public void moveDown() {
      if (Math.abs(head.y - y) > 1) {
        x = head.x;
        y = head.y + 1;
      }
      if (tail != null) {
        tail.moveDown();
      }
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
    final int x = 1000;
    final int y = 1000;

    String[][] field = new String[y][x];

    //    final int xStart = 0;
    //    final int yStart = 0;
    final int xStart = 500;
    final int yStart = 500;
    final Knot head = new Knot(xStart, yStart, null, null);
    final Knot tail = new Knot(xStart, yStart, null, null);

    if (knotsInBetween > 0) {
      Knot lastKnot = null;
      for (int i = 0; i < knotsInBetween; i++) {
        final Knot knot = new Knot(xStart, yStart, lastKnot, null);

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

    field[head.x][head.y] = "#";

    for (String move : moves) {
      final String[] splitMove = move.split(" ");
      int stepsToMove = parseInt(splitMove[1]);

      switch (splitMove[0]) {
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
        default -> System.out.println("could not recognize");
      }
    }
    return field;
  }

  private static int countVisitedPositions(String[][] field) {
    int counter = 0;
    for (int i = field.length - 1; i > -1; i--) {
      String[] xRow = field[i];
      for (int j = 0; j < xRow.length; j++) {
        String element = xRow[j];
        if ("#".equals(element)) {
          counter++;
          System.out.print("#");
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
    return counter;
  }
}
