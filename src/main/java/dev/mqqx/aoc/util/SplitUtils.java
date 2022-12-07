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
    return stream(new String(stringResource.getInputStream().readAllBytes()).split(regex));
  }

  @SneakyThrows
  public static Stream<Integer> linesToInt(Resource stringResource) {
    return new String(stringResource.getInputStream().readAllBytes()).lines().map(Integer::valueOf);
  }

  @SneakyThrows
  public static List<Integer> linesToIntList(Resource stringResource) {
    return new String(stringResource.getInputStream().readAllBytes())
        .lines()
        .map(Integer::valueOf)
        .toList();
  }

  /**
   * Splits the given string resource by <code>\n</code> to get a stream of lines
   *
   * @param stringResource the string resource
   * @return the stream of lines
   */
  @SneakyThrows
  public static Stream<String> lines(Resource stringResource) {
    return new String(stringResource.getInputStream().readAllBytes()).lines();
  }

  /**
   * Splits the given string resource by <code>\n</code> to get a list of lines
   *
   * @param stringResource the string resource
   * @return the list of lines
   */
  @SneakyThrows
  public static List<String> linesList(Resource stringResource) {
    return new String(stringResource.getInputStream().readAllBytes()).lines().toList();
  }

  @SneakyThrows
  public static List<String> linesList(Resource stringResource, String regex) {
    return stream(new String(stringResource.getInputStream().readAllBytes()).split(regex)).toList();
  }
}
