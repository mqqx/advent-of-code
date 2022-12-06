package dev.mqqx.aoc.year21;

import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day3 {

  static int doThing(Resource input) {
    final List<String> strings = SplitUtils.lines(input).toList();
    final int[] bitCounters = countBits(strings);
    final String gammaRate = calculateGammaRate(strings, bitCounters);
    final String epsilonRate = gammaRate.replace('0', '2').replace('1', '0').replace('2', '1');

    return bitStringToInt(gammaRate) * bitStringToInt(epsilonRate);
  }

  static int doThingAdvanced(Resource input) {
    int oxygenGeneratorRating = calculateRating(new ArrayList<>(SplitUtils.linesList(input)), true);
    int co2ScrubberRating = calculateRating(new ArrayList<>(SplitUtils.linesList(input)), false);

    return oxygenGeneratorRating * co2ScrubberRating;
  }

  private static int bitStringToInt(String gammaRate) {
    return parseInt(gammaRate, 2);
  }

  private static String calculateGammaRate(List<String> strings, int[] bitCounters) {
    final StringBuilder gammaRateBuilder = new StringBuilder();
    for (int bitCounter : bitCounters) {
      if (bitCounter >= strings.size() / 2) {
        gammaRateBuilder.append("1");
      } else {
        gammaRateBuilder.append("0");
      }
    }

    return gammaRateBuilder.toString();
  }

  private static int[] countBits(List<String> strings) {
    final int[] bitCounters = new int[strings.get(0).length()];

    for (String currentLine : strings) {
      char[] charArray = currentLine.toCharArray();
      for (int i = 0; i < charArray.length; i++) {
        char currentChar = charArray[i];
        if ('1' == currentChar) {
          bitCounters[i]++;
        }
      }
    }
    return bitCounters;
  }

  private static int calculateRating(List<String> lines, boolean isMostRelevant) {
    final int[] bitCounters = new int[lines.get(0).length()];
    int currentIndex = 0;
    do {
      bitCounters[currentIndex] = countCurrentBits(lines, currentIndex);

      final boolean hasMostCommonBitOne = bitCounters[currentIndex] >= (float) lines.size() / 2;
      final int finalCurrentIndex = currentIndex;

      if (isMostRelevant ^ hasMostCommonBitOne) {
        lines.removeIf(line -> line.charAt(finalCurrentIndex) == '0');
      } else {
        lines.removeIf(line -> line.charAt(finalCurrentIndex) == '1');
      }
      currentIndex++;
    } while (lines.size() > 1);

    return bitStringToInt(lines.get(0));
  }

  private static int countCurrentBits(List<String> lines, int currentIndex) {
    int bitCounter = 0;
    for (String currentLine : lines) {
      char[] charArray = currentLine.toCharArray();
      char currentChar = charArray[currentIndex];
      if ('1' == currentChar) {
        bitCounter++;
      }
    }
    return bitCounter;
  }
}
