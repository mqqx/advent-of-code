package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;

import dev.mqqx.aoc.util.UnexpectedValueException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.LongStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day10 {

  static long solvePart1(Resource input) {
    return solve(input, true);
  }

  static long solvePart2(Resource input) {
    return solve(input, false);
  }

  private static long solve(Resource input, boolean isPart1) {
    final LongStream solution = lines(input).mapToLong(line -> calculateScore(isPart1, line));

    if (isPart1) {
      return solution.sum();
    } else {
      final long[] scores = solution.sorted().filter(score -> score > 0).toArray();
      return scores[scores.length / 2];
    }
  }

  private static long calculateScore(boolean isPart1, String line) {
    final Deque<Character> expectedClosingCharacters = new ArrayDeque<>();
    Character expectedClosingCharacter = null;
    for (char cur : line.toCharArray()) {
      Integer syntaxErrorScore = checkSyntaxError(isPart1, expectedClosingCharacter, cur);
      if (syntaxErrorScore != null) {
        return syntaxErrorScore;
      }

      expectedClosingCharacter =
          updateExcpectedClosingCharacter(expectedClosingCharacters, expectedClosingCharacter, cur);
    }

    if (isPart1) {
      return 0;
    } else {
      expectedClosingCharacters.addFirst(expectedClosingCharacter);
      return calculateAutocompleteScore(expectedClosingCharacters);
    }
  }

  private static Integer checkSyntaxError(
      boolean isPart1, Character expectedClosingCharacter, char cur) {
    boolean isClosing = cur == ')' || cur == ']' || cur == '}' || cur == '>';

    if (expectedClosingCharacter != null && isClosing && cur != expectedClosingCharacter) {
      if (isPart1) {
        return toSyntaxErrorPoints(cur);
      } else {
        return 0;
      }
    }
    return null;
  }

  private static long calculateAutocompleteScore(Deque<Character> expectedClosingCharacters) {
    long score = 0;
    while (!expectedClosingCharacters.isEmpty()) {
      score = score * 5 + toAutocompletePoints(expectedClosingCharacters.removeFirst());
    }

    return score;
  }

  private static int toAutocompletePoints(char cur) {
    return switch (cur) {
      case ')' -> 1;
      case ']' -> 2;
      case '}' -> 3;
      case '>' -> 4;
      default -> throw new UnexpectedValueException(cur);
    };
  }

  private static int toSyntaxErrorPoints(char cur) {
    return switch (cur) {
      case ')' -> 3;
      case ']' -> 57;
      case '}' -> 1_197;
      case '>' -> 25_137;
      default -> throw new UnexpectedValueException(cur);
    };
  }

  private static Character updateExcpectedClosingCharacter(
      Deque<Character> expectedClosingCharacters, Character expectedClosingCharacter, char cur) {
    boolean isClosing = cur == ')' || cur == ']' || cur == '}' || cur == '>';

    if (isClosing) {
      // there can be more than one top level sequence, therefore we need to check deque size before
      // popping
      expectedClosingCharacter =
          expectedClosingCharacters.isEmpty() ? null : expectedClosingCharacters.pop();
    } else {
      if (expectedClosingCharacter != null) {
        expectedClosingCharacters.push(expectedClosingCharacter);
      }
      expectedClosingCharacter =
          switch (cur) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            case '<' -> '>';
            default -> throw new UnexpectedValueException(cur);
          };
    }
    return expectedClosingCharacter;
  }
}
