package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

import dev.mqqx.aoc.util.Point;
import java.util.HashSet;
import java.util.function.BooleanSupplier;
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

  record Chain(Link head, Link tail) {}

  @Data
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  static class Link {
    Point pos;
    String id;

    Link head;
    Link tail;

    public void moveRight() {
      moveHead(() -> head.pos = head.pos.right());
      moveWithXFocus(tail != null ? tail::moveRight : null);
    }

    public void moveUp() {
      moveHead(() -> head.pos = head.pos.up());
      moveWithYFocus(tail != null ? tail::moveUp : null);
    }

    public void moveLeft() {
      moveHead(() -> head.pos = head.pos.left());
      moveWithXFocus(tail != null ? tail::moveLeft : null);
    }

    public void moveDown() {
      moveHead(() -> head.pos = head.pos.down());
      moveWithYFocus(tail != null ? tail::moveDown : null);
    }

    private void moveHead(Runnable moveHead) {
      if (head.getHead() == null) {
        moveHead.run();
      }
    }

    private void moveWithXFocus(Runnable move) {
      moveWithFocus(this::hasXGapGreaterThanOne, this::updateIndexWithXGapAndY, move);
    }

    private void moveWithYFocus(Runnable move) {
      moveWithFocus(this::hasYGapGreaterThanOne, this::updateIndexWithYGapAndX, move);
    }

    private void moveWithFocus(
        BooleanSupplier hasGapGreaterThanOne, Runnable updateIndexWithGap, Runnable move) {
      if (isChainWithoutLinksInBetween() && hasGapGreaterThanOne.getAsBoolean()) {
        updateIndexWithGap.run();
      } else {
        updateIndexMultipleLinks(move);
      }
    }

    private void updateIndexWithXGapAndY() {
      pos = new Point((pos.x() + head.pos.x()) / 2, head.pos.y());
    }

    private void updateIndexWithYGapAndX() {
      pos = new Point(head.pos.x(), (pos.y() + head.pos.y()) / 2);
    }

    private boolean isChainWithoutLinksInBetween() {
      return tail == null && "H".equals(head.id);
    }

    private void updateIndexMultipleLinks(Runnable move) {
      if (hasYGapGreaterThanOne() && hasXGapGreaterThanOne()) {
        pos = new Point((pos.x() + head.pos.x()) / 2, (pos.y() + head.pos.y()) / 2);
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
      return pos.hasYGapGreaterThan(head.pos.y(), 1);
    }

    private boolean hasXGapGreaterThanOne() {
      return pos.hasXGapGreaterThan(head.pos.x(), 1);
    }
  }

  static int solvePart1(Resource input) {
    return moveRope(input, 1);
  }

  static int solvePart2(Resource input) {
    return moveRope(input, 9);
  }

  private static int moveRope(Resource input, int additionalLinks) {
    final Chain chain = createChain(additionalLinks);
    final Link head = chain.head;
    final Link tail = chain.tail;

    final HashSet<Point> visitedPositions = new HashSet<>();
    visitedPositions.add(tail.pos);

    for (String moveString : linesList(input)) {
      final String[] splitMove = moveString.split(" ");
      final Runnable moveDirection = getMoveDirection(head, splitMove[0]);
      move(moveDirection, tail, visitedPositions, parseInt(splitMove[1]));
    }

    return visitedPositions.size();
  }

  private static Runnable getMoveDirection(Link head, String directionToMove) {
    return switch (directionToMove) {
      case "R" -> head.getTail()::moveRight;
      case "U" -> head.getTail()::moveUp;
      case "L" -> head.getTail()::moveLeft;
      case "D" -> head.getTail()::moveDown;
      default -> {
        log.warn("Could not recognize direction: {}", directionToMove);
        yield null;
      }
    };
  }

  private static void move(
      Runnable moveDirection, Link tail, HashSet<Point> visitedPositions, int stepsToMove) {
    if (moveDirection == null) {
      return;
    }

    while (stepsToMove > 0) {
      stepsToMove--;
      moveDirection.run();
      visitedPositions.add(tail.pos.copy());
    }
  }

  private static Chain createChain(int additionalLinks) {
    final Link head = new Link(new Point(), "H", null, null);

    Link lastLink = null;
    for (int i = 0; i < additionalLinks; i++) {
      final Link link = new Link(new Point(), valueOf(i + 1), lastLink, null);

      if (i == 0) {
        head.setTail(link);
        link.setHead(head);
      }
      if (lastLink != null) {
        lastLink.setTail(link);
      }
      lastLink = link;
    }

    return new Chain(head, lastLink);
  }
}
