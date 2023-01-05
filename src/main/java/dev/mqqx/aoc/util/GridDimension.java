package dev.mqqx.aoc.util;

public record GridDimension(int minX, int minY, int maxX, int maxY) {
  public GridDimension(int[][] grid) {
    this(0, 0, grid[0].length - 1, grid.length - 1);
  }

  public static GridDimension of(int[][] grid) {
    return new GridDimension(grid);
  }

  public boolean includes(Point p) {
    return p.x() >= minX && p.y() >= minY && p.x() <= maxX && p.y() <= maxY;
  }

  public Point start() {
    return new Point(minX, minY);
  }

  public Point end() {
    return new Point(maxX, maxY);
  }
}
