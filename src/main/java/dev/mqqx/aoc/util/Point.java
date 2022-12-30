package dev.mqqx.aoc.util;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.rangeClosed;
import static org.springframework.util.CollectionUtils.containsAny;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

// TODO add comparable
// public class Point implements Comparable<Point> {
public record Point(int x, int y) {
  public Point() {
    this(0, 0);
  }

  public boolean hasYGapGreaterThanOne(int yToCompare) {
    return hasYGapGreaterThan(yToCompare, 1);
  }

  public boolean hasXGapGreaterThanOne(int xToCompare) {
    return hasXGapGreaterThan(xToCompare, 1);
  }

  public boolean hasYGapGreaterThan(int yToCompare, int n) {
    return yGap(yToCompare) > n;
  }

  public boolean hasXGapGreaterThan(int xToCompare, int n) {
    return xGap(xToCompare) > n;
  }

  public int yGap(int yToCompare) {
    return abs(yToCompare - y);
  }

  public int xGap(int xToCompare) {
    return abs(xToCompare - x);
  }

  public int manDist(Point toMeasure) {
    return yGap(toMeasure.y()) + xGap(toMeasure.x());
  }

  public Point moveX(int n) {
    return new Point(x + n, y);
  }

  public Point moveY(int n) {
    return new Point(x, y + n);
  }

  public Point left() {
    return left(1);
  }

  public Point left(int n) {
    return new Point(x - n, y);
  }

  public Point up() {
    return up(1);
  }

  public Point up(int n) {
    return new Point(x, y + n);
  }

  public Point right() {
    return right(1);
  }

  public Point right(int n) {
    return new Point(x + n, y);
  }

  public Point down() {
    return down(1);
  }

  public Point down(int n) {
    return new Point(x, y - n);
  }

  public Point upLeft() {
    return new Point(x - 1, y + 1);
  }

  public Point upRight() {
    return new Point(x + 1, y + 1);
  }

  public Point downRight() {
    return new Point(x + 1, y - 1);
  }

  public Point downLeft() {
    return new Point(x - 1, y - 1);
  }

  public Point copy() {
    return new Point(x, y);
  }

  public Set<Point> horizontalPointsBetween(Point p) {
    final Set<Point> points = new HashSet<>();
    if (y == p.y()) {
      int xMin = min(x, p.x());
      int xMax = max(x, p.x());
      rangeClosed(xMin, xMax).mapToObj(i -> new Point(i, y)).forEach(points::add);
    }
    return points;
  }

  public Set<Point> verticalPointsBetween(Point p) {
    final Set<Point> points = new HashSet<>();
    if (x == p.x()) {
      int yMin = min(y, p.y());
      int yMax = max(y, p.y());
      rangeClosed(yMin, yMax).mapToObj(i -> new Point(x, i)).forEach(points::add);
    }
    return points;
  }

  public Set<Point> diagonalPointsBetween(Point p) {
    final Set<Point> points = new HashSet<>();

    if (xGap(p.x()) == yGap(p.y())) {
      int xMin = min(x, p.x());
      int xMax = max(x, p.x());
      int yMin = min(y, p.y());
      int yMax = max(y, p.y());

      int slope = slope(p);

      if (slope == 1) {
        // add all points between x/y min to max x/y max
        for (int x = xMin, y = yMin; x <= xMax && y <= yMax; x++, y++) {
          points.add(new Point(x, y));
        }
      } else {
        // if the slope is negative we need to add all points between x min/y max to x max/y min
        for (int x = xMin, y = yMax; x <= xMax && y >= yMin; x++, y--) {
          points.add(new Point(x, y));
        }
      }
    }
    return points;
  }

  public int slope(Point p) {
    return x > p.x() && y > p.y() || x < p.x() && y < p.y() ? 1 : -1;
  }

  public Set<Point> inBetweenHorizontalOrVertical(Point p) {
    final Set<Point> points = horizontalPointsBetween(p);
    points.addAll(verticalPointsBetween(p));
    return points;
  }

  public Set<Point> inBetweenHorizontalOrVerticalOrDiagonal(Point p) {
    final Set<Point> points = horizontalPointsBetween(p);
    points.addAll(verticalPointsBetween(p));
    points.addAll(diagonalPointsBetween(p));
    return points;
  }

  public Point downIfNotSurrounded(List<Point> points) {
    return containsAny(points, List.of(downLeft(), down(), downRight())) ? null : down();
  }

  public Point upIfNotSurrounded(List<Point> points) {
    return containsAny(points, List.of(upLeft(), up(), upRight())) ? null : up();
  }

  public Point leftIfNotSurrounded(List<Point> points) {
    return containsAny(points, List.of(upLeft(), left(), downLeft())) ? null : left();
  }

  public Point rightIfNotSurrounded(List<Point> points) {
    return containsAny(points, List.of(upRight(), right(), downRight())) ? null : right();
  }

  public Stream<Point> connecting() {
    return Stream.of(left(), up(), right(), down());
  }

  public Stream<Point> surrounding() {
    return Stream.of(left(), upLeft(), up(), upRight(), right(), downRight(), down(), downLeft());
  }

  public boolean hasSurroundingIn(List<Point> points) {
    return containsAny(points, surrounding().toList());
  }
}
