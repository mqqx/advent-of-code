package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day10 {

  static int solvePart1(Resource input) {
    final AtomicInteger cycle = new AtomicInteger();
    final AtomicInteger strength = new AtomicInteger(1);

    return lines(input)
        .map(
            instruction -> {
              final String[] splitI = instruction.split(" ");
              final String command = splitI[0];
              if ("noop".equals(command)) {
                return getSignalStrength(cycle, strength);
              } else {
                final int sum =
                    getSignalStrength(cycle, strength) + getSignalStrength(cycle, strength);
                strength.addAndGet(parseInt(splitI[1]));
                return sum;
              }
            })
        .mapToInt(e -> e)
        .sum();
  }

  static String solvePart2(Resource input) {
    final AtomicInteger cycle = new AtomicInteger(0);
    final AtomicInteger strength = new AtomicInteger(1);

    return lines(input)
        .map(
            instruction -> {
              final String[] splitI = instruction.split(" ");
              final String command = splitI[0];
              String pixel = getPixel(cycle, strength);

              if ("addx".equals(command)) {
                cycle.getAndIncrement();
                pixel += getPixel(cycle, strength);
                strength.addAndGet(parseInt(splitI[1]));
              }
              cycle.getAndIncrement();
              return pixel;
            })
        .collect(joining());
  }

  private static int getSignalStrength(AtomicInteger cycle, AtomicInteger strength) {
    cycle.getAndIncrement();
    return (cycle.get() + 20) % 40 == 0 ? cycle.get() * strength.get() : 0;
  }

  private static String getPixel(AtomicInteger cycle, AtomicInteger strength) {
    int cyclePart = cycle.get() % 40;
    String pixels = "";

    if (cycle.get() != 0 && cyclePart == 0) {
      pixels += "\n";
    }

    if (cyclePart >= strength.get() - 1 && cyclePart <= strength.get() + 1) {
      pixels += "#";
    } else {
      pixels += ".";
    }

    return pixels;
  }
}
