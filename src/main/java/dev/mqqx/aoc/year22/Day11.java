package dev.mqqx.aoc.year22;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.floor;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.LongFunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day11 {

  record Monkey(
      List<Long> items,
      LongFunction<Long> operation,
      long testDivisibleBy,
      int trueMonkeyId,
      int falseMonkeyId,
      AtomicInteger inspectionCounter) {}

  static long solvePart1(Resource input) {
    return solve(input, 20);
  }

  static long solvePart2(Resource input) {
    return solve(input, 10_000);
  }

  private static long solve(Resource input, int numberOfRounds) {
    final List<Monkey> monkeys = getMonkeys(input);
    final long gcp = monkeys.stream().map(Monkey::testDivisibleBy).reduce(1L, (a, b) -> a * b);

    for (int i = 0; i < numberOfRounds; i++) {
      processMonkeys(numberOfRounds, monkeys, gcp);
    }

    return multiplyHighestInspectionCounters(monkeys);
  }

  private static void processMonkeys(int numberOfRounds, List<Monkey> monkeys, long gcp) {
    for (Monkey monkey : monkeys) {
      if (monkey.items.isEmpty()) {
        continue;
      }

      for (Long item : monkey.items) {
        monkey.inspectionCounter.getAndIncrement();

        long newItemLevel = monkey.operation.apply(item);
        if (numberOfRounds <= 20) {
          newItemLevel = (long) floor((double) newItemLevel / 3);
        }

        if (newItemLevel % monkey.testDivisibleBy == 0) {
          monkeys.get(monkey.trueMonkeyId).items.add(newItemLevel % gcp);
        } else {
          monkeys.get(monkey.falseMonkeyId).items.add(newItemLevel % gcp);
        }
      }
      monkey.items.clear();
    }
  }

  private static long multiplyHighestInspectionCounters(List<Monkey> monkeys) {
    return monkeys.stream()
        .map(Monkey::inspectionCounter)
        .map(AtomicInteger::get)
        .sorted(Comparator.reverseOrder())
        .limit(2)
        .mapToLong(Long::valueOf)
        .reduce(1, (a, b) -> a * b);
  }

  private static List<Monkey> getMonkeys(Resource input) {
    return SplitUtils.lines(input, "\n\n")
        .map(
            monkeyString -> {
              final List<String> monkeyLines = monkeyString.lines().toList();

              List<Long> items =
                  Arrays.stream(monkeyLines.get(1).split(":")[1].trim().split(", "))
                      .map(Long::parseLong)
                      .toList();

              final LongFunction<Long> operation = parseOperation(monkeyLines.get(2));

              final long testDivisibleBy = parseLong(monkeyLines.get(3).substring(21));
              final int trueMonkeyId = parseInt(monkeyLines.get(4).substring(28).trim());
              final int falseMonkeyId = parseInt(monkeyLines.get(5).substring(29).trim());

              return new Monkey(
                  new ArrayList<>(items),
                  operation,
                  testDivisibleBy,
                  trueMonkeyId,
                  falseMonkeyId,
                  new AtomicInteger());
            })
        .toList();
  }

  private static LongFunction<Long> parseOperation(String operationLine) {
    final String[] opString = operationLine.substring(23).split(" ");

    LongFunction<Long> operation;

    if ("*".equals(opString[0])) {
      operation = "old".equals(opString[1]) ? x -> x * x : x -> x * parseLong(opString[1]);
    } else {
      operation = "old".equals(opString[1]) ? x -> x + x : x -> x + parseLong(opString[1]);
    }
    return operation;
  }
}
