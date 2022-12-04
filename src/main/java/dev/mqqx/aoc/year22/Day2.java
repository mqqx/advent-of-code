package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.ElfUtils.splitStringResourceByLineFeed;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day2 {
  private static final String ROCK_ROCK = "A X";
  private static final String ROCK_SCISSORS = "A Z";
  private static final String ROCK_PAPER = "A Y";
  private static final String PAPER_ROCK = "B X";
  private static final String PAPER_PAPER = "B Y";
  private static final String SCISSORS_ROCK = "C X";
  private static final String SCISSORS_PAPER = "C Y";
  private static final String SCISSORS_SCISSORS = "C Z";
  private static final String PAPER_SCISSORS = "B Z";

  static int calculateScore(Resource strategyGuide) {
    List<String> games = splitStringResourceByLineFeed(strategyGuide);

    return calculatePoints(games);
  }

  static int calculateScoreWithChangedOutcome(Resource strategyGuide) {
    List<String> games =
        splitStringResourceByLineFeed(strategyGuide).stream()
            .map(
                game ->
                    switch (game) {
                      case ROCK_ROCK -> ROCK_SCISSORS;
                      case ROCK_PAPER -> ROCK_ROCK;
                      case ROCK_SCISSORS -> ROCK_PAPER;
                      case SCISSORS_ROCK -> SCISSORS_PAPER;
                      case SCISSORS_PAPER -> SCISSORS_SCISSORS;
                      case SCISSORS_SCISSORS -> SCISSORS_ROCK;
                      default -> game;
                    })
            .toList();

    return calculatePoints(games);
  }

  private static int calculatePoints(List<String> games) {
    int points = 0;
    for (String game : games) {
      switch (game) {
        case ROCK_ROCK -> points += 1 + 3;
        case ROCK_PAPER -> points += 2 + 6;
        case ROCK_SCISSORS -> points += 3;
        case PAPER_ROCK -> points += 1;
        case PAPER_PAPER -> points += 2 + 3;
        case PAPER_SCISSORS -> points += 3 + 6;
        case SCISSORS_ROCK -> points += 1 + 6;
        case SCISSORS_PAPER -> points += 2;
        case SCISSORS_SCISSORS -> points += 3 + 3;
        default -> log.warn("Wrong game input: {}", game);
      }
    }
    return points;
  }
}
