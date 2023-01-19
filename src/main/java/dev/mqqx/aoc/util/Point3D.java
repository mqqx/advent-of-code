package dev.mqqx.aoc.util;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public record Point3D(int x, int y, int z) {
  public static final Point3D ORIGIN = new Point3D(0, 0, 0);

  public Point3D(String[] splitPoint) {
    this(parseInt(splitPoint[0]), parseInt(splitPoint[1]), parseInt(splitPoint[2]));
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
}
