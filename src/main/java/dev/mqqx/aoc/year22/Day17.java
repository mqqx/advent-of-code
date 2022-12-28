package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.read;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day17 {
  private static final int ROCKS_TO_FALL_1 = 2_022;
  private static final long ROCKS_TO_FALL_2 = 1_000_000_000_000L;

  private static final int HEIGHT = 10_000;
  private static final int WIDTH = 7;
  private static final int CACHE_NAME_LENGTH = 2_000;

  public static int solvePart1(Resource input) {
    final JetDirection jetDirection =
        new JetDirection(initializeJetPattern(input), new AtomicInteger());
    RockTower.reset(new boolean[HEIGHT][WIDTH]);
    int y = -1;
    for (int i = 0; i < ROCKS_TO_FALL_1; i++) {
      y = simulateNextRock(jetDirection, y);
    }
    log.info("Height: " + (y + 1));
    return y + 1;
  }

  public static long solvePart2(Resource input) {
    final JetDirection jetDirection =
        new JetDirection(initializeJetPattern(input), new AtomicInteger());

    boolean[][] grid = new boolean[HEIGHT][WIDTH];
    RockTower.reset(grid);
    int y = -1;
    long numberOfHeightAdjusts = 0L;
    long distanceOfHeightAdjust = 0L;
    boolean cacheAlreadyHit = false;
    final Map<String, HeightAndNumBricks> cache = new HashMap<>();

    for (long i = 0; i < ROCKS_TO_FALL_2; ++i) {
      String cacheCheckName = getCacheNameFromGrid(y);
      final boolean shouldBeCached = !cacheAlreadyHit && cacheCheckName != null;

      if (shouldBeCached && cache.containsKey(cacheCheckName)) {
        cacheAlreadyHit = true;
        final HeightAndNumBricks cachedHeightAndNumBricks = cache.get(cacheCheckName);
        distanceOfHeightAdjust = y - cachedHeightAndNumBricks.height;
        final long numBricksDuringPeriod = i - cachedHeightAndNumBricks.numBricks;
        final long heightAdjust = ROCKS_TO_FALL_2 - i;

        // skip i to end of last repeating cycle < ROCKS_TO_FALL_2
        numberOfHeightAdjusts = heightAdjust / numBricksDuringPeriod;
        i = i + (numberOfHeightAdjusts * numBricksDuringPeriod);
      } else if (shouldBeCached) {
        cache.put(cacheCheckName, new HeightAndNumBricks(y, i));
      }

      y = simulateNextRock(jetDirection, y);
    }
    long height = y + 1L;
    height += numberOfHeightAdjusts * distanceOfHeightAdjust;
    log.info("Height: " + height);
    return height;
  }

  private static int simulateNextRock(JetDirection jetDirection, int y) {
    // Figure out where new rock should start
    Rock rock = RockTower.getNextRock(y + 4);
    int thisRocksHeight;
    do {
      // Simulate wind push
      rock.move(jetDirection.next());
      // Simulate fall
      thisRocksHeight = rock.drop();
    } while (thisRocksHeight == -1);

    if (thisRocksHeight > y) {
      y = thisRocksHeight;
    }
    return y;
  }

  private static List<Boolean> initializeJetPattern(Resource input) {
    return read(input).chars().mapToObj(jet -> jet == '<').toList();
  }

  private static String getCacheNameFromGrid(int y) {
    if (y < CACHE_NAME_LENGTH) {
      return null;
    }
    StringBuilder cacheName = new StringBuilder();
    for (int i = 0; i < CACHE_NAME_LENGTH; ++i) {
      int value = 0;
      for (int x = 0; x < WIDTH; ++x) {
        value <<= 1;
        if (RockTower.grid[y - i][x]) {
          value += 1;
        }
      }
      cacheName.append(value).append(".");
    }
    return cacheName.toString();
  }

  record HeightAndNumBricks(long height, long numBricks) {}

  record JetDirection(List<Boolean> pattern, AtomicInteger current) {
    boolean next() {
      return pattern.get(current.getAndIncrement() % pattern.size());
    }
  }

  private static class RockTower {
    private static int nextRockId = 0;
    private static boolean[][] grid;

    private RockTower() {}

    static void reset(boolean[][] newGrid) {
      nextRockId = 0;
      grid = newGrid;
    }

    static Rock getNextRock(int y) {
      Rock rock =
          switch (nextRockId) {
            case 0 -> new HorizontalRock(grid, y);
            case 1 -> new PlusRock(grid, y);
            case 2 -> new ReversedLRock(grid, y);
            case 3 -> new VerticalRock(grid, y);
            case 4 -> new SquareRock(grid, y);
            default -> throw new IllegalArgumentException(
                "Cannot recognize rock id: " + nextRockId);
          };
      nextRockId++;
      nextRockId %= 5;
      return rock;
    }
  }

  private interface Rock {
    // The rock moves itself the given direction, if possible
    void move(boolean moveLeft);

    // The rock moves itself down and returns -1
    // Otherwise, inserts itself into grid and
    // returns the height of its highest element.
    int drop();
  }

  /** Rock with shape #### */
  private static class HorizontalRock implements Rock {
    private final boolean[][] grid;
    private int y;
    private int x;

    HorizontalRock(boolean[][] grid, int height) {
      this.grid = grid;
      this.y = height;
      this.x = 2;
    }

    @Override
    public void move(boolean moveLeft) {
      if (moveLeft) {
        if (x == 0) {
          return;
        }
        if (grid[y][x - 1]) {
          return;
        }
        x--;
      } else {
        if (x == WIDTH - 4) {
          return;
        }
        if (grid[y][x + 4]) {
          return;
        }
        x++;
      }
    }

    @Override
    public int drop() {
      if (y == 0
          || grid[y - 1][x]
          || grid[y - 1][x + 1]
          || grid[y - 1][x + 2]
          || grid[y - 1][x + 3]) {
        grid[y][x] = true;
        grid[y][x + 1] = true;
        grid[y][x + 2] = true;
        grid[y][x + 3] = true;
        return y;
      } else {
        y--;
        return -1;
      }
    }
  }

  /** Rock with shape .#. ### .#. */
  private static class PlusRock implements Rock {
    private final boolean[][] grid;
    private int y;
    private int x;

    PlusRock(boolean[][] grid, int height) {
      this.grid = grid;
      this.y = height;
      this.x = 2;
    }

    @Override
    public void move(boolean moveLeft) {
      if (moveLeft) {
        if (x == 0) {
          return;
        }
        if (grid[y + 2][x] || grid[y + 1][x - 1] || grid[y][x]) {
          return;
        }
        x--;
      } else {
        if (x == WIDTH - 3) {
          return;
        }
        if (grid[y + 2][x + 2] || grid[y + 1][x + 3] || grid[y][x + 2]) {
          return;
        }
        x++;
      }
    }

    @Override
    public int drop() {
      if (y == 0 || grid[y][x] || grid[y - 1][x + 1] || grid[y][x + 2]) {
        grid[y][x + 1] = true;
        grid[y + 1][x] = true;
        grid[y + 1][x + 1] = true;
        grid[y + 1][x + 2] = true;
        grid[y + 2][x + 1] = true;
        return y + 2;
      } else {
        y--;
        return -1;
      }
    }
  }

  /** Rock with shape ..# ..# ### */
  private static class ReversedLRock implements Rock {
    private final boolean[][] grid;
    private int y;
    private int x;

    ReversedLRock(boolean[][] grid, int height) {
      this.grid = grid;
      this.y = height;
      this.x = 2;
    }

    @Override
    public void move(boolean moveLeft) {
      if (moveLeft) {
        if (x == 0) {
          return;
        }
        if (grid[y + 2][x + 1] || grid[y + 1][x + 1] || grid[y][x - 1]) {
          return;
        }
        x--;
      } else {
        if (x == WIDTH - 3) {
          return;
        }
        if (grid[y + 2][x + 3] || grid[y + 1][x + 3] || grid[y][x + 3]) {
          return;
        }
        x++;
      }
    }

    @Override
    public int drop() {
      if (y == 0 || grid[y - 1][x] || grid[y - 1][x + 1] || grid[y - 1][x + 2]) {
        grid[y][x] = true;
        grid[y][x + 1] = true;
        grid[y][x + 2] = true;
        grid[y + 1][x + 2] = true;
        grid[y + 2][x + 2] = true;
        return y + 2;
      } else {
        y--;
        return -1;
      }
    }
  }

  /** Rock with shape # # # # */
  private static class VerticalRock implements Rock {
    private final boolean[][] grid;
    private int y;
    private int x;

    VerticalRock(boolean[][] grid, int height) {
      this.grid = grid;
      this.y = height;
      this.x = 2;
    }

    @Override
    public void move(boolean moveLeft) {
      if (moveLeft) {
        if (x == 0) {
          return;
        }
        if (grid[y][x - 1] || grid[y + 1][x - 1] || grid[y + 2][x - 1] || grid[y + 3][x - 1]) {
          return;
        }
        x--;
      } else {
        if (x == WIDTH - 1) {
          return;
        }
        if (grid[y][x + 1] || grid[y + 1][x + 1] || grid[y + 2][x + 1] || grid[y + 3][x + 1]) {
          return;
        }
        x++;
      }
    }

    @Override
    public int drop() {
      if (y == 0 || grid[y - 1][x]) {
        grid[y][x] = true;
        grid[y + 1][x] = true;
        grid[y + 2][x] = true;
        grid[y + 3][x] = true;
        return y + 3;
      } else {
        y--;
        return -1;
      }
    }
  }

  /** Rock with shape ## ## */
  private static class SquareRock implements Rock {
    private final boolean[][] grid;
    private int y;
    private int x;

    SquareRock(boolean[][] grid, int height) {
      this.grid = grid;
      this.y = height;
      this.x = 2;
    }

    @Override
    public void move(boolean moveLeft) {
      if (moveLeft) {
        if (x == 0) {
          return;
        }
        if (grid[y + 1][x - 1] || grid[y][x - 1]) {
          return;
        }
        x--;
      } else {
        if (x == WIDTH - 2) {
          return;
        }
        if (grid[y + 1][x + 2] || grid[y][x + 2]) {
          return;
        }
        x++;
      }
    }

    @Override
    public int drop() {
      if (y == 0 || grid[y - 1][x] || grid[y - 1][x + 1]) {
        grid[y][x] = true;
        grid[y][x + 1] = true;
        grid[y + 1][x] = true;
        grid[y + 1][x + 1] = true;
        return y + 1;
      } else {
        y--;
        return -1;
      }
    }
  }
}
