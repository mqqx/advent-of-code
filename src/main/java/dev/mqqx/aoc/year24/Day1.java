package dev.mqqx.aoc.year24;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import org.springframework.core.io.Resource;

public class Day1 {

  static int solvePart1(Resource example) {
    final Input input = getInput(example);

    int sum = 0;
    for (int i = 0; i < input.left().size(); i++) {
      sum += Math.abs(input.left().get(i) - input.right().get(i));
    }

    return sum;
  }

  static int solvePart2(Resource example) {
    final Input input = getInput(example);

    int sum = 0;
    for (Integer leftValue : input.left()) {
      int hit =
          (int) input.right().stream().filter(rightValue -> rightValue.equals(leftValue)).count();
      sum += hit * leftValue;
    }
    return sum;
  }

  private static Input getInput(Resource example) {
    ArrayList<Integer> left = new ArrayList<>();
    ArrayList<Integer> right = new ArrayList<>();
    SplitUtils.lines(example)
        .map(line -> line.split(" {3}"))
        .forEach(
            lineParts -> {
              left.add(Integer.parseInt(lineParts[0]));
              right.add(Integer.parseInt(lineParts[1]));
            });

    left.sort(Integer::compareTo);
    right.sort(Integer::compareTo);
    return new Input(left, right);
  }

  private record Input(ArrayList<Integer> left, ArrayList<Integer> right) {}
}
