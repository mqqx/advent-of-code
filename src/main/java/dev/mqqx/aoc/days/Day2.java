package dev.mqqx.aoc.days;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day2 {
  @SneakyThrows
  static int calculateScore(Resource strategyGuide) {
    List<String> games = splitIntoGroupsOfGames(strategyGuide);

    int points = 0;
    for (String game : games) {
      switch (game) {
        case "A X" -> points += 1 + 3;
        case "A Y" -> points += 2 + 6;
        case "A Z" -> points += 3;
        case "B X" -> points += 1;
        case "B Y" -> points += 2 + 3;
        case "B Z" -> points += 3 + 6;
        case "C X" -> points += 1 + 6;
        case "C Y" -> points += 2;
        case "C Z" -> points += 3 + 3;
      }
    }

    return points;
  }

  @SneakyThrows
  static int calculateScoreWithChangedOutcome(Resource strategyGuide) {
    List<String> games =
        splitIntoGroupsOfGames(strategyGuide).stream()
            .map(
                game ->
                    switch (game) {
                      case "A X" -> "A Z";
                      case "A Y" -> "A X";
                      case "A Z" -> "A Y";
                      case "B X" -> "B X";
                      case "B Y" -> "B Y";
                      case "B Z" -> "B Z";
                      case "C X" -> "C Y";
                      case "C Y" -> "C Z";
                      case "C Z" -> "C X";
                      default -> "";
                    })
            .toList();

    int points = 0;
    for (String game : games) {
      switch (game) {
        case "A X" -> points += 1 + 3;
        case "A Y" -> points += 2 + 6;
        case "A Z" -> points += 3;
        case "B X" -> points += 1;
        case "B Y" -> points += 2 + 3;
        case "B Z" -> points += 3 + 6;
        case "C X" -> points += 1 + 6;
        case "C Y" -> points += 2;
        case "C Z" -> points += 3 + 3;
      }
    }

    return points;
  }

  private static List<String> splitIntoGroupsOfGames(Resource strategyGuide) throws IOException {
    return stream(new String(strategyGuide.getInputStream().readAllBytes()).split("\n")).toList();
  }
}
