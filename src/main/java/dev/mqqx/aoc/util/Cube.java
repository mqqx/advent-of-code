package dev.mqqx.aoc.util;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.valueOf;
import static java.util.Optional.empty;

import java.math.BigInteger;
import java.util.Optional;

public record Cube(Point3D lower, Point3D upper) {
  private Point3D upperOverlapping(Cube o) {
    return new Point3D(
        min(upper.x(), o.upper().x()),
        min(upper.y(), o.upper().y()),
        min(upper.z(), o.upper().z()));
  }

  private Point3D lowerOverlapping(Cube o) {
    return new Point3D(
        max(lower.x(), o.lower().x()),
        max(lower.y(), o.lower().y()),
        max(lower.z(), o.lower().z()));
  }

  public boolean isEnclosing(Cube o) {
    return o.lower().x() >= lower.x()
        && o.lower().y() >= lower.y()
        && o.lower().z() >= lower.z()
        && o.upper().x() <= upper.x()
        && o.upper().y() <= upper.y()
        && o.upper().z() <= upper.z();
  }

  public BigInteger volume() {
    return new BigInteger(valueOf(lower.xGap(upper.x())))
        .multiply(new BigInteger(valueOf(lower.yGap(upper.y()))))
        .multiply(new BigInteger(valueOf(lower.zGap(upper.z()))));
  }

  public Optional<Cube> overlapping(Cube o) {
    Cube potentialOverlappingCube =
        new Cube(
            new Point3D(
                max(lower.x(), o.lower().x()),
                max(lower.y(), o.lower().y()),
                max(lower.z(), o.lower().z())),
            new Point3D(
                min(upper.x(), o.upper().x()),
                min(upper.y(), o.upper().y()),
                min(upper.z(), o.upper().z())));

    if (potentialOverlappingCube.isValid()) {
      return Optional.of(new Cube(lowerOverlapping(o), upperOverlapping(o)));
    }

    return empty();
  }

  private boolean isValid() {
    return lower.x() < upper.x() && lower.y() < upper.y() && lower.z() < upper.z();
  }
}
