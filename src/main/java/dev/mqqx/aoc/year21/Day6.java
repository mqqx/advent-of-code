package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.util.Arrays.stream;

import com.google.common.util.concurrent.AtomicLongMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day6 {
  static long solvePart1(Resource input) {
    return simulateDays(input, 80);
  }

  static long solvePart2(Resource input) {
    return simulateDays(input, 256);
  }

  private static long simulateDays(Resource input, int daysToSimulate) {
    AtomicLongMap<Integer> fishByDay = AtomicLongMap.create();

    stream(read(input).split(",")).map(Integer::parseInt).forEach(fishByDay::getAndIncrement);

    for (int day = 1; day < daysToSimulate + 1; day++) {
      fishByDay = simulateDay(fishByDay);
      log.debug("After " + day + " days: " + fishByDay.sum());
    }

    return fishByDay.sum();
  }

  private static AtomicLongMap<Integer> simulateDay(AtomicLongMap<Integer> fishByDay) {
    final AtomicLongMap<Integer> fishByDayNext = AtomicLongMap.create();

    for (Integer dayId : fishByDay.asMap().keySet()) {
      final long count = fishByDay.get(dayId);

      if (dayId == 0) {
        fishByDayNext.put(6, count);
        fishByDayNext.put(8, count);
      } else {
        fishByDayNext.getAndAdd(dayId - 1, count);
      }
    }

    return fishByDayNext;
  }
}
