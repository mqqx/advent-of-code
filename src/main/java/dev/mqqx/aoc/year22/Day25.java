package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Character.getNumericValue;
import static java.lang.Math.pow;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day25 {

  private static final int SNAFU_BASE = 5;

  static String solvePart1(Resource input) {
    final long reduce = lines(input).map(Day25::convertSNAFUtoBase10).reduce(0L, Long::sum);

    return convertBase10ToSNAFU(reduce);
  }

  private static long convertSNAFUtoBase10(String line) {
    final char[] chars = line.toCharArray();
    long sum = 0;
    int exponent = 0;

    for (int i = chars.length - 1; i >= 0; i--) {
      final int currentChar =
          switch (chars[i]) {
            case '-' -> -1;
            case '=' -> -2;
            default -> getNumericValue(chars[i]);
          };

      sum += pow(SNAFU_BASE, exponent++) * currentChar;
    }

    return sum;
  }

  static String convertBase10ToSNAFU(long toConvert) {
    final StringBuilder snafu = new StringBuilder();
    int overflow = 0;

    while (toConvert > 0) {
      long remainder = toConvert % SNAFU_BASE + overflow;

      if (remainder == 3) {
        overflow = 1;
        snafu.insert(0, '=');
      } else if (remainder == 4) {
        overflow = 1;
        snafu.insert(0, '-');
      } else if (remainder == 5) {
        overflow = 1;
        snafu.insert(0, 0);
      } else {
        overflow = 0;
        snafu.insert(0, remainder);
      }

      toConvert /= SNAFU_BASE;
    }

    if (overflow > 0) {
      snafu.insert(0, overflow);
    }

    return snafu.toString();
  }

  static String solvePart2(Resource input) {
    return "สุขสันต์วันหยุด";
  }
}
