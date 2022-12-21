package dev.mqqx.aoc.year22;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day21 {

  static long solvePart1(Resource input) {
    String monkeyNumbers = SplitUtils.read(input);

    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    long result = 0;
    ExpressionParser parser = new SpelExpressionParser();

    boolean rootNotFound = true;
    while (rootNotFound) {
      List<String> list =
          monkeyNumbers.lines().filter(line -> !line.isBlank()).collect(Collectors.toList());
      for (int i = list.size() - 1; i > -1; i--) {
        final String line = list.get(i);
        final String[] splitLine = line.split(": ");
        try {
          final String numberOrExpression = splitLine[1];

          final String[] splitNumberOrExpression = numberOrExpression.split(" ");

          if (splitNumberOrExpression.length == 1) {
            result = Long.parseLong(numberOrExpression);

          } else {
            long part1 = Long.parseLong(splitNumberOrExpression[0]);
            long part2 = Long.parseLong(splitNumberOrExpression[2]);

            result =
                switch (splitNumberOrExpression[1]) {
                  case "+" -> part1 + part2;
                  case "-" -> part1 - part2;
                  case "*" -> part1 * part2;
                  case "/" -> part1 / part2;
                  default -> -1;
                };
          }

          //          result = parser.parseExpression(numberOrExpression).getValue(Long.class);

          if ("root".equals(splitLine[0])) {
            rootNotFound = false;
            break;
          }
          //            System.out.println("Removing: " + line + " - lines left: " + list.size());
          list.remove(i);
          monkeyNumbers =
              monkeyNumbers.replaceAll(line, "").replaceAll(splitLine[0], String.valueOf(result));
        } catch (NumberFormatException ignored) {
        }
      }
    }

    return result;
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return 157;
  }
}
