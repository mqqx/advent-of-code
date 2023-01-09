package dev.mqqx.aoc.year21;

import static com.google.common.collect.Lists.newArrayList;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.joining;

import dev.mqqx.aoc.util.UnexpectedValueException;
import java.util.List;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day16 {
  private static int index;
  private static String binary;

  static int solvePart1(Resource input) {
    initBinary(input);
    return scanPacketVersions();
  }

  static long solvePart2(Resource input) {
    initBinary(input);
    return parseAndEvaluateExpression();
  }

  private static void initBinary(Resource input) {
    index = 0;
    binary = read(input).chars().mapToObj(Day16::hex).collect(joining());
  }

  private static int scanPacketVersions() {
    final int version = parseIntFromBinary(binary, 3);
    final int typeId = parseIntFromBinary(binary, 3);
    final List<Number> versions = newArrayList();

    final boolean isLiteralValuePacket = typeId == 4;
    if (isLiteralValuePacket) {
      boolean isPrefixedByOne;
      do {
        isPrefixedByOne = binary.charAt(index) == '1';
        index += 5;
      } while (isPrefixedByOne);
    } else {
      parseSubPackets(versions, Day16::scanPacketVersions);
    }

    versions.add(version);
    return versions.stream().mapToInt(Number::intValue).sum();
  }

  private static long parseAndEvaluateExpression() {
    // skip version as it's not needed for part 2
    index += 3;
    final int typeId = parseIntFromBinary(binary, 3);
    final List<Number> results = newArrayList();

    final boolean isLiteralValuePacket = typeId == 4;
    if (isLiteralValuePacket) {
      final StringBuilder literalValueBuilder = new StringBuilder();

      while (binary.charAt(index) == '1') {
        parseAndAddLiteralValue(literalValueBuilder);
      }

      parseAndAddLiteralValue(literalValueBuilder);
      results.add(parseLong(literalValueBuilder.toString(), 2));
    } else {
      parseSubPackets(results, Day16::parseAndEvaluateExpression);
    }

    return evaluateExpression(typeId, results);
  }

  private static void parseAndAddLiteralValue(StringBuilder literalValue) {
    final String parsedString = binary.substring(index + 1, index + 5);
    index += 5;
    literalValue.append(parsedString);
  }

  private static long evaluateExpression(int typeId, List<Number> results) {
    return switch (typeId) {
      case 0 -> results.stream().mapToLong(Number::longValue).sum();
      case 1 -> results.stream().mapToLong(Number::longValue).reduce(1L, (a, b) -> a * b);
      case 2 -> results.stream().mapToLong(Number::longValue).min().orElseThrow();
      case 3 -> results.stream().mapToLong(Number::longValue).max().orElseThrow();
      case 4 -> results.get(0).longValue();
      case 5 -> results.get(0).longValue() > results.get(1).longValue() ? 1L : 0L;
      case 6 -> results.get(0).longValue() < results.get(1).longValue() ? 1L : 0L;
      case 7 -> results.get(0).longValue() == results.get(1).longValue() ? 1L : 0L;
      default -> throw new UnexpectedValueException(typeId);
    };
  }

  private static void parseSubPackets(List<Number> results, Supplier<Number> solution) {
    // is operator packet
    final boolean isCountOfSubPackets = binary.charAt(index) == '1';
    index++;

    if (isCountOfSubPackets) {
      int countOfSubPacketsTo = parseIntFromBinary(binary, 11);

      while (results.size() < countOfSubPacketsTo) {
        results.add(solution.get());
      }
    } else {
      int lengthOfSubPackets = parseIntFromBinary(binary, 15);

      final int endOfSubPackets = index + lengthOfSubPackets;
      while (index < endOfSubPackets) {
        results.add(solution.get());
      }
    }
  }

  private static int parseIntFromBinary(String binary, int length) {
    final int parsedInt = binaryToInt(binary.substring(index, index + length));
    index += length;
    return parsedInt;
  }

  private static String hex(int c) {
    return switch (c) {
      case '0' -> "0000";
      case '1' -> "0001";
      case '2' -> "0010";
      case '3' -> "0011";
      case '4' -> "0100";
      case '5' -> "0101";
      case '6' -> "0110";
      case '7' -> "0111";
      case '8' -> "1000";
      case '9' -> "1001";
      case 'A' -> "1010";
      case 'B' -> "1011";
      case 'C' -> "1100";
      case 'D' -> "1101";
      case 'E' -> "1110";
      case 'F' -> "1111";
      default -> null;
    };
  }

  private static int binaryToInt(String binary) {
    return parseInt(binary, 2);
  }
}
