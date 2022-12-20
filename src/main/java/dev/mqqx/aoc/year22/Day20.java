package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day20 {

  record Number(int initialIndex, long value) {}

  static long solvePart1(Resource input) {
    final List<Number> listToRotate = initializeList(input);

    rotateList(listToRotate);

    return getGrooveCoordinatesSum(listToRotate);
  }

  private static void rotateList(List<Number> listToRotate) {
    for (int i = 0; i < listToRotate.size(); i++) {
      final int originalNumberIndex = i;
      final int currentNumberIndex =
          range(0, listToRotate.size())
              .filter(j -> listToRotate.get(j).initialIndex == originalNumberIndex)
              .findFirst()
              .orElse(-1);

      // as each remove is followed by an add the size of the list never changes outside each
      // iteration
      @java.lang.SuppressWarnings("squid:S5413")
      final Number numberToMove = listToRotate.remove(currentNumberIndex);
      final int newIndex = calculateNewIndex(listToRotate.size(), currentNumberIndex, numberToMove);

      listToRotate.add(newIndex, numberToMove);
    }
  }

  private static List<Number> initializeList(Resource input) {
    return initializeList(input, 1);
  }

  // list needs to be modifiable, as moved numbers will be removed and added for automated shifting
  @java.lang.SuppressWarnings("squid:S6204")
  private static List<Number> initializeList(Resource input, int decryptionKey) {
    final AtomicInteger index = new AtomicInteger();

    return lines(input)
        .mapToInt(Integer::parseInt)
        .mapToObj(value -> new Number(index.getAndIncrement(), (long) value * decryptionKey))
        .collect(toList());
  }

  private static int calculateNewIndex(
      int listToRotateSize, int currentIndex, Number numberToMove) {
    final long newIndex = currentIndex + numberToMove.value;
    final int newIndexToAdd;
    if (newIndex > listToRotateSize - 1) {
      newIndexToAdd = (int) (newIndex % listToRotateSize);
    } else if (newIndex < 0) {
      newIndexToAdd = listToRotateSize + (int) (newIndex % listToRotateSize);
    } else if (newIndex == 0) {
      newIndexToAdd = listToRotateSize;
    } else {
      newIndexToAdd = (int) newIndex;
    }
    return newIndexToAdd;
  }

  private static long getGrooveCoordinatesSum(List<Number> rotatedList) {
    final int zeroIndex =
        range(0, rotatedList.size())
            .filter(i -> rotatedList.get(i).value == 0)
            .findFirst()
            .orElse(-1);
    return IntStream.of(1_000, 2_000, 3_000)
        .mapToLong(i -> rotatedList.get(((i + zeroIndex) % rotatedList.size())).value)
        .sum();
  }

  static long solvePart2(Resource input) {
    final List<Number> listToRotate = initializeList(input, 811_589_153);

    range(0, 10).forEach(i -> rotateList(listToRotate));

    return getGrooveCoordinatesSum(listToRotate);
  }
}
