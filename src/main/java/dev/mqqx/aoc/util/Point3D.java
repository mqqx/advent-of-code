package dev.mqqx.aoc.util;

import static java.lang.Integer.parseInt;

public record Point3D(int x, int y, int z) {
  public static final Point3D ORIGIN = new Point3D(0, 0, 0);

  public Point3D(String[] splitPoint) {
    this(parseInt(splitPoint[0]), parseInt(splitPoint[1]), parseInt(splitPoint[2]));
  }

  public Point3D diff(Point3D o) {
    return new Point3D(x - o.x(), y - o.y(), z - o.z());
  }

  public Point3D sum(Point3D o) {
    return new Point3D(x + o.x(), y + o.y(), z + o.z());
  }
}
