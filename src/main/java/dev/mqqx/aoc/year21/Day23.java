package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Comparator.comparingInt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day23 {
  private static final int[] COST = new int[] {1, 10, 100, 1_000};

  static int solvePart1(Resource input) {
    final Map<AmphipodBurrow, Integer> energyCosts = moveAmphipods(input, false);
    return energyCosts.get(AmphipodBurrow.END);
  }

  static int solvePart2(Resource input) {
    return findLowestEnergyCost(input, true);
  }

  public static int findLowestEnergyCost(Resource input, boolean isPart2) {
    final Map<AmphipodBurrow, Integer> energyCosts = new HashMap<>();
    final PriorityQueue<AmphipodBurrow> queue = initQueue(input, isPart2, energyCosts);

    while (!queue.isEmpty()) {
      final AmphipodBurrow cur = queue.poll();
      final char[] hallway = cur.hallway;
      final char[][] rooms = cur.rooms;

      // try moving from hallway to burrow
      for (int hallIndex = 0; hallIndex < hallway.length; hallIndex++) {
        final AmphipodDestination amphipodDestination = getAmphipodDestination(hallway, hallIndex);
        if (amphipodDestination == null) {
          continue;
        }
        final int amphipod = amphipodDestination.amphipod;
        // additional check to reduce total size of moves
        if (isRoomEmptyExcept(rooms[amphipod], (char) (amphipod + 'A'))) {
          final int roomDestIndex = firstFreeRoom(rooms[amphipod]);
          final char[] newHallway = copyOf(hallway, hallway.length);
          newHallway[hallIndex] = 0;
          final char[][] newRooms = copy(rooms);
          newRooms[amphipod][roomDestIndex] = (char) (amphipod + 'A');

          final int newCost =
              (abs(amphipodDestination.destIndex - hallIndex) + roomDestIndex + 1) * COST[amphipod];
          final int cost = newCost + energyCosts.get(cur);
          addNextBurrow(energyCosts, queue, cost, newHallway, newRooms);
        }
      }

      // try moving from burrow to hallway
      for (int burrow = 0; burrow < 4; burrow++) {
        for (int burrowStart = 0; burrowStart < 4; burrowStart++) {
          if (shouldSkip(rooms, burrow, burrowStart)) {
            continue;
          }
          final int amphipodStart = burrow * 2 + 2;
          for (int dest = 0; dest < hallway.length; dest++) {
            final char[] newHallway = getNewHall(hallway, amphipodStart, dest);
            if (newHallway.length == 0) {
              continue;
            }
            final int amphipod = rooms[burrow][burrowStart] - 'A';
            // move to hallway
            newHallway[dest] = (char) (amphipod + 'A');
            final char[][] newRooms = copy(rooms);
            // delete in burrow
            newRooms[burrow][burrowStart] = 0;

            final int newCost = (abs(dest - amphipodStart) + burrowStart + 1) * COST[amphipod];
            final int cost = newCost + energyCosts.get(cur);
            addNextBurrow(energyCosts, queue, cost, newHallway, newRooms);
          }
        }
      }
    }

    return energyCosts.get(AmphipodBurrow.END_PART_2);
  }

  private static char[] getNewHall(char[] hallway, int amphipodStart, int dest) {
    if (hallway[dest] != 0
        || dest == 2
        || dest == 4
        || dest == 6
        || dest == 8
        || isBurrowPathBlocked(hallway, amphipodStart, dest)) {
      return new char[0];
    }
    return copyOf(hallway, hallway.length);
  }

  private static boolean shouldSkip(char[][] rooms, int burrow, int burrowStart) {
    // skip if already empty, already correct, or already inaccessible
    // an amphipod in the correct burrow is only "locked" if there are none of the wrong type
    // hiding behind it
    return rooms[burrow][burrowStart] == 0
        || rooms[burrow][burrowStart] == burrow + 'A'
            && isRoomEmptyExcept(rooms[burrow], (char) (burrow + 'A'))
        || burrowStart > 0 && isBurrowPathBlocked(rooms[burrow], burrowStart - 1, 0);
  }

  private static PriorityQueue<AmphipodBurrow> initQueue(
      Resource input, boolean isPart2, Map<AmphipodBurrow, Integer> energyCosts) {
    final char[][] initialRooms = initRooms(input, isPart2);
    final AmphipodBurrow start = new AmphipodBurrow(initialRooms, new char[11]);
    energyCosts.put(start, 0);

    final PriorityQueue<AmphipodBurrow> queue =
        new PriorityQueue<>(comparingInt(o -> energyCosts.getOrDefault(o, 0)));
    queue.add(start);
    return queue;
  }

  private static Map<AmphipodBurrow, Integer> moveAmphipods(Resource input, boolean isPart2) {
    final Map<AmphipodBurrow, Integer> energyCosts = new HashMap<>();
    final PriorityQueue<AmphipodBurrow> queue = initQueue(input, isPart2, energyCosts);

    while (!queue.isEmpty()) {
      final AmphipodBurrow cur = queue.poll();
      final char[][] rooms = moveFromHallwayToBurrow(energyCosts, queue, cur);
      moveFromBurrowToHallway(energyCosts, queue, cur, rooms);
    }
    return energyCosts;
  }

  private static char[][] initRooms(Resource input, boolean isPart2) {
    final List<Character> initialOccupancy =
        read(input).chars().filter(Character::isAlphabetic).mapToObj(c -> (char) c).toList();
    final char[][] initialRooms = new char[4][isPart2 ? 4 : 2];
    final List<Character> extraOccupancy = List.of('D', 'C', 'B', 'A', 'D', 'B', 'A', 'C');

    for (int i = 0; i < initialOccupancy.size() / 2; i++) {
      initialRooms[i][0] = initialOccupancy.get(i);
      if (isPart2) {
        initialRooms[i][1] = extraOccupancy.get(i);
        initialRooms[i][2] = extraOccupancy.get(i + 4);
      }
      initialRooms[i][initialRooms[i].length - 1] = initialOccupancy.get(i + 4);
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
    final char[] hallway = cur.hallway;
    final char[][] rooms = cur.rooms;

    for (int hallIndex = 0; hallIndex < hallway.length; hallIndex++) {
      final AmphipodDestination amphipodDestination = getAmphipodDestination(hallway, hallIndex);
      if (amphipodDestination == null) {
        continue;
      }
      final int amphipod = amphipodDestination.amphipod;
      // additional check to reduce total size of moves
      final boolean isBurrowClear =
          rooms[amphipod][0] == 0
              && (rooms[amphipod][1] == 0 || rooms[amphipod][1] == amphipod + 'A');
      if (isBurrowClear) {
        move(energyCosts, queue, cur, hallIndex, amphipod, amphipodDestination.destIndex);
      }
    }
    return rooms;
  }

  private static AmphipodDestination getAmphipodDestination(char[] hallway, int hallIndex) {
    if (hallway[hallIndex] == 0) {
      return null;
    }
    final int amphipodIndex = hallway[hallIndex] - 'A';
    // the destination index for room entrance of amphipod is always 2 * amphipod + 2
    final int destIndex = amphipodIndex * 2 + 2;

    // offset hallway index to not count current position
    final int hallIndexOffset = hallIndex + (int) signum((double) destIndex - hallIndex);
    if (isBurrowPathBlocked(hallway, hallIndexOffset, destIndex)) {
      return null;
    }
    return new AmphipodDestination(amphipodIndex, destIndex);
  }

  private static void move(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      AmphipodBurrow cur,
      int hallIndex,
      int amphipod,
      int destIndex) {
    final char[] hallway = cur.hallway;
    final char[][] rooms = cur.rooms;

    // move to back if back is clear
    final char[] newHallway = copyOf(hallway, hallway.length);
    final char[][] newRooms = copy(rooms);
    newHallway[hallIndex] = 0;

    final int floor = rooms[amphipod][1] == 0 ? 1 : 0;
    newRooms[amphipod][floor] = (char) (amphipod + 'A');

    final int newCost = (abs(destIndex - hallIndex) + 1 + floor) * COST[amphipod];
    final int cost = newCost + energyCosts.get(cur);

    addNextBurrow(energyCosts, queue, cost, newHallway, newRooms);
  }

  private static void move(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      AmphipodBurrow cur,
      int room,
      int floor) {
    final char[] hallway = cur.hallway;
    final char[][] rooms = cur.rooms;
    final int amphipod = rooms[room][floor] - 'A';
    final int amphipodStart = room * 2 + 2;

    for (int dest = 0; dest < hallway.length; dest++) {
      final char[] newHallway = getNewHall(hallway, amphipodStart, dest);
      if (newHallway.length == 0) {
        continue;
      }
      // move to hallway
      newHallway[dest] = (char) (amphipod + 'A');
      final char[][] newRooms = copy(rooms);
      // delete in room
      newRooms[room][floor] = 0;
      final int newCost = (abs(dest - amphipodStart) + 1 + floor) * COST[amphipod];
      final int cost = newCost + energyCosts.get(cur);

      addNextBurrow(energyCosts, queue, cost, newHallway, newRooms);
    }
  }

  private static void addNextBurrow(
      Map<AmphipodBurrow, Integer> energyCosts,
      PriorityQueue<AmphipodBurrow> queue,
      int cost,
      char[] newHallway,
      char[][] newRooms) {
    final AmphipodBurrow next = new AmphipodBurrow(newRooms, newHallway);

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
  public static boolean isBurrowPathBlocked(char[] hallway, int start, int end) {
    if (start > end) {
      final int temp = start;
      start = end;
      end = temp;
    }
    for (int i = start; i <= end; i++) {
      if (hallway[i] != 0) {
        return true;
      }
    }
    return false;
  }

  // gets first index of room that is available to put amphipod in
  private static int firstFreeRoom(char[] rooms) {
    for (int i = 3; i > -1; i--) {
      if (rooms[i] == 0) {
        return i;
      }
    }
    return -1;
  }

  // returns false if rooms contains any values other than amphipod or zero
  private static boolean isRoomEmptyExcept(char[] rooms, char amphipod) {
    for (char occupyingAmphipod : rooms) {
      if (occupyingAmphipod != 0 && occupyingAmphipod != amphipod) {
        return false;
      }
    }
    return true;
  }

  private record AmphipodDestination(int amphipod, int destIndex) {}

  private record AmphipodBurrow(char[][] rooms, char[] hallway) {

    private static final AmphipodBurrow END =
        new AmphipodBurrow(
            new char[][] {{'A', 'A'}, {'B', 'B'}, {'C', 'C'}, {'D', 'D'}}, new char[11]);
    private static final AmphipodBurrow END_PART_2 =
        new AmphipodBurrow(
            new char[][] {
              {'A', 'A', 'A', 'A'}, {'B', 'B', 'B', 'B'}, {'C', 'C', 'C', 'C'}, {'D', 'D', 'D', 'D'}
            },
            new char[11]);

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
