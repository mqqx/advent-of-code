package dev.mqqx.aoc.util;

import static java.util.Arrays.stream;

import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SplitUtils {
  @SneakyThrows
  public static String read(Resource stringResource) {
    return new String(stringResource.getInputStream().readAllBytes());
  }

  @SneakyThrows
  public static Stream<String> lines(Resource stringResource, String regex) {
    return stream(read(stringResource).split(regex));
  }

  @SneakyThrows
  public static Stream<Integer> linesToInt(Resource stringResource) {
    return read(stringResource).lines().map(Integer::valueOf);
  }

  @SneakyThrows
  public static List<Integer> linesToIntList(Resource stringResource) {
    return read(stringResource).lines().map(Integer::valueOf).toList();
  }

  /**
   * Splits the given string resource by <code>\n</code> to get a stream of lines
   *
   * @param stringResource the string resource
   * @return the stream of lines
   */
  @SneakyThrows
  public static Stream<String> lines(Resource stringResource) {
    return read(stringResource).lines();
  }

  /**
   * Splits the given string resource by <code>\n</code> to get a list of lines
   *
   * @param stringResource the string resource
   * @return the list of lines
   */
  @SneakyThrows
  public static List<String> linesList(Resource stringResource) {
    return read(stringResource).lines().toList();
  }

  @SneakyThrows
  public static List<String> linesList(Resource stringResource, String regex) {
    return stream(read(stringResource).split(regex)).toList();
  }

  public static Stream<Point> toPoints(Resource input) {
    return lines(input)
        .map(line -> line.split(","))
        .filter(splitLine -> splitLine.length == 2)
        .map(Point::new);
  }

  public static int[][] toGrid(Resource stringResource) {
    final List<String> lines = linesList(stringResource);
    final int[][] grid = new int[lines.size()][lines.get(0).length()];

    for (int y = 0; y < grid.length; y++) {
      final String line = lines.get(y);
      for (int x = 0; x < grid[0].length; x++) {
        grid[y][x] = line.charAt(x) - '0';
      }
    }

    return grid;
  }
}
