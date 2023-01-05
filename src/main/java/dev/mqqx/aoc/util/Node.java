package dev.mqqx.aoc.util;

import lombok.NonNull;

public record Node<T extends Comparable<T>>(Point p, T weight) implements Comparable<Node<T>> {
  @Override
  public int compareTo(@NonNull Node<T> o) {
    if (weight != null && o.weight() != null) {
      return weight.compareTo(o.weight());
    }
    return -1;
  }
}
