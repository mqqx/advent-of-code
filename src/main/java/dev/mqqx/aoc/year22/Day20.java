package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day20 {

  record Position(int initialIndex, long value) {
  }

  static long solvePart1(Resource input) {
    final List<Integer> originalList = lines(input).mapToInt(Integer::parseInt).boxed().toList();
    final List<Position> rotatingList = initializeRotatingList(originalList);

    rotateList(originalList, rotatingList);

    return getGrooveCoordinatesSum(rotatingList);
  }

  private static void rotateList(List<Integer> originalList, List<Position> rotatingList) {
    for (int i = 0; i < originalList.size(); i++) {
      int currentIndex = -1;
      for (int j = 0; j < rotatingList.size(); j++) {
        if (rotatingList.get(j).initialIndex == i) {
          currentIndex = j;
        }
      }

      final Position numberToMove = rotatingList.remove(currentIndex);
      final int newIndexToAdd = getNewIndexToAdd(rotatingList.size(), currentIndex, numberToMove);

      rotatingList.add(newIndexToAdd, numberToMove);
    }
  }

  private static List<Position> initializeRotatingList(List<Integer> originalList) {
    return initializeRotatingList(originalList, 1);
  }

  private static List<Position> initializeRotatingList(List<Integer> originalList, int decryptionKey) {
    final List<Position> rotatingList = new ArrayList<>();

    for (int i = 0; i < originalList.size(); i++) {
      final int value = originalList.get(i);
      rotatingList.add(new Position(i, (long) value * decryptionKey));
    }
    return rotatingList;
  }

  private static int getNewIndexToAdd(int rotatingListSize, int currentIndex, Position numberToMove) {
    final long newIndex = currentIndex + numberToMove.value;
    final int newIndexToAdd;
    if (newIndex > rotatingListSize - 1) {
      newIndexToAdd = (int) (newIndex % rotatingListSize);
    } else if (newIndex < 0) {
      newIndexToAdd = rotatingListSize + (int) (newIndex % rotatingListSize);
    } else if (newIndex == 0) {
      newIndexToAdd = rotatingListSize;
    } else {
      newIndexToAdd = (int) newIndex;
    }
    return newIndexToAdd;
  }

  private static long getGrooveCoordinatesSum(List<Position> finalList) {
    int zeroIndex = -1;
    for (int i = 0; i < finalList.size(); i++) {
      if (finalList.get(i).value == 0) {
        zeroIndex = i;
        break;
      }
    }

    final long one1000th = finalList.get((1_000 + zeroIndex) % finalList.size()).value;
    final long two1000th = finalList.get((2_000 + zeroIndex) % finalList.size()).value;
    final long three1000th = finalList.get((3_000 + zeroIndex) % finalList.size()).value;

    return one1000th + two1000th + three1000th;
  }

  static long solvePart2(Resource input) {
    final List<Integer> originalList = lines(input).mapToInt(Integer::parseInt).boxed().toList();
    final List<Position> rotatingList = initializeRotatingList(originalList, 811_589_153);

    for (int i = 0; i < 10; i++) {
      rotateList(originalList, rotatingList);
    }

    return getGrooveCoordinatesSum(rotatingList);
  }
}
