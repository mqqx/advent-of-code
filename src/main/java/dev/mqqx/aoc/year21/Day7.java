package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.NumberUtils.median;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.util.Arrays.stream;

import dev.mqqx.aoc.util.NumberUtils;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day7 {

  static int solvePart1(Resource input) {
    return solve(input, a -> a);
  }

  static int solvePart2(Resource input) {
    return solve(input, NumberUtils::sumToN);
  }

  private static int solve(Resource input, IntUnaryOperator intUnaryOperator) {
    final String[] crabPositions = read(input).split(",");
    final int[] nums = stream(crabPositions).mapToInt(Integer::parseInt).toArray();
    final int median = median(nums);
    final AtomicInteger counter = new AtomicInteger(1);
    int minFuel = fuelSum(nums, median, intUnaryOperator);
    int possibleMinFuelUpwards;
    int possibleMinFuelDownwards;

    do {
      possibleMinFuelUpwards = fuelSum(nums, median - counter.get(), intUnaryOperator);
      possibleMinFuelDownwards = fuelSum(nums, median + counter.get(), intUnaryOperator);

      counter.incrementAndGet();
      minFuel =
          IntStream.of(minFuel, possibleMinFuelDownwards, possibleMinFuelUpwards).min().getAsInt();
    } while (minFuel >= possibleMinFuelDownwards || minFuel >= possibleMinFuelUpwards);

    return minFuel;
  }

  private static int fuelSum(int[] nums, int median, IntUnaryOperator intUnaryOperator) {
    return stream(nums)
        .map(position -> position - median)
        .map(Math::abs)
        .map(intUnaryOperator)
        .sum();
  }
}
