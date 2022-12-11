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
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day11 {

  record Monkey(
      List<Long> items,
      Function<Long, Long> operation,
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
    List<Monkey> monkeys = getMonkeys(input);

    final long gcp = monkeys.stream().map(Monkey::testDivisibleBy).reduce(1L, (a, b) -> a * b);

    for (int i = 0; i < numberOfRounds; i++) {

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

    return multiplyHighestInspectionCounters(monkeys);
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
    List<Monkey> monkeys = new ArrayList<>();

    SplitUtils.lines(input, "\n\n")
        .forEach(
            monkeyString -> {
              final List<String> monkeyLines = monkeyString.lines().toList();

              List<Long> items =
                  Arrays.stream(monkeyLines.get(1).substring(18).split(","))
                      .map(String::trim)
                      .map(Long::parseLong)
                      .toList();

              final String[] operationString =
                  monkeyLines
                      .get(2)
                      .substring("  Operation: new = old ".length())
                      .trim()
                      .split(" ");

              Function<Long, Long> operation;

              if ("*".equals(operationString[0])) {
                try {
                  final long number = parseLong(operationString[1]);
                  operation = x -> x * number;
                } catch (NumberFormatException e) {
                  operation = x -> x * x;
                }
              } else {
                try {
                  final long number = parseLong(operationString[1]);
                  operation = x -> x + number;
                } catch (NumberFormatException e) {
                  operation = x -> x + x;
                }
              }

              final long testDivisibleBy =
                  parseLong(monkeyLines.get(3).substring("  Test: divisible by ".length()).trim());
              final int trueMonkeyId =
                  parseInt(
                      monkeyLines
                          .get(4)
                          .substring("    If true: throw to monkey ".length())
                          .trim());
              final int falseMonkeyId =
                  parseInt(
                      monkeyLines
                          .get(5)
                          .substring("    If false: throw to monkey ".length())
                          .trim());

              monkeys.add(
                  new Monkey(
                      new ArrayList<>(items),
                      operation,
                      testDivisibleBy,
                      trueMonkeyId,
                      falseMonkeyId,
                      new AtomicInteger()));
            });
    return monkeys;
  }
}
