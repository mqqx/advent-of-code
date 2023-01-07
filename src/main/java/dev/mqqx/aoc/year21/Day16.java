package dev.mqqx.aoc.year21;

import static com.google.common.collect.Lists.newArrayList;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day16 {
  private static int index;

  static int solvePart1(Resource input) {
    index = 0;
    final String binary = read(input).chars().mapToObj(Day16::hex).collect(joining());

    // ignore zero bits at the end
    // one packet has bits 0-2 (version), 3-5 (type id)
    // type id 4 represents literal value which is encoded in a single bit
    return scanPacketVersions(binary);
  }

  private static int scanPacketVersions(String binary) {
    int version = parse(binary, 3);
    final int typeId = parse(binary, 3);

    final List<Integer> versions = newArrayList(version);

    final boolean isLiteralValuePacket = typeId == 4;
    if (isLiteralValuePacket) {
      boolean isPrefixedByOne;
      do {
        isPrefixedByOne = binary.charAt(index) == '1';
        index += 5;
      } while (isPrefixedByOne);
    } else {
      // is operator packet
      final boolean isCountOfSubPackets = binary.charAt(index) == '1';
      index++;

      if (isCountOfSubPackets) {
        int countOfSubPacketsTo = parse(binary, 11);

        while (versions.size() <= countOfSubPacketsTo) {
          versions.add(scanPacketVersions(binary));
        }

      } else {
        int lengthOfSubPackets = parse(binary, 15);

        final int endOfSubPackets = index + lengthOfSubPackets;
        while (index < endOfSubPackets) {
          versions.add(scanPacketVersions(binary));
        }
      }
    }

    return versions.stream().mapToInt(Integer::intValue).sum();
  }

  private static int parse(String binary, int length) {
    final int parsedInt = binaryToInt(binary.substring(index, index + length));
    index += length;
    return parsedInt;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return -1;
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
