package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day8 {

  record VisibilityScore(boolean isVisible, int score) {}

  static int solvePart1(Resource input) {
    final Integer[][] forest = readForest(linesList(input));
    final int additionalVisibleTrees = calculateAdditionalVisibleTrees(forest);
    final int sumOfAllOuterTrees = 4 * forest.length - 4;

    return sumOfAllOuterTrees + additionalVisibleTrees;
  }

  static int solvePart2(Resource input) {
    final Integer[][] forest = readForest(linesList(input));

    return calculateScenicScore(forest);
  }

  private static Integer[][] readForest(List<String> lines) {
    final Integer[][] forest = new Integer[lines.size()][lines.get(0).length()];

    for (int i = 0; i < lines.size(); i++) {
      final char[] trees = lines.get(i).toCharArray();
      for (int j = 0; j < trees.length; j++) {
        forest[i][j] = parseInt(Character.toString(trees[j]));
      }
    }
    return forest;
  }

  private static int calculateAdditionalVisibleTrees(Integer[][] forest) {
    int additionalVisibleTrees = 0;

    for (int i = 1; i < forest.length - 1; i++) {
      for (int j = 1; j < forest[0].length - 1; j++) {
        boolean isVisible =
            calculateLeftVisibilityScore(forest[i], j).isVisible
                || calculateRightVisibilityScore(forest[i], j).isVisible
                || calculateTopVisibilityScore(forest, i, j).isVisible
                || calculateBottomVisibilityScore(forest, i, j).isVisible;

        if (isVisible) {
          additionalVisibleTrees++;
        }
      }
    }
    return additionalVisibleTrees;
  }

  private static int calculateScenicScore(Integer[][] forest) {
    int highestScore = 0;

    for (int i = 1; i < forest.length - 1; i++) {
      for (int j = 1; j < forest[0].length - 1; j++) {
        int score = calculateScenicScore(forest, i, j);

        if (score > highestScore) {
          highestScore = score;
        }
      }
    }
    return highestScore;
  }

  private static int calculateScenicScore(Integer[][] forest, int i, int j) {
    final VisibilityScore leftScore = calculateLeftVisibilityScore(forest[i], j);
    final VisibilityScore rightScore = calculateRightVisibilityScore(forest[i], j);
    final VisibilityScore topScore = calculateTopVisibilityScore(forest, i, j);
    final VisibilityScore bottomScore = calculateBottomVisibilityScore(forest, i, j);

    if (leftScore.isVisible
        || rightScore.isVisible
        || topScore.isVisible
        || bottomScore.isVisible) {
      return leftScore.score * rightScore.score * topScore.score * bottomScore.score;
    }
    return -1;
  }

  private static VisibilityScore calculateBottomVisibilityScore(Integer[][] forest, int i, int j) {
    int counter = 0;

    boolean isVisibleBottom = true;
    for (int k = i + 1; k < forest.length; k++) {
      final Integer treeToCompare = forest[k][j];
      counter++;
      if (treeToCompare >= forest[i][j]) {
        isVisibleBottom = false;
        break;
      }
    }
    return new VisibilityScore(isVisibleBottom, counter);
  }

  private static VisibilityScore calculateTopVisibilityScore(Integer[][] forest, int i, int j) {
    int counter = 0;

    boolean isVisibleTop = true;
    for (int k = i - 1; k > -1; k--) {
      final Integer treeToCompare = forest[k][j];
      counter++;
      if (treeToCompare >= forest[i][j]) {
        isVisibleTop = false;
        break;
      }
    }
    return new VisibilityScore(isVisibleTop, counter);
  }

  private static VisibilityScore calculateLeftVisibilityScore(Integer[] forest, int j) {
    int counter = 0;

    boolean isVisibleLeft = true;
    for (int k = j - 1; k > -1; k--) {
      final Integer treeToCompare = forest[k];
      counter++;
      if (treeToCompare >= forest[j]) {
        isVisibleLeft = false;
        break;
      }
    }
    return new VisibilityScore(isVisibleLeft, counter);
  }

  private static VisibilityScore calculateRightVisibilityScore(Integer[] forest, int j) {
    int counter = 0;
    boolean isVisibleRight = true;

    for (int k = j + 1; k < forest.length; k++) {
      final Integer treeToCompare = forest[k];
      counter++;
      if (treeToCompare >= forest[j]) {
        isVisibleRight = false;
        break;
      }
    }
    return new VisibilityScore(isVisibleRight, counter);
  }
}
