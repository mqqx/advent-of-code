package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;

import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day21 {

  static int solvePart1(Resource input) {
    final List<Player> players =
        lines(input)
            .map(line -> line.charAt(line.length() - 1))
            .map(Character::getNumericValue)
            .map(Player::new)
            .toList();

    int dice = 0;
    int rollCounter = 0;
    int stepsToMove;
    boolean isGameRunning = true;
    int loser = -1;
    do {
      for (int i = 0; i < players.size(); i++) {
        stepsToMove = ++dice;
        int dice1 = dice;
        dice = dice == 100 ? 0 : dice;
        stepsToMove += ++dice;
        int dice2 = dice;
        dice = dice == 100 ? 0 : dice;
        stepsToMove += ++dice;
        int dice3 = dice;
        dice = dice == 100 ? 0 : dice;

        rollCounter += 3;

        final Player player = players.get(i);

        final int newPos = (player.getPos() + stepsToMove) % 10;
        player.setPos(newPos == 0 ? 10 : newPos);
        player.setScore(player.getScore() + player.getPos());

        System.out.println(
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

        if (player.getScore() >= 1000) {
          loser = i == 1 ? 0 : 1;
          isGameRunning = false;
          break;
        }
      }

    } while (isGameRunning);

    System.out.println(
        players.get(loser).getScore()
            + " * "
            + rollCounter
            + " = "
            + rollCounter * players.get(loser).getScore());
    return rollCounter * players.get(loser).getScore();
  }

  static int solvePart2(Resource input) {
    final Stream<String> strings = lines(input);

    return 157;
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
  }
}
