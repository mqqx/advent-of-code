package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static dev.mqqx.aoc.util.SplitUtils.linesList;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day2 {

  static int doThing(Resource input) {
    final int horizontalPosition =
        lines(input)
            .filter(command -> command.startsWith("f"))
            .map(command -> command.replace("forward ", ""))
            .mapToInt(Integer::valueOf)
            .sum();

    final int depth =
        lines(input)
            .filter(command -> !command.startsWith("f"))
            .map(command -> command.replace("down ", ""))
            .map(command -> command.replace("up ", "-"))
            .mapToInt(Integer::valueOf)
            .sum();

    return horizontalPosition * depth;
  }

  static int doThingAdvanced(Resource input) {
    final List<String> strings = linesList(input);

    int aim = 0;
    int horizontalPosition = 0;
    int depth = 0;

    for (String command : strings) {
      if (command.startsWith("f")) {
        final int forward = Integer.parseInt(command.split(" ")[1]);
        horizontalPosition += forward;
        depth += forward * aim;
      } else if (command.startsWith("d")) {
        final int down = Integer.parseInt(command.split(" ")[1]);
        aim += down;
      } else if (command.startsWith("u")) {
        final int up = Integer.parseInt(command.split(" ")[1]);
        aim -= up;
      }
    }

    return horizontalPosition * depth;
  }
}
