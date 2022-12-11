package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;

import java.awt.Point;
import java.util.HashSet;
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
    Point pos;
    String id;

    Knot head;
    Knot tail;

    public void moveRight() {
      moveHead(() -> head.pos.x++);
      moveWithXFocus(tail != null ? tail::moveRight : null);
    }

    public void moveUp() {
      moveHead(() -> head.pos.y++);
      moveWithYFocus(tail != null ? tail::moveUp : null);
    }

    public void moveLeft() {
      moveHead(() -> head.pos.x--);
      moveWithXFocus(tail != null ? tail::moveLeft : null);
    }

    public void moveDown() {
      moveHead(() -> head.pos.y--);
      moveWithYFocus(tail != null ? tail::moveDown : null);
    }

    private void moveHead(Runnable moveHead) {
      if (head.getHead() == null) {
        moveHead.run();
      }
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
      pos.setLocation((pos.x + head.pos.x) / 2, head.pos.y);
    }

    private void updateIndexWithYGapAndX() {
      pos.setLocation(head.pos.x, (pos.y + head.pos.y) / 2);
    }

    private boolean isHeadTailWithoutKnotsInBetween() {
      return tail == null && "H".equals(head.id);
    }

    private void updateIndexMultipleKnots(Runnable move) {
      if (hasYGapGreaterThanOne() && hasXGapGreaterThanOne()) {
        pos.setLocation((pos.x + head.pos.x) / 2, (pos.y + head.pos.y) / 2);
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
      return Math.abs(head.pos.y - pos.y) > 1;
    }

    private boolean hasXGapGreaterThanOne() {
      return Math.abs(head.pos.x - pos.x) > 1;
    }
  }

  static int solvePart1(Resource input) {
    return moveRope(input, 0);
  }

  static int solvePart2(Resource input) {
    return moveRope(input, 8);
  }

  private static int moveRope(Resource input, int knotsInBetween) {
    final HeadTail headTail = createHeadTail(knotsInBetween);
    final Knot head = headTail.head;
    final Knot tail = headTail.tail;

    final HashSet<Point> visitedPositions = new HashSet<>();
    visitedPositions.add(tail.pos);

    for (String moveString : linesList(input)) {
      final String[] splitMove = moveString.split(" ");
      final Runnable moveDirection = getMoveDirection(head, splitMove[0]);
      move(moveDirection, tail, visitedPositions, parseInt(splitMove[1]));
    }

    return visitedPositions.size();
  }

  private static Runnable getMoveDirection(Knot head, String directionToMove) {
    Runnable moveDirection = null;

    switch (directionToMove) {
      case "R" -> moveDirection = head.getTail()::moveRight;
      case "U" -> moveDirection = head.getTail()::moveUp;
      case "L" -> moveDirection = head.getTail()::moveLeft;
      case "D" -> moveDirection = head.getTail()::moveDown;
      default -> log.warn("Could not recognize direction: {}", directionToMove);
    }
    return moveDirection;
  }

  private static void move(
      Runnable moveDirection, Knot tail, HashSet<Point> visitedPositions, int stepsToMove) {
    if (moveDirection == null) {
      return;
    }

    while (stepsToMove > 0) {
      stepsToMove--;
      moveDirection.run();
      visitedPositions.add(new Point(tail.pos.x, tail.pos.y));
    }
  }

  private static HeadTail createHeadTail(int knotsInBetween) {
    final Knot head = new Knot(new Point(), "H", null, null);
    final Knot tail = new Knot(new Point(), "9", null, null);

    if (knotsInBetween > 0) {
      Knot lastKnot = null;
      for (int i = 0; i < knotsInBetween; i++) {
        final Knot knot = new Knot(new Point(), String.valueOf(i + 1), lastKnot, null);

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
}
