package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Comparator.comparingInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day23 {
  private static final int[] COST = new int[] {1, 10, 100, 1_000};

  static int solvePart1(Resource input) {
    final char[][] initialRooms = initRooms(input);
    final AmphipodBurrow start = new AmphipodBurrow(initialRooms, new char[11]);
    final Map<AmphipodBurrow, Integer> energyCosts = new HashMap<>();
    energyCosts.put(start, 0);

    final PriorityQueue<AmphipodBurrow> queue =
        new PriorityQueue<>(comparingInt(o -> energyCosts.getOrDefault(o, 0)));
    queue.add(start);

    while (!queue.isEmpty()) {
      final AmphipodBurrow cur = queue.poll();
      final char[][] rooms = moveFromHallwayToBurrow(energyCosts, queue, cur);
      moveFromBurrowToHallway(energyCosts, queue, cur, rooms);
    }

    return energyCosts.get(AmphipodBurrow.END);
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return 157;
  }

  private static char[][] initRooms(Resource input) {
    final List<Character> initialCharOrder =
        read(input).chars().filter(Character::isAlphabetic).mapToObj(c -> (char) c).toList();
    final char[][] initialRooms = new char[4][2];

    for (int i = 0; i < initialCharOrder.size() / 2; i++) {
      initialRooms[i][0] = initialCharOrder.get(i);
      initialRooms[i][1] = initialCharOrder.get(i + 4);
    }
    return initialRooms;
  }

  private static void moveFromBurrowToHallway(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      AmphipodBurrow cur,
      char[][] rooms) {
    for (int room = 0; room < 4; room++) {
      final int floor = rooms[room][0] == 0 ? 1 : 0;
      final int amphipod = room + 'A';
      final boolean isAmphipodNotInRoom = rooms[room][floor] != amphipod;

      if (floor == 1 && isAmphipodNotInRoom && rooms[room][floor] != 0
          || (floor == 0 && (isAmphipodNotInRoom || rooms[room][1] != amphipod))) {
        move(energyCosts, queue, cur, room, floor);
      }
    }
  }

  private static char[][] moveFromHallwayToBurrow(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      AmphipodBurrow cur) {
    final char[] hall = cur.hallway;
    final char[][] rooms = cur.rooms;

    for (int hallIndex = 0; hallIndex < hall.length; hallIndex++) {
      if (hall[hallIndex] == 0) {
        continue;
      }
      final int amphipod = hall[hallIndex] - 'A';
      // the destination index for room entrance of amphipod is always 2 * amphipod + 2
      final int destIndex = amphipod * 2 + 2;
      // offset hall index to not count current position
      final int hallIndexOffset = hallIndex + (int) signum((double) destIndex - hallIndex);
      if (isBurrowPathBlocked(hall, hallIndexOffset, destIndex)) {
        continue;
      }
      // additional check to reduce total size
      final boolean isBurrowClear =
          rooms[amphipod][0] == 0
              && (rooms[amphipod][1] == 0 || rooms[amphipod][1] == amphipod + 'A');
      if (isBurrowClear) {
        move(energyCosts, queue, cur, hallIndex, amphipod, destIndex);
      }
    }
    return rooms;
  }

  private static void move(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      AmphipodBurrow cur,
      int hallIndex,
      int amphipod,
      int destIndex) {
    final char[] hall = cur.hallway;
    final char[][] rooms = cur.rooms;

    // move to back if back is clear
    final char[] newHall = copyOf(hall, hall.length);
    final char[][] newRooms = copy(rooms);
    newHall[hallIndex] = 0;

    final int floor = rooms[amphipod][1] == 0 ? 1 : 0;
    newRooms[amphipod][floor] = (char) (amphipod + 'A');

    final int newCost = (abs(destIndex - hallIndex) + 1 + floor) * COST[amphipod];
    final int cost = newCost + energyCosts.get(cur);

    addNextBurrow(energyCosts, queue, cost, newHall, newRooms);
  }

  private static void move(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      AmphipodBurrow cur,
      int room,
      int floor) {
    final char[] hall = cur.hallway;
    final char[][] rooms = cur.rooms;
    final int amphipod = rooms[room][floor] - 'A';
    final int amphipodStart = room * 2 + 2;

    for (int dest = 0; dest < hall.length; dest++) {
      if (hall[dest] != 0
          || dest == 2
          || dest == 4
          || dest == 6
          || dest == 8
          || isBurrowPathBlocked(hall, amphipodStart, dest)) {
        continue;
      }
      final char[] newHall = copyOf(hall, hall.length);
      // move to hall
      newHall[dest] = (char) (amphipod + 'A');
      final char[][] newRooms = copy(rooms);
      // delete in room
      newRooms[room][floor] = 0;
      final int newCost = (abs(dest - amphipodStart) + 1 + floor) * COST[amphipod];
      final int cost = newCost + energyCosts.get(cur);

      addNextBurrow(energyCosts, queue, cost, newHall, newRooms);
    }
  }

  private static void addNextBurrow(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      int cost,
      char[] newHall,
      char[][] newRooms) {
    final AmphipodBurrow next = new AmphipodBurrow(newRooms, newHall);

    if (cost < energyCosts.getOrDefault(next, Integer.MAX_VALUE)) {
      energyCosts.put(next, cost);
      queue.add(next);
    }
  }

  private static char[][] copy(char[][] rooms) {
    char[][] newRooms = new char[4][];
    for (int i = 0; i < 4; i++) {
      newRooms[i] = copyOf(rooms[i], rooms[i].length);
    }
    return newRooms;
  }

  // checks if hallway is blocked from start to end (inclusive)
  public static boolean isBurrowPathBlocked(char[] hall, int start, int end) {
    if (start > end) {
      int temp = start;
      start = end;
      end = temp;
    }
    for (int i = start; i <= end; i++) {
      if (hall[i] != 0) {
        return true;
      }
    }
    return false;
  }

  private record AmphipodBurrow(char[][] rooms, char[] hallway) {

    private static final AmphipodBurrow END =
        new AmphipodBurrow(
            new char[][] {{'A', 'A'}, {'B', 'B'}, {'C', 'C'}, {'D', 'D'}}, new char[11]);

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      AmphipodBurrow that = (AmphipodBurrow) o;
      return deepEquals(rooms, that.rooms) && Arrays.equals(hallway, that.hallway);
    }

    @Override
    public int hashCode() {
      int result = deepHashCode(rooms);
      result = 31 * result + Arrays.hashCode(hallway);
      return result;
    }

    @Override
    public String toString() {
      return "AmphipodBurrow{"
          + "rooms="
          + Arrays.deepToString(rooms)
          + ", hallway="
          + Arrays.toString(hallway)
          + '}';
    }
  }
}
