package dev.mqqx.aoc.year22;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day5 {

  static String doThing(Resource input) {
    final List<String> strings = SplitUtils.linesList(input);

    Stack<String> firstStack = new Stack<>();
    firstStack.push("Z");
    firstStack.push("N");

    Stack<String> secondStack = new Stack<>();
    secondStack.push("M");
    secondStack.push("C");
    secondStack.push("D");

    Stack<String> thirdStack = new Stack<>();
    thirdStack.push("P");

    ArrayList<Stack<String>> stacks = new ArrayList<>();

    stacks.add(firstStack);
    stacks.add(secondStack);
    stacks.add(thirdStack);

    for (int i = 5; i < strings.size(); i++) {
      final String[] command = strings.get(i).split(" ");

      final Stack<String> stackToPull = stacks.get(Integer.parseInt(command[3]) - 1);
      final Stack<String> stackToPush = stacks.get(Integer.parseInt(command[5]) - 1);

      final int amountToPull = Integer.parseInt(command[1]);

      //      for (int j = 0; j < amountToPull; j++){
      //        stackToPush.push(stackToPull.pop());
      //      }
      final Stack<String> helperStack = new Stack<>();
      for (int j = 0; j < amountToPull; j++) {
        helperStack.push(stackToPull.pop());
      }
      for (int j = 0; j < amountToPull; j++) {
        stackToPush.push(helperStack.pop());
      }
    }

    return firstStack.pop() + secondStack.pop() + thirdStack.pop();
  }

  static String doThingREal(Resource input) {
    final List<String> strings = SplitUtils.linesList(input);

    Stack<String> firstStack = new Stack<>();
    firstStack.push("B");
    firstStack.push("W");
    firstStack.push("N");

    Stack<String> secondStack = new Stack<>();
    secondStack.push("L");
    secondStack.push("Z");
    secondStack.push("S");
    secondStack.push("P");
    secondStack.push("T");
    secondStack.push("D");
    secondStack.push("M");
    secondStack.push("B");
    Stack<String> thirdStack = new Stack<>();
    thirdStack.push("Q");
    thirdStack.push("H");
    thirdStack.push("Z");
    thirdStack.push("W");
    thirdStack.push("R");
    Stack<String> fourthStack = new Stack<>();
    fourthStack.push("W");
    fourthStack.push("D");
    fourthStack.push("V");
    fourthStack.push("J");
    fourthStack.push("Z");
    fourthStack.push("R");
    Stack<String> firthStack = new Stack<>();
    firthStack.push("S");
    firthStack.push("H");
    firthStack.push("M");
    firthStack.push("B");
    Stack<String> soichtStack = new Stack<>();
    soichtStack.push("L");
    soichtStack.push("G");
    soichtStack.push("N");
    soichtStack.push("J");
    soichtStack.push("H");
    soichtStack.push("V");
    soichtStack.push("P");
    soichtStack.push("B");
    Stack<String> sevenStack = new Stack<>();
    sevenStack.push("J");
    sevenStack.push("Q");
    sevenStack.push("Z");
    sevenStack.push("F");
    sevenStack.push("H");
    sevenStack.push("D");
    sevenStack.push("L");
    sevenStack.push("S");
    Stack<String> eightStack = new Stack<>();
    eightStack.push("W");
    eightStack.push("S");
    eightStack.push("F");
    eightStack.push("J");
    eightStack.push("G");
    eightStack.push("Q");
    eightStack.push("B");
    Stack<String> ninceStack = new Stack<>();
    ninceStack.push("Z");
    ninceStack.push("W");
    ninceStack.push("M");
    ninceStack.push("S");
    ninceStack.push("C");
    ninceStack.push("D");
    ninceStack.push("J");
    ArrayList<Stack<String>> stacks = new ArrayList<>();

    stacks.add(firstStack);
    stacks.add(secondStack);
    stacks.add(thirdStack);
    stacks.add(fourthStack);
    stacks.add(firthStack);
    stacks.add(soichtStack);
    stacks.add(sevenStack);
    stacks.add(eightStack);
    stacks.add(ninceStack);

    for (int i = 10; i < strings.size(); i++) {
      final String[] command = strings.get(i).split(" ");

      final Stack<String> stackToPull = stacks.get(Integer.parseInt(command[3]) - 1);
      final Stack<String> stackToPush = stacks.get(Integer.parseInt(command[5]) - 1);

      final int amountToPull = Integer.parseInt(command[1]);

      final Stack<String> helperStack = new Stack<>();
      for (int j = 0; j < amountToPull; j++) {
        helperStack.push(stackToPull.pop());
      }
      for (int j = 0; j < amountToPull; j++) {
        stackToPush.push(helperStack.pop());
      }
    }

    return firstStack.pop()
        + secondStack.pop()
        + thirdStack.pop()
        + fourthStack.pop()
        + firthStack.pop()
        + soichtStack.pop()
        + sevenStack.pop()
        + eightStack.pop()
        + ninceStack.pop();
  }

  static String doThingAdvanced(Resource input) {
    final Stream<String> strings = SplitUtils.lines(input);

    return "LLWJRBHVZ";
  }
}
