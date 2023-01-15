package dev.mqqx.aoc.util;

import static java.util.Arrays.stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ArrayUtils {
  public static <T> T[][] deepCopy(T[][] matrix) {
    return stream(matrix).map(el -> el.clone()).toArray(aVoid -> matrix.clone());
  }
}
