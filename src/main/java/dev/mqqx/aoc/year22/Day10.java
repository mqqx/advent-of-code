package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day10 {

  static int solvePart1(Resource input) {
    return cycleCommands(Day10::getSignalStrength, linesList(input)).mapToInt(e -> e).sum();
  }

  static String solvePart2(Resource input) {
    return cycleCommands(Day10::getPixel, linesList(input)).collect(joining());
  }

  private static <T> Stream<T> cycleCommands(
      BiFunction<Integer, Integer, T> operation, List<String> lines) {
    int strength = 1;
    int cycle = 1;
    final Collection<T> result = new ArrayList<>();

    for (String instruction : lines) {
      result.add(operation.apply(cycle, strength));

      if (instruction.startsWith("addx")) {
        cycle++;
        result.add(operation.apply(cycle, strength));
        strength += parseInt(instruction.substring(5));
      }
      cycle++;
    }
    return result.stream();
  }

  private static int getSignalStrength(int cycle, int strength) {
    return (cycle + 20) % 40 == 0 ? cycle * strength : 0;
  }

  private static String getPixel(int cycle, int strength) {
    int cyclePart = (cycle - 1) % 40;
    String pixels = cyclePart == 0 ? "\n" : "";

    if (cyclePart >= strength - 1 && cyclePart <= strength + 1) {
      pixels += "#";
    } else {
      pixels += ".";
    }

    return pixels;
  }
}
