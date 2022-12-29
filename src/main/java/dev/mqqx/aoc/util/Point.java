package dev.mqqx.aoc.util;

import static java.lang.Math.abs;

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

  public Point copy() {
    return new Point(x, y);
  }

  public Stream<Point> surrounding() {
    return Stream.of(left(), up(), right(), down());
  }
}
