package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.read;

import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day6 {

  static int doThing(Resource input, int distinctChars) {
    final String inputString = read(input);
    return IntStream.range(distinctChars - 1, inputString.length())
            .filter(
                i ->
                    inputString.substring(i - distinctChars + 1, i + 1).chars().distinct().count()
                        == distinctChars)
            .findAny()
            .orElse(-1)
        + 1;
  }
}
