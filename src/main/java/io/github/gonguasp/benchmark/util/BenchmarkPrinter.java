package io.github.gonguasp.benchmark.util;

import com.google.gson.GsonBuilder;
import io.github.gonguasp.benchmark.dto.Executions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BenchmarkPrinter {

  private static Map<String, Map<String, Object>> summaryStatistics = new ConcurrentHashMap<>();

  private final static String MAX = "max";
  private final static String MIN = "min";
  private final static String AVG = "avg";
  private final static String TIME_UNIT = "timeUnit";

  public static void printStatistics(Map<String, Executions> statistics) {
    new ConcurrentHashMap<>(statistics).entrySet()
        .parallelStream().forEach(entryMap -> {
          Map<String, Object> results = new HashMap<>();
          summaryStatistics.put(entryMap.getKey(), results);
          results.put(TIME_UNIT, entryMap.getValue().getTimeUnit());
          results.put(MAX, Collections.max(entryMap.getValue().getExecutions()));
          results.put(MIN, Collections.min(entryMap.getValue().getExecutions()));
          results.put(AVG,
              (long) entryMap.getValue().getExecutions().stream().
                  mapToDouble(d -> d).
                  average().orElse(0));
        });
    log.info(System.lineSeparator() + "Results:" + System.lineSeparator() +
        new GsonBuilder().setPrettyPrinting().create().toJson(summaryStatistics));
  }
}
