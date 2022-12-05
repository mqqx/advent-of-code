package dev.mqqx.aoc.year22;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day5 {
  private static int commandStartIndex = 0;

  static String doThing(Resource input) {
    return getFinalTopContainers(input, true);
  }

  private static String getFinalTopContainers(Resource input, boolean isSingleContainerOnly) {
    final List<String> strings = SplitUtils.linesList(input);
    final ArrayList<Stack<String>> stacks = getStacks(strings);

    for (int i = commandStartIndex; i < strings.size(); i++) {
      final String[] command = strings.get(i).split(" ");

      final Stack<String> stackToPull = stacks.get(Integer.parseInt(command[3]) - 1);
      final Stack<String> stackToPush = stacks.get(Integer.parseInt(command[5]) - 1);

      final int amountToPull = Integer.parseInt(command[1]);

      if (isSingleContainerOnly) {
        for (int j = 0; j < amountToPull; j++) {
          stackToPush.push(stackToPull.pop());
        }
      } else {
        final Stack<String> helperStack = new Stack<>();
        for (int j = 0; j < amountToPull; j++) {
          helperStack.push(stackToPull.pop());
        }
        for (int j = 0; j < amountToPull; j++) {
          stackToPush.push(helperStack.pop());
        }
      }
    }

    return stacks.stream().map(Stack::pop).collect(Collectors.joining());
  }

  private static ArrayList<Stack<String>> getStacks(List<String> strings) {
    final ArrayList<Stack<String>> stacks = new ArrayList<>();

    int stackIdLineNumber = 0;

    for (int i = 0; i < strings.size(); i++) {
      String currentLine = strings.get(i);
      if (currentLine.startsWith(" 1")) {
        stackIdLineNumber = i;
        commandStartIndex = i + 2;
        for (int j = 0; j < currentLine.replace(" ", "").length(); j++) {
          stacks.add(new Stack<>());
        }
        break;
      }
    }

    for (int i = stackIdLineNumber - 1; i >= 0; i--) {
      for (int j = 1, k = 0; k < stacks.size(); j += 4, k++) {
        final String currentLine = strings.get(i);
        if (j < currentLine.length()) {
          final String charToAdd = String.valueOf(currentLine.charAt(j));
          if (!" ".equals(charToAdd)) {
            stacks.get(k).push(charToAdd);
          }
        }
      }
    }
    return stacks;
  }

  static String doThingAdvanced(Resource input) {
    return getFinalTopContainers(input, false);
  }
}
