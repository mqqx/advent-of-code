package dev.mqqx.aoc.year21;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Day16Test {

  @Test
  void testSolvePart1Example() {
    final Resource example = new ClassPathResource("year21/day16/16-example");

    assertThat(Day16.solvePart1(example)).isEqualTo(6);
  }

  private static Stream<Arguments> provideOperatorPackets() {
    return Stream.of(arguments("38006F45291200", 9), arguments("EE00D40C823060", 14));
  }

  @ParameterizedTest
  @MethodSource("provideOperatorPackets")
  void testSolvePart1OperatorExamples(String packet, Integer expectedVersionSum) {
    assertThat(Day16.solvePart1(new ByteArrayResource(packet.getBytes())))
        .isEqualTo(expectedVersionSum);
  }

  private static Stream<Arguments> provideLongPackets() {
    return Stream.of(
        arguments("8A004A801A8002F478", 16),
        arguments("620080001611562C8802118E34", 12),
        arguments("C0015000016115A2E0802F182340", 23),
        arguments("A0016C880162017C3686B18A3D4780", 31));
  }

  @ParameterizedTest
  @MethodSource("provideLongPackets")
  void testSolvePart1LongerExamples(String packet, Integer expectedVersionSum) {
    assertThat(Day16.solvePart1(new ByteArrayResource(packet.getBytes())))
        .isEqualTo(expectedVersionSum);
  }

  @Test
  void testSolvePart1() {
    final Resource resource = new ClassPathResource("year21/day16/16");

    assertThat(Day16.solvePart1(resource)).isEqualTo(984);
  }

  @Test
  void testSolvePart2Example() {
    final Resource example = new ClassPathResource("year21/day16/16-example");

    assertThat(Day16.solvePart2(example)).isEqualTo(2_021);
  }

  private static Stream<Arguments> provideLongPacketsPart2() {
    return Stream.of(
        arguments("C200B40A82", 3L),
        arguments("04005AC33890", 54L),
        arguments("880086C3E88112", 7L),
        arguments("CE00C43D881120", 9L),
        arguments("D8005AC2A8F0", 1L),
        arguments("F600BC2D8F", 0L),
        arguments("9C005AC2F8F0", 0L),
        arguments("9C0141080250320F1802104A08", 1L));
  }

  @ParameterizedTest
  @MethodSource("provideLongPacketsPart2")
  void testSolvePart2Examples(String packet, Long expectedVersionSum) {
    assertThat(Day16.solvePart2(new ByteArrayResource(packet.getBytes())))
        .isEqualTo(expectedVersionSum);
  }

  @Test
  void testSolvePart2() {
    final Resource resource = new ClassPathResource("year21/day16/16");

    assertThat(Day16.solvePart2(resource)).isEqualTo(1_015_320_896_946L);
  }
}
