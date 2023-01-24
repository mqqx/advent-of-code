package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.lines;
import static java.lang.Math.max;

import java.util.List;
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

        log.trace(
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

  static long solvePart2(Resource input) {
    final List<Player> players = parsePlayers(input);
    player1Universes = 0L;
    player2Universes = 0L;

    quantumSplit(1, players.get(0).getPos(), players.get(1).getPos(), 0, 0, true);
    return max(player1Universes, player2Universes);
  }

  private static long player1Universes;
  private static long player2Universes;

  // index 0-2 will be skipped, as the least steps a player can make per round are 3 (1+1+1)
  // the list represents the frequency of the possible sum to n after three D3 rolls
  // for example the most frequent number 6 has 7 possibilities:
  // 1-2-3, 1-3-2, 2-1-3, 2-2-2, 2-3-1, 3-1-2, 3-2-1
  static final int[] frequencyOf3DiceRolls = new int[] {0, 0, 0, 1, 3, 6, 7, 6, 3, 1};

  public static void quantumSplit(
      long currentUniverseCount,
      int player1Pos,
      int player2Pos,
      int player1Score,
      int player2Score,
      boolean isPlayer1Turn) {
    if (player1Score >= 21) {
      player1Universes += currentUniverseCount;
      return;
    }
    if (player2Score >= 21) {
      player2Universes += currentUniverseCount;
      return;
    }

    for (int i = 3; i < 10; i++) {
      final long newUniverseCount = currentUniverseCount * frequencyOf3DiceRolls[i];
      if (isPlayer1Turn) {
        final int newPlayer1Pos = Player.reducePos(player1Pos + i);
        quantumSplit(
            newUniverseCount,
            newPlayer1Pos,
            player2Pos,
            player1Score + newPlayer1Pos,
            player2Score,
            false);
      } else {
        final int newPlayer2Pos = Player.reducePos(player2Pos + i);
        quantumSplit(
            newUniverseCount,
            player1Pos,
            newPlayer2Pos,
            player1Score,
            player2Score + newPlayer2Pos,
            true);
      }
    }
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

    private static int reducePos(int position) {
      final int newPos = position % 10;
      return newPos == 0 ? 10 : newPos;
    }

    private void addPos(int stepsToMove) {
      pos = reducePos(pos + stepsToMove);
    }

    private void move(int stepsToMove) {
      addPos(stepsToMove);
      score += pos;
    }
  }
}
