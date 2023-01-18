package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.String.valueOf;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day18 {
  static int solvePart1(Resource input) {
    final JsonParser parser = JsonParserFactory.getJsonParser();
    final List<List<Object>> snailfishNumbers = lines(input).map(parser::parseList).toList();

    Node current = parseSnailfishNumber(snailfishNumbers.get(0), 0);
    for (int i = 1; i < snailfishNumbers.size(); i++) {
      current = merge(current, parseSnailfishNumber(snailfishNumbers.get(i), 1));
      current.reduce();
    }

    final int magnitude = current.calcMagnitude();
    log.info("Magnitude: " + magnitude);
    return magnitude;
  }

  static int solvePart2(Resource input) {
    final JsonParser parser = JsonParserFactory.getJsonParser();
    final List<Node> snailfishNumbers =
        lines(input).map(parser::parseList).map(list -> parseSnailfishNumber(list, 0)).toList();

    int maxMagnitude = Integer.MIN_VALUE;
    for (Node x : snailfishNumbers) {
      for (Node y : snailfishNumbers) {
        if (!x.equals(y)) {
          final Node left = x.copy();
          final Node right = y.copy();
          Node merged = new Node(0);
          left.increaseDepth();
          right.increaseDepth();
          merged.left = left;
          merged.right = right;
          merged.reduce();

          final int magnitude = merged.calcMagnitude();
          if (magnitude > maxMagnitude) {
            maxMagnitude = magnitude;
          }
        }
      }
    }

    log.info("Largest magnitude: " + maxMagnitude);
    return maxMagnitude;
  }

  private static Node merge(Node current, Node next) {
    Node merged = new Node(0);
    current.increaseDepth();
    merged.left = current;
    merged.right = next;
    current = merged;
    return current;
  }

  private static Node parseSnailfishNumber(Object input, int depth) {
    final Node node = new Node(depth);

    if (input instanceof Integer i) {
      node.value = i;
      return node;
    } else if (input instanceof List<?> l) {
      node.left = parseSnailfishNumber(l.get(0), depth + 1);
      node.right = parseSnailfishNumber(l.get(1), depth + 1);
    }
    return node;
  }

  @EqualsAndHashCode
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class Node {
    Integer value;
    int depth;
    Node left;
    Node right;

    Node(int depth) {
      this.depth = depth;
    }

    Node copy() {
      Node copy = new Node(depth);
      if (value != null) {
        copy.value = value;
      } else {
        copy.left = left.copy();
        copy.right = right.copy();
      }
      return copy;
    }

    @Override
    public String toString() {
      if (value != null) {
        return valueOf(value);
      } else {
        return "[" + left.toString() + "," + right.toString() + "]";
      }
    }

    void increaseDepth() {
      depth++;
      if (value == null) {
        left.increaseDepth();
        right.increaseDepth();
      }
    }

    int calcMagnitude() {
      if (value != null) {
        return value;
      }
      return 3 * left.calcMagnitude() + 2 * right.calcMagnitude();
    }

    // Only to be called on the root
    void reduce() {
      boolean shouldReduce = true;
      while (shouldReduce) {
        log.trace(toString());
        shouldReduce = reduceDepth();
        if (!shouldReduce) {
          shouldReduce = reduceValue();
        }
      }
    }

    // nodeTwoPrevious != null is reachable sonar warning is a false positive
    @SuppressWarnings("squid:S2583")
    boolean reduceDepth() {
      Node nodeTwoPrevious = null;
      Node previousNodeWithValue = null;
      boolean foundPairToExplode = false;
      Integer leftover = null;
      Deque<Node> stack = new ArrayDeque<>();
      Node current = this;
      while (current != null || !stack.isEmpty()) {
        while (current != null) {
          stack.push(current);
          current = current.left;
        }
        current = stack.pop();

        if (current.value != null) {
          if (foundPairToExplode) {
            current.value += leftover;
            return true;
          } else {
            nodeTwoPrevious = previousNodeWithValue;
            previousNodeWithValue = current;
          }
        } else if (current.left.value != null
            && current.right.value != null
            && current.depth >= 4) {
          foundPairToExplode = true;
          if (nodeTwoPrevious != null) {
            nodeTwoPrevious.value += current.left.value;
          }
          leftover = current.right.value;
          current.left = null;
          current.right = null;
          current.value = 0;
        }
        current = current.right;
      }
      return foundPairToExplode;
    }

    boolean reduceValue() {
      if (value != null) {
        if (value < 10) {
          return false;
        }
        left = new Node(depth + 1);
        left.value = value / 2;
        right = new Node(depth + 1);
        right.value = value - left.value;
        value = null;
        return true;
      } else {
        return left.reduceValue() || right.reduceValue();
      }
    }
  }
}
