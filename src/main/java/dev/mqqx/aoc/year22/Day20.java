package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day20 {

  record Position(int initialIndex, int value) {
  }

  static int solvePart1(Resource input) {
    final List<Integer> originalList = lines(input).mapToInt(Integer::parseInt).boxed().toList();
    final List<Position> rotatingList = new ArrayList<>();

    final HashMap<Integer, Position> numberMap = new HashMap<>();
    for (int i = 0; i < originalList.size(); i++) {
      final int value = originalList.get(i);
      numberMap.put(i, new Position(i, value));
      rotatingList.add(new Position(i, value));
    }

    for (int i = 0; i < originalList.size(); i++) {
      int currentIndex = -1;
      for (int j = 0; j < rotatingList.size(); j++) {
        if (rotatingList.get(j).initialIndex == i) {
          currentIndex = j;
        }
      }

      final Position numberToMove = rotatingList.remove(currentIndex);
      final int newIndex = currentIndex + numberToMove.value;
      final int newIndexToAdd;
      if (newIndex > rotatingList.size() - 1) {
        newIndexToAdd = newIndex % rotatingList.size();
      } else if (newIndex < 0) {
        newIndexToAdd = rotatingList.size() + (newIndex % rotatingList.size());
      } else if (newIndex == 0) {
        newIndexToAdd = rotatingList.size();
      } else {
        newIndexToAdd = newIndex;
      }

      rotatingList.add(newIndexToAdd, numberToMove);
    }

    return getGrooveCoordinatesSum(rotatingList);
  }

  private static int getGrooveCoordinatesSum(List<Position> finalList) {
    int zeroIndex = -1;
    for (int i = 0; i < finalList.size(); i++) {
      if (finalList.get(i).value == 0) {
        zeroIndex = i;
        break;
      }
    }

    // i need to
    final Integer one1000th = finalList.get((1_000 + zeroIndex) % finalList.size()).value;
    final Integer two1000th = finalList.get((2_000 + zeroIndex) % finalList.size()).value;
    final Integer three1000th = finalList.get((3_000 + zeroIndex) % finalList.size()).value;

    return one1000th + two1000th + three1000th;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
  }
}
