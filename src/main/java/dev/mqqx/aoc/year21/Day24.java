package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day24 {
  private static final int INSTRUCTIONS_PER_DIGIT = 18;
  private static final int FIRST_IMPORTANT_INSTRUCTION_OP = 5;
  private static final int SECOND_IMPORTANT_INSTRUCTION_OP = 15;

  private record Digit(int index, int x, int y) {}

  static long solvePart1(Resource input) {
    return solve(input, false);
  }

  static long solvePart2(Resource input) {
    return solve(input, true);
  }

  private static long solve(Resource input, boolean isPart2) {
    final List<Digit> modelNumberDigits = parseModelNumberDigits(input);
    final int[] modelNumber = calcModelNumber(modelNumberDigits, isPart2);
    return toLong(modelNumber);
  }

  private static int[] calcModelNumber(List<Digit> modelNumberDigits, boolean isPart2) {
    // each set does one of two actions:
    // sets z to 26 * z + input + y if x >= 10
    // sets z to z/26 if x < 10
    // as multiplication and division on z are balanced, we only care about the input + y portion
    // all inputs are 1-9, therefore no single input can break this balance
    // keeping digits on the stack, we can decrease the last number if the current is not fitting
    final int[] modelNumber = new int[modelNumberDigits.size()];
    final LinkedList<Digit> stack = new LinkedList<>();
    for (Digit current : modelNumberDigits) {
      if (current.x() >= 10) {
        stack.push(current);
      } else {
        final Digit last = stack.pop();
        final int i = last.y() + current.x();
        if (i >= 0) {
          if (isPart2) {
            modelNumber[current.index()] = 1 + i;
            modelNumber[last.index()] = 1;
          } else {
            modelNumber[current.index()] = 9;
            modelNumber[last.index()] = 9 - i;
          }
        } else {
          if (isPart2) {
            modelNumber[current.index()] = 1;
            modelNumber[last.index()] = 1 - i;
          } else {
            modelNumber[current.index()] = 9 + i;
            modelNumber[last.index()] = 9;
          }
        }
      }
    }
    return modelNumber;
  }

  private static long toLong(int[] modelNumber) {
    return parseLong(stream(modelNumber).mapToObj(String::valueOf).collect(joining()));
  }

  private static List<Digit> parseModelNumberDigits(Resource input) {
    final List<String> lines = linesList(input);
    final List<Digit> modelNumberDigits = new ArrayList<>();

    // the program consists of 14 digits which are represented by sets of 18 instructions
    // examining, comparing and reducing these instruction patterns, only two become important
    // therefore we save these numbers together with their instruction set index
    for (int index = 0, i = FIRST_IMPORTANT_INSTRUCTION_OP, j = SECOND_IMPORTANT_INSTRUCTION_OP;
        i < lines.size() && j < lines.size();
        index++, i += INSTRUCTIONS_PER_DIGIT, j += INSTRUCTIONS_PER_DIGIT) {
      int x = parseInt(lines.get(i).split(" ")[2]);
      int y = parseInt(lines.get(j).split(" ")[2]);
      modelNumberDigits.add(new Digit(index, x, y));
    }
    return modelNumberDigits;
  }
}
