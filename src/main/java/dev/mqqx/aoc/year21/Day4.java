package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day4 {

  static int solvePart1(Resource input) {
    final List<String> bingoLines = linesList(input, "\n\n");

    return calculateScoreOfFirstWinningBoard(
        getBingoNumbers(bingoLines.get(0)), readBingoBoards(bingoLines));
  }

  private static List<Integer> getBingoNumbers(String bingoLines) {
    return stream(bingoLines.split(",")).mapToInt(Integer::parseInt).boxed().toList();
  }

  private static int calculateScoreOfFirstWinningBoard(
      List<Integer> bingoNumbers, ArrayList<BingoNumber[][]> bingoBoards) {
    for (int bingoNumber : bingoNumbers) {
      for (BingoNumber[][] bingoBoard : bingoBoards) {
        markNumberOnBoard(bingoNumber, bingoBoard);
        if (hasBingo(bingoBoard)) {
          return calculateScore(bingoNumber, bingoBoard);
        }
      }
    }

    return -1;
  }

  private static int calculateScoreOfLastWinningBoard(
      List<Integer> bingoNumbers, ArrayList<BingoNumber[][]> bingoBoards) {
    final ArrayList<BingoNumber[][]> boardsToRemove = new ArrayList<>();
    for (int bingoNumber : bingoNumbers) {
      for (BingoNumber[][] bingoBoard : bingoBoards) {
        markNumberOnBoard(bingoNumber, bingoBoard);
        if (hasBingo(bingoBoard)) {
          boardsToRemove.add(bingoBoard);
          if (bingoBoards.size() == 1) {
            return calculateScore(bingoNumber, bingoBoard);
          }
        }
      }
      bingoBoards.removeAll(boardsToRemove);
    }

    return -1;
  }

  private static ArrayList<BingoNumber[][]> readBingoBoards(List<String> bingoFields) {
    final ArrayList<BingoNumber[][]> bingoBoards = new ArrayList<>();

    for (int i = 1; i < bingoFields.size(); i++) {
      final BingoNumber[][] bingoBoard = new BingoNumber[5][5];
      final List<String> lines = bingoFields.get(i).lines().toList();

      for (int j = 0; j < lines.size(); j++) {
        final String[] numbers = lines.get(j).trim().split("\\s+");
        for (int k = 0; k < numbers.length; k++) {
          bingoBoard[j][k] = new BingoNumber(parseInt(numbers[k]), false);
        }
      }
      bingoBoards.add(bingoBoard);
    }
    return bingoBoards;
  }

  private static int calculateScore(int bingoNumber, BingoNumber[][] bingoBoard) {
    int unmarkedSum = 0;
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (!bingoBoard[i][j].isMarked) {
          unmarkedSum += bingoBoard[i][j].number;
        }
      }
    }
    return unmarkedSum * bingoNumber;
  }

  private static void markNumberOnBoard(int bingoNumber, BingoNumber[][] bingoBoard) {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (bingoBoard[i][j].number == bingoNumber) {
          bingoBoard[i][j] = new BingoNumber(bingoNumber, true);
          return;
        }
      }
    }
  }

  private static boolean hasBingo(BingoNumber[][] bingoBoard) {
    for (int i = 0; i < 5; i++) {
      int horizontalBingo = 0;
      int verticalBingo = 0;
      for (int j = 0; j < 5; j++) {
        if (bingoBoard[i][j].isMarked) {
          horizontalBingo++;
        }
        if (bingoBoard[j][i].isMarked) {
          verticalBingo++;
        }
        if (verticalBingo == 5 || horizontalBingo == 5) {
          return true;
        }
      }
    }
    return false;
  }

  static int solvePart2(Resource input) {
    final List<String> bingoLines = linesList(input, "\n\n");

    return calculateScoreOfLastWinningBoard(
        getBingoNumbers(bingoLines.get(0)), readBingoBoards(bingoLines));
  }

  record BingoNumber(int number, boolean isMarked) {}
}
