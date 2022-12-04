package dev.mqqx.aoc.year21;

import static dev.mqqx.aoc.util.SplitUtils.linesToIntList;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day1 {

  static int countDepthIncreases(Resource depthMeasurementsResource) {
    final List<Integer> depthMeasurements = linesToIntList(depthMeasurementsResource);

    return countDepthIncreases(depthMeasurements);
  }

  private static int countDepthIncreases(List<Integer> depthMeasurements) {
    int depthIncreases = 0;

    for (int i = 1; i < depthMeasurements.size(); i++) {
      if (depthMeasurements.get(i) > depthMeasurements.get(i - 1)) {
        depthIncreases++;
      }
    }

    return depthIncreases;
  }

  static int countThreeMeasurementsWindowIncreases(Resource depthMeasurementsResource) {
    final List<Integer> depthMeasurements = linesToIntList(depthMeasurementsResource);

    List<Integer> slidingWindows = new ArrayList<>();

    for (int i = 1; i < depthMeasurements.size() - 1; i++) {
      slidingWindows.add(
          depthMeasurements.get(i - 1) + depthMeasurements.get(i) + depthMeasurements.get(i + 1));
    }

    return countDepthIncreases(slidingWindows);
  }
}
