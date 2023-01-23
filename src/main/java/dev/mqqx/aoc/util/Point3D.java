package dev.mqqx.aoc.util;

import static java.lang.Integer.compare;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import lombok.NonNull;

public record Point3D(int x, int y, int z) implements Comparable<Point3D> {
  public static final Point3D ORIGIN = new Point3D(0, 0, 0);

  public Point3D(String[] splitPoint) {
    this(parseInt(splitPoint[0]), parseInt(splitPoint[1]), parseInt(splitPoint[2]));
  }

  public Point3D(String x, String y, String z) {
    this(parseInt(x), parseInt(y), parseInt(z));
  }

  public Point3D increaseX() {
    return moveX(1);
  }

  public Point3D increaseY() {
    return moveY(1);
  }

  public Point3D increaseZ() {
    return moveZ(1);
  }

  public Point3D decreaseX() {
    return moveX(-1);
  }

  public Point3D decreaseY() {
    return moveY(-1);
  }

  public Point3D decreaseZ() {
    return moveZ(-1);
  }

  public Point3D moveX(int toMove) {
    return new Point3D(x + toMove, y, z);
  }

  public Point3D moveY(int toMove) {
    return new Point3D(x, y + toMove, z);
  }

  public Point3D moveZ(int toMove) {
    return new Point3D(x, y, z + toMove);
  }

  public int xGap(int xToCompare) {
    return abs(xToCompare - x);
  }

  public int yGap(int yToCompare) {
    return abs(yToCompare - y);
  }

  public int zGap(int zToCompare) {
    return abs(zToCompare - z);
  }

  public int manDist(Point3D toMeasure) {
    return xGap(toMeasure.x()) + yGap(toMeasure.y()) + zGap(toMeasure.z());
  }

  public Point3D diff(Point3D o) {
    return new Point3D(x - o.x(), y - o.y(), z - o.z());
  }

  public Point3D sum(Point3D o) {
    return new Point3D(x + o.x(), y + o.y(), z + o.z());
  }

  @Override
  public int compareTo(@NonNull Point3D other) {
    if (x != other.x()) {
      return compare(x, other.x());
    } else if (y != other.y()) {
      return compare(y, other.y());
    } else {
      return compare(z, other.z());
    }
  }
}
