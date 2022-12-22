package dev.mqqx.aoc.year22;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day22 {
  static int solvePart1(Resource input) {
    final List<String> lines = SplitUtils.linesList(input);
    final int initialX = lines.get(0).indexOf('.');
    final Position initialPosition = new Position(initialX, 0, Direction.RIGHT);
    final Boolean[][] field = initializeField(lines);

    return walkField(lines, initialPosition, field);
  }

  static int solvePart2(Resource input) {
    final List<String> lines = SplitUtils.linesList(input);
    final int initialX = lines.get(0).indexOf('.');
    final CubePosition initialPosition = new CubePosition(initialX, 0, Direction.RIGHT);
    final Boolean[][] field = initializeField(lines);

    return walkField(lines, initialPosition, field);
  }

  private static int walkField(List<String> lines, Position position, Boolean[][] field) {
    final String commandsLine = lines.get(lines.size() - 1);
    final List<Character> directions = parseDirections(commandsLine);
    final List<Integer> stepsToMove = parseSteps(commandsLine);

    for (int i = 0; i < stepsToMove.size(); i++) {
      position.move(stepsToMove.get(i), field);

      if (i < directions.size()) {
        position.turn(directions.get(i));
      }
    }

    return position.toFinalPassword();
  }

  private static List<Integer> parseSteps(String commandsLine) {
    return stream(commandsLine.split("[L,R]")).map(Integer::parseInt).toList();
  }

  private static List<Character> parseDirections(String commandsLine) {
    return stream(commandsLine.split("\\d+"))
        .filter(direction -> !direction.isEmpty())
        .map(command1 -> command1.charAt(0))
        .toList();
  }

  private static Boolean[][] initializeField(List<String> lines) {
    final List<String> groundToWalk = lines.subList(0, lines.size() - 2);
    final int columns = groundToWalk.stream().map(String::length).max(Integer::compare).orElse(-1);
    final int rows = groundToWalk.size();

    final Boolean[][] field = new Boolean[rows][columns];

    for (int y = 0; y < rows; y++) {
      final char[] chars = groundToWalk.get(y).toCharArray();
      for (int x = 0; x < chars.length; x++) {
        if (' ' != chars[x]) {
          field[y][x] = '.' == chars[x];
        }
      }
    }
    return field;
  }

  @AllArgsConstructor
  @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
  private enum Direction {
    RIGHT(0),
    DOWN(1),
    LEFT(2),
    UP(3);
    int id;

    private static final List<Direction> ALL = asList(Direction.values());

    static Direction byId(int id) {
      return ALL.get(id);
    }
  }

  @AllArgsConstructor
  @NoArgsConstructor
  private static class Position {
    private static final char TURN_CLOCKWISE = 'R';
    private static final char TURN_COUNTERCLOCKWISE = 'L';
    int x;
    int y;

    Direction direction;

    int toFinalPassword() {
      return (y + 1) * 1_000 + (x + 1) * 4 + direction.id;
    }

    void turn(char toDirection) {
      int curDirection = direction.id;
      if (toDirection == TURN_CLOCKWISE) {
        curDirection++;
      } else if (toDirection == TURN_COUNTERCLOCKWISE) {
        curDirection--;
      }

      direction = Direction.byId((curDirection + 4) % 4);
    }

    void move(int stepsToMove, Boolean[][] field) {
      for (int i = 0; i < stepsToMove; ++i) {
        moveOneStep(field);
      }
    }

    protected void moveOneStep(Boolean[][] grid) {
      if (direction == Direction.RIGHT) {
        moveRight(grid);
      } else if (direction == Direction.DOWN) {
        moveDown(grid);
      } else if (direction == Direction.LEFT) {
        moveLeft(grid);
      } else if (direction == Direction.UP) {
        moveUp(grid);
      }
    }

    protected void moveUp(Boolean[][] grid) {
      int nextY = y - 1;
      nextY += grid.length;
      nextY %= grid.length;
      while (grid[nextY][x] == null) {
        nextY--;
        nextY += grid.length;
        nextY %= grid.length;
      }
      if (Boolean.TRUE.equals(grid[nextY][x])) {
        y = nextY;
      }
    }

    protected void moveLeft(Boolean[][] grid) {
      int nextX = x - 1;
      nextX += grid[y].length;
      nextX %= grid[y].length;
      while (grid[y][nextX] == null) {
        nextX--;
        nextX += grid[y].length;
        nextX %= grid[y].length;
      }
      if (Boolean.TRUE.equals(grid[y][nextX])) {
        x = nextX;
      }
    }

    protected void moveDown(Boolean[][] grid) {
      int nextY = y + 1;
      nextY %= grid.length;
      while (grid[nextY][x] == null) {
        nextY++;
        nextY %= grid.length;
      }
      if (Boolean.TRUE.equals(grid[nextY][x])) {
        y = nextY;
      }
    }

    protected void moveRight(Boolean[][] grid) {
      int nextX = x + 1;
      nextX %= grid[y].length;
      while (grid[y][nextX] == null) {
        nextX++;
        nextX %= grid[y].length;
      }
      if (Boolean.TRUE.equals(grid[y][nextX])) {
        x = nextX;
      }
    }
  }

  private static class CubePosition extends Position {
    CubePosition(int x, int y, Direction direction) {
      super(x, y, direction);
    }

    @Override
    protected void moveUp(Boolean[][] grid) {
      int nextY = y - 1;
      if (nextY < 0 || grid[nextY][x] == null) {
        moveOnCube(x, nextY, grid);
      } else if (Boolean.TRUE.equals(grid[nextY][x])) {
        y = nextY;
      }
    }

    @Override
    protected void moveLeft(Boolean[][] grid) {
      int nextX = x - 1;
      if (nextX < 0 || grid[y][nextX] == null) {
        moveOnCube(nextX, y, grid);
      } else if (Boolean.TRUE.equals(grid[y][nextX])) {
        x = nextX;
      }
    }

    @Override
    protected void moveDown(Boolean[][] grid) {
      int nextY = y + 1;
      if (nextY >= grid.length || grid[nextY][x] == null) {
        moveOnCube(x, nextY, grid);
      } else if (Boolean.TRUE.equals(grid[nextY][x])) {
        y = nextY;
      }
    }

    @Override
    protected void moveRight(Boolean[][] grid) {
      int nextX = x + 1;
      if (nextX >= grid[y].length || grid[y][nextX] == null) {
        moveOnCube(nextX, y, grid);
      } else if (Boolean.TRUE.equals(grid[y][nextX])) {
        x = nextX;
      }
    }

    // TODO: make generic to work with any input size
    private void moveOnCube(int nextX, int nextY, Boolean[][] grid) {
      final int face = getFace(x, y, grid);
      switch (face) {
        case 1 -> moveFrom1(nextX, nextY, grid);
        case 2 -> moveFrom2(nextX, nextY, grid);
        case 3 -> moveFrom3(nextX, grid);
        case 4 -> moveFrom4(nextX, nextY, grid);
        case 5 -> moveFrom5(nextX, nextY, grid);
        case 6 -> moveFrom6(nextX, nextY, grid);
        default -> log.warn("Wrong face: {}", face);
      }
    }

    private void moveFrom6(int nextX, int nextY, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;

      if (nextX < 0) {
        // Moving into face 1
        nextX = y - height / 2;
        nextY = 0;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.DOWN;
        }
      } else if (nextX > 49) {
        // Moving into face 4
        nextX = y - height / 2;
        nextY = 3 * height / 4 - 1;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.UP;
        }
      } else if (nextY >= height) {
        // Moving into face 2
        nextX = x + 2 * width / 3;
        nextY = 0;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.DOWN;
        }
      }
    }

    private void moveFrom5(int nextX, int nextY, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;

      if (nextY < height / 2) {
        // Moving into face 3
        nextX = width / 3;
        nextY = x + height / 4;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.RIGHT;
        }
      } else if (nextX < 0) {
        // Moving into face 1
        nextX = width / 3;
        nextY = 3 * height / 4 - 1 - y;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.RIGHT;
        }
      }
    }

    private void moveFrom4(int nextX, int nextY, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;

      if (nextX >= 2 * width / 3) {
        // Moving into face 2
        nextX = width - 1;
        nextY = 3 * height / 4 - 1 - y;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.LEFT;
        }
      } else if (nextY >= 3 * height / 4) {
        // Moving into face 6
        nextX = width / 3 - 1;
        nextY = x + height / 2;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.LEFT;
        }
      }
    }

    private void moveFrom3(int nextX, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;

      int nextY;
      if (nextX < width / 3) {
        // Moving into face 5
        nextX = y - width / 3;
        nextY = height / 2;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.DOWN;
        }
      } else if (nextX >= 2 * width / 3 - 1) {
        // Moving into face 2
        nextX = y + width / 3;
        nextY = height / 4 - 1;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.UP;
        }
      }
    }

    private void moveFrom2(int nextX, int nextY, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;

      if (nextY < 0) {
        // Moving into face 6
        nextX = x - 2 * width / 3;
        nextY = height - 1;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.UP;
        }
      } else if (nextY >= height / 4) {
        // Moving into face 3
        nextX = 2 * width / 3 - 1;
        nextY = x - height / 4;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.LEFT;
        }
      } else if (nextX >= 3 * height / 4) {
        // Moving into face 4
        nextX = 2 * width / 3 - 1;
        nextY = 3 * height / 4 - 1 - y;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.LEFT;
        }
      }
    }

    private void moveFrom1(int nextX, int nextY, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;

      if (nextY < 0) {
        // Moving towards face 6
        nextY = x + height / 2;
        nextX = 0;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.RIGHT;
        }
      } else if (nextX < width / 3) {
        // Moving into face 5
        nextY = 3 * height / 4 - 1 - y;
        nextX = 0;
        if (Boolean.TRUE.equals(grid[nextY][nextX])) {
          x = nextX;
          y = nextY;
          direction = Direction.RIGHT;
        }
      }
    }

    private int getFace(int x, int y, Boolean[][] grid) {
      final int height = grid.length;
      final int width = grid[0].length;
      if (x < width / 3) {
        if (y < height * 3 / 4) {
          return 5;
        } else {
          return 6;
        }
      } else if (x < width / 3 * 2) {
        if (y < height / 4) {
          return 1;
        } else if (y < height / 2) {
          return 3;
        } else {
          return 4;
        }
      } else {
        return 2;
      }
    }
  }
}
