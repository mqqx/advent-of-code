package dev.mqqx.aoc.util;

import static java.util.Arrays.stream;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ElfUtils {
  @SneakyThrows
  public static List<String> splitStringResource(Resource stringResource, String regex) {
    return stream(new String(stringResource.getInputStream().readAllBytes()).split(regex)).toList();
  }

  @SneakyThrows
  public static List<String> splitStringResourceByLineFeed(Resource stringResource) {
    return splitStringResource(stringResource, "\n");
  }
}
