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

    final StringBuilder gammaRateBuilder = new StringBuilder();
    final StringBuilder epsilonRateBuilder = new StringBuilder();
    for (int bitCounter : bitCounters) {
      if (bitCounter >= strings.size() / 2) {
        gammaRateBuilder.append("1");
        epsilonRateBuilder.append("0");
      } else {
        gammaRateBuilder.append("0");
        epsilonRateBuilder.append("1");
      }
    }

    return parseInt(gammaRateBuilder.toString(), 2) * parseInt(epsilonRateBuilder.toString(), 2);
  }

  static int doThingAdvanced(Resource input) {
    int oxygenGeneratorRating = calculateRating(new ArrayList<>(SplitUtils.linesList(input)), true);
    int co2ScrubberRating = calculateRating(new ArrayList<>(SplitUtils.linesList(input)), false);

    return oxygenGeneratorRating * co2ScrubberRating;
  }

  private static int calculateRating(List<String> lines, boolean isMostRelevant) {
    final int[] bitCounters = new int[lines.get(0).length()];
    int currentIndex = 0;
    do {
      for (String currentLine : lines) {
        char[] charArray = currentLine.toCharArray();
        char currentChar = charArray[currentIndex];
        if ('1' == currentChar) {
          bitCounters[currentIndex]++;
        }
      }

      if (currentIndex < bitCounters.length) {
        final int finalCurrentIndex = currentIndex;

        if (isMostRelevant) {
          if (bitCounters[currentIndex] >= (float) lines.size() / 2) {
            lines.removeIf(line -> line.charAt(finalCurrentIndex) == '0');
          } else {
            lines.removeIf(line -> line.charAt(finalCurrentIndex) == '1');
          }
        } else {
          if (bitCounters[currentIndex] >= (float) lines.size() / 2) {
            lines.removeIf(line -> line.charAt(finalCurrentIndex) == '1');
          } else {
            lines.removeIf(line -> line.charAt(finalCurrentIndex) == '0');
          }
        }
      }
      currentIndex++;
    } while (lines.size() > 1);

    return parseInt(lines.get(0), 2);
  }
}
