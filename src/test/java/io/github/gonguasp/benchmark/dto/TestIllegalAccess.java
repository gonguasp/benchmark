package io.github.gonguasp.benchmark.dto;

import io.github.gonguasp.benchmark.annotation.Benchmark;

public class TestIllegalAccess {

  @Benchmark(warmups = 1)
  private void test() {}
}
