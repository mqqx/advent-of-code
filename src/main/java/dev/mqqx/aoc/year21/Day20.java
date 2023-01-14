package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.read;
import static java.lang.Integer.parseInt;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day20 {

  static int solvePart1(Resource input) {
    final String[] splitInput = read(input).split("\n\n");

    final String imageEnhancementAlgorithm =
        splitInput[0].replace("\n", "").replace('.', '0').replace('#', '1');

    Character[][] image = toGrid(splitInput[1]);
    printImage(image);

    for (int enhanceCount = 0; enhanceCount < 2; enhanceCount++) {
      image = addLightPixelBorder(image);
      //      printImage(image);
      enhance(image, imageEnhancementAlgorithm);
      printImage(image);
    }

    return countPixels(image);
  }

  private static void enhance(Character[][] image, String imageEnhancementAlgorithm) {
    final Character[][] originalImage = deepCopy(image);

    for (int y = 1; y < originalImage.length - 1; y++) {
      for (int x = 1; x < originalImage[0].length - 1; x++) {

        final String toConvert =
            ""
                + originalImage[y - 1][x - 1]
                + originalImage[y - 1][x]
                + originalImage[y - 1][x + 1]
                + originalImage[y][x - 1]
                + originalImage[y][x]
                + originalImage[y][x + 1]
                + originalImage[y + 1][x - 1]
                + originalImage[y + 1][x]
                + originalImage[y + 1][x + 1];

        if (x == 7 && y == 7) {
          System.out.println("gonna check: " + toConvert);
        }

        image[y][x] = imageEnhancementAlgorithm.charAt(parseInt(toConvert, 2));
      }
    }

    printImage(originalImage);
  }

  static <T> T[][] deepCopy(T[][] matrix) {
    return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
  }

  private static void printImage(Character[][] image) {
    final StringBuilder board = new StringBuilder();
    for (Character[] chars : image) {
      for (char aChar : chars) {
        board.append(aChar == '0' ? '.' : '#');
      }
      board.append(System.lineSeparator());
    }
    System.out.println(board);
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

  private static Character[][] addLightPixelBorder(Character[][] image) {
    final Character[][] grid = new Character[image.length + 10][image[0].length + 10];

    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[0].length; x++) {
        if (y < 5 || y >= grid.length - 5 || x < 5 || x >= grid[0].length - 5) {
          grid[y][x] = '0';
        } else {
          grid[y][x] = image[y - 5][x - 5];
        }
      }
    }

    return grid;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return 157;
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
