package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day8 {

  static int doThing(Resource input) {
    final Integer[][] forest = readForest(linesList(input));
    final int additionalVisibleTrees = calculateAdditionalVisibleTrees(forest);
    final int sumOfAllOuterTrees = (forest.length - 1) * 2 + (forest[0].length - 1) * 2;

    return sumOfAllOuterTrees + additionalVisibleTrees;
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
        boolean isVisible = checkVisibility(forest, i, j);

        if (isVisible) {
          additionalVisibleTrees++;
        }
      }
    }
    return additionalVisibleTrees;
  }

  private static boolean checkVisibility(Integer[][] forest, int i, int j) {
    return isVisibleLeft(forest[i], j)
        || isVisibleRight(forest[i], j)
        || isVisibleTop(forest, i, j)
        || isVisibleBottom(forest, i, j);
  }

  private static boolean isVisibleBottom(Integer[][] forest, int i, int j) {
    final Integer currentTree = forest[i][j];
    boolean isVisibleBottom = true;
    for (int k = i + 1; k < forest.length; k++) {
      final Integer treeToCompare = forest[k][j];
      if (treeToCompare >= currentTree) {
        isVisibleBottom = false;
        break;
      }
    }
    return isVisibleBottom;
  }

  private static boolean isVisibleTop(Integer[][] forest, int i, int j) {
    final Integer currentTree = forest[i][j];
    boolean isVisibleTop = true;
    for (int k = i - 1; k > -1; k--) {
      final Integer treeToCompare = forest[k][j];
      if (treeToCompare >= currentTree) {
        isVisibleTop = false;
        break;
      }
    }
    return isVisibleTop;
  }

  private static boolean isVisibleRight(Integer[] forest, int j) {
    final Integer currentTree = forest[j];
    boolean isVisibleRight = true;
    for (int k = j + 1; k < forest.length; k++) {
      final Integer treeToCompare = forest[k];
      if (treeToCompare >= currentTree) {
        isVisibleRight = false;
        break;
      }
    }
    return isVisibleRight;
  }

  private static boolean isVisibleLeft(Integer[] forest, int j) {
    final Integer currentTree = forest[j];
    boolean isVisibleLeft = true;
    for (int k = j - 1; k > -1; k--) {
      final Integer treeToCompare = forest[k];
      if (treeToCompare >= currentTree) {
        isVisibleLeft = false;
        break;
      }
    }
    return isVisibleLeft;
  }

  static int doThingAdvanced(Resource input) {
    final Integer[][] forest = readForest(linesList(input));

    return calculateScenicScore(forest);
  }

  private static int calculateScenicScore(Integer[][] forest) {
    int highestScore = 0;

    for (int i = 1; i < forest.length - 1; i++) {
      for (int j = 1; j < forest[0].length - 1; j++) {

        // if is visible increase addition
        final Integer currentTree = forest[i][j];

        // search in every direction
        int score = calculateScenicScore(forest, i, j, currentTree);

        if (score > highestScore) {
          highestScore = score;
        }
      }
    }
    return highestScore;
  }

  private static int calculateScenicScore(Integer[][] forest, int i, int j, Integer currentTree) {
    boolean isVisibleLeft = true;
    boolean isVisibleRight = true;
    boolean isVisibleTop = true;
    boolean isVisibleBottom = true;

    int leftCounter = 0;
    int rightCounter = 0;
    int topCounter = 0;
    int botCounter = 0;

    // search left
    for (int k = j - 1; k > -1; k--) {
      final Integer treeToCompare = forest[i][k];
      leftCounter++;
      if (treeToCompare >= currentTree) {
        isVisibleLeft = false;
        break;
      }
    }

    // search right
    for (int k = j + 1; k < forest[0].length; k++) {
      final Integer treeToCompare = forest[i][k];
      rightCounter++;
      if (treeToCompare >= currentTree) {
        isVisibleRight = false;
        break;
      }
    }

    // search up
    for (int k = i - 1; k > -1; k--) {
      final Integer treeToCompare = forest[k][j];
      topCounter++;
      if (treeToCompare >= currentTree) {
        isVisibleTop = false;
        break;
      }
    }
    // search down
    for (int k = i + 1; k < forest.length; k++) {
      final Integer treeToCompare = forest[k][j];
      botCounter++;
      if (treeToCompare >= currentTree) {
        isVisibleBottom = false;
        break;
      }
    }
    if (isVisibleLeft || isVisibleRight || isVisibleTop || isVisibleBottom) {
      return leftCounter * rightCounter * topCounter * botCounter;
    }
    return -1;
  }
}
