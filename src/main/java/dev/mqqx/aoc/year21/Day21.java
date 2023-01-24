package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;

import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class Day21 {

  static int solvePart1(Resource input) {
    final List<Player> players = parsePlayers(input);

    int dice = 0;
    int rollCounter = 0;
    int stepsToMove;
    do {
      for (int i = 0; i < players.size(); i++) {
        rollCounter += 3;
        stepsToMove = ++dice;
        final int dice1 = dice;
        dice %= 100;
        stepsToMove += ++dice;
        final int dice2 = dice;
        dice %= 100;
        stepsToMove += ++dice;
        final int dice3 = dice;
        dice %= 100;

        final Player player = players.get(i);
        player.move(stepsToMove);

        log.debug(
            "Player "
                + (i + 1)
                + " rolls "
                + dice1
                + "+"
                + dice2
                + "+"
                + dice3
                + " and moves to space "
                + player.getPos()
                + " for a total score of "
                + player.getScore());

        if (player.getScore() >= 1_000) {
          final int loserId = i == 1 ? 0 : 1;
          final int loserScore = players.get(loserId).getScore();
          final int score = rollCounter * loserScore;
          log.info(loserScore + " * " + rollCounter + " = " + score);
          return score;
        }
      }
    } while (true);
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
  }

  private static List<Player> parsePlayers(Resource input) {
    return lines(input)
        .map(line -> line.charAt(line.length() - 1))
        .map(Character::getNumericValue)
        .map(Player::new)
        .toList();
  }

  @Data
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class Player {
    int pos;
    int score;

    private Player(int startingPos) {
      this(startingPos, 0);
    }

    private void move(int stepsToMove) {
      final int newPos = (pos + stepsToMove) % 10;
      pos = newPos == 0 ? 10 : newPos;
      score += pos;
    }
  }
}
