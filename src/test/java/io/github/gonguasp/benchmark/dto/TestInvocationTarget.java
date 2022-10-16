package io.github.gonguasp.benchmark.dto;

import io.github.gonguasp.benchmark.annotation.Benchmark;

public class TestInvocationTarget {

  @Benchmark(warmups = 1)
  public void test(int i) {}
}
