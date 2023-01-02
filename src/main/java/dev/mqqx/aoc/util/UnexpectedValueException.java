package dev.mqqx.aoc.util;

public class UnexpectedValueException extends IllegalStateException {

  public UnexpectedValueException(Object value) {
    super("Unexpected value: " + value);
  }
}
