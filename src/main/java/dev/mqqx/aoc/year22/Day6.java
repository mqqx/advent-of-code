package dev.mqqx.aoc.year22;

import dev.mqqx.aoc.util.SplitUtils;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day6 {

  static int doThing(Resource input) {
    final List<String> strings = SplitUtils.linesList(input);

    for (int i = 0; i < strings.get(0).length(); i++) {
      final char firstChar = strings.get(0).charAt(i);
      final char secondChar = strings.get(0).charAt(i + 1);
      final char thirdChar = strings.get(0).charAt(i + 2);
      final char fourthChar = strings.get(0).charAt(i + 3);

      final HashSet<Character> chars = new HashSet<>();
      chars.add(firstChar);
      chars.add(secondChar);
      chars.add(thirdChar);
      chars.add(fourthChar);

      if (chars.size() == 4) {
        return i + 4;
      }
    }

    return -1;
  }

  static int doThingAdvanced(Resource input) {
    final List<String> strings = SplitUtils.linesList(input);

    for (int i = 0; i < strings.get(0).length(); i++) {
      final char firstChar = strings.get(0).charAt(i);
      final char secondChar = strings.get(0).charAt(i + 1);
      final char thirdChar = strings.get(0).charAt(i + 2);
      final char fourthChar = strings.get(0).charAt(i + 3);
      final char a = strings.get(0).charAt(i + 4);
      final char b = strings.get(0).charAt(i + 5);
      final char c = strings.get(0).charAt(i + 6);
      final char d = strings.get(0).charAt(i + 7);
      final char e = strings.get(0).charAt(i + 8);
      final char f = strings.get(0).charAt(i + 9);
      final char g = strings.get(0).charAt(i + 10);
      final char h = strings.get(0).charAt(i + 11);
      final char k = strings.get(0).charAt(i + 12);
      final char j = strings.get(0).charAt(i + 13);

      final HashSet<Character> chars = new HashSet<>();
      chars.add(firstChar);
      chars.add(secondChar);
      chars.add(thirdChar);
      chars.add(fourthChar);
      chars.add(a);
      chars.add(b);
      chars.add(d);
      chars.add(c);
      chars.add(e);
      chars.add(f);
      chars.add(g);
      chars.add(h);
      chars.add(k);
      chars.add(j);

      if (chars.size() == 14) {
        return i + 14;
      }
    }

    return -1;
  }
}
