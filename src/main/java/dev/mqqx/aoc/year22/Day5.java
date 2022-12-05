package dev.mqqx.aoc.year22;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day5 {
  static String getTopContainersSingle(Resource input) {
    return getFinalTopContainers(input, true);
  }

  static String getTopContainersMulti(Resource input) {
    return getFinalTopContainers(input, false);
  }

  record ContainerStacks(List<Stack<String>> stacks, int commandStartIndex) {}

  private static String getFinalTopContainers(Resource input, boolean isSingleContainerOnly) {
    final List<String> strings = SplitUtils.linesList(input);
    final ContainerStacks containerStacks = initializeStacks(strings);

    for (int i = containerStacks.commandStartIndex; i < strings.size(); i++) {
      final String[] command = strings.get(i).split(" ");

      moveContainers(isSingleContainerOnly, containerStacks, command);
    }

    return containerStacks.stacks.stream().map(Stack::pop).collect(Collectors.joining());
  }

  private static ContainerStacks initializeStacks(List<String> strings) {
    final ContainerStacks containerStacks = createInitialStacks(strings);

    for (int i = containerStacks.commandStartIndex - 3; i >= 0; i--) {
      for (int j = 1, k = 0; k < containerStacks.stacks.size(); j += 4, k++) {
        final String currentLine = strings.get(i);
        if (j < currentLine.length()) {
          final String charToAdd = String.valueOf(currentLine.charAt(j));
          if (!" ".equals(charToAdd)) {
            containerStacks.stacks.get(k).push(charToAdd);
          }
        }
      }
    }
    return containerStacks;
  }

  private static ContainerStacks createInitialStacks(List<String> strings) {
    final List<Stack<String>> stacks = new ArrayList<>();
    AtomicInteger commandStartIndex = new AtomicInteger(1);

    strings.stream()
        .map(
            currentLine -> {
              commandStartIndex.getAndIncrement();
              return currentLine;
            })
        .filter(currentLine -> currentLine.startsWith(" 1"))
        .map(currentLine -> currentLine.replace(" ", ""))
        .findFirst()
        .map(String::chars)
        .ifPresent(intStream -> intStream.forEach(j -> stacks.add(new Stack<>())));

    return new ContainerStacks(stacks, commandStartIndex.get());
  }

  private static void moveContainers(
      boolean isSingleContainerOnly, ContainerStacks containerStacks, String[] command) {
    final Stack<String> stackToPull = containerStacks.stacks.get(Integer.parseInt(command[3]) - 1);
    final Stack<String> stackToPush = containerStacks.stacks.get(Integer.parseInt(command[5]) - 1);

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
}
