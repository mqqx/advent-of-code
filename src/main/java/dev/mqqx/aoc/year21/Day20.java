package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.ArrayUtils.deepCopy;
import static dev.mqqx.aoc.util.NumberUtils.isOdd;
import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day20 {

  static int solvePart1(Resource input) {
    return enhanceImageAndCountPixels(input, 2);
  }

  static int solvePart2(Resource input) {
    return enhanceImageAndCountPixels(input, 50);
  }

  private static int enhanceImageAndCountPixels(Resource input, int numberOfEnhancementRounds) {
    final String[] splitInput = read(input).split("\n\n");

    final String imageEnhancementAlgorithm =
        splitInput[0].replace("\n", "").replace('.', '0').replace('#', '1');

    final boolean shouldFlipBorderPixel =
        imageEnhancementAlgorithm.charAt(0) == '1'
            && imageEnhancementAlgorithm.charAt(imageEnhancementAlgorithm.length() - 1) == '0';

    Character[][] image = toGrid(splitInput[1]);
    print(image);

    for (int round = 0; round < numberOfEnhancementRounds; round++) {
      final boolean hasFlippingBits = isOdd(round) && shouldFlipBorderPixel;
      image = addLightPixelBorder(image, hasFlippingBits);
      enhance(image, imageEnhancementAlgorithm, hasFlippingBits);
      print(image);
    }

    return countPixels(image);
  }

  private static void enhance(
      Character[][] image, String imageEnhancementAlgorithm, boolean hasFlippingBits) {
    final Character[][] originalImage = deepCopy(image);

    for (int y = 0; y < originalImage.length; y++) {
      for (int x = 0; x < originalImage[0].length; x++) {
        final int index = getIndexToCheck(hasFlippingBits, originalImage, y, x);
        image[y][x] = imageEnhancementAlgorithm.charAt(index);
      }
    }
  }

  private static int getIndexToCheck(
      boolean hasFlippingBits, Character[][] originalImage, int y, int x) {
    final StringBuilder convertBuilder = new StringBuilder();

    for (int yMarker = -1; yMarker < 2; yMarker++) {
      for (int xMarker = -1; xMarker < 2; xMarker++) {
        final boolean isBorderPixel =
            y + yMarker < 0
                || y + yMarker == originalImage.length
                || x + xMarker < 0
                || x + xMarker == originalImage[0].length;
        if (isBorderPixel) {
          convertBuilder.append(hasFlippingBits ? '1' : '0');
        } else {
          convertBuilder.append(originalImage[y + yMarker][x + xMarker]);
        }
      }
    }

    return parseInt(convertBuilder.toString(), 2);
  }

  private static void print(Character[][] image) {
    if (log.isTraceEnabled()) {
      final StringBuilder board = new StringBuilder();
      for (Character[] chars : image) {
        for (char aChar : chars) {
          board.append(aChar == '0' ? '.' : '#');
        }
        board.append(System.lineSeparator());
      }
      log.trace(valueOf(board));
    }
  }

  private static int countPixels(Character[][] image) {
    int count = 0;
    for (Character[] chars : image) {
      for (char aChar : chars) {
        if (aChar == '1') {
          count++;
        }
      }
    }
    return count;
  }

  private static Character[][] addLightPixelBorder(Character[][] image, boolean hasFlippingBits) {
    final int border = 1;
    final Character[][] grid =
        new Character[image.length + border * 2][image[0].length + border * 2];

    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[0].length; x++) {
        if (y < border || y >= grid.length - border || x < border || x >= grid[0].length - border) {
          grid[y][x] = hasFlippingBits ? '1' : '0';
        } else {
          grid[y][x] = image[y - border][x - border];
        }
      }
    }

    return grid;
  }

  private static Character[][] toGrid(String resource) {
    final List<String> lines = resource.lines().toList();
    final Character[][] grid = new Character[lines.size()][lines.get(0).length()];

    for (int y = 0; y < grid.length; y++) {
      final String line = lines.get(y);
      for (int x = 0; x < grid[0].length; x++) {
        grid[y][x] = line.charAt(x) == '.' ? '0' : '1';
      }
    }

    return grid;
  }
}
