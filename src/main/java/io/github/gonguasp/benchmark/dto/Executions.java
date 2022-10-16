package io.github.gonguasp.benchmark.dto;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Executions {

  private List<Long> executions;
  private TimeUnit timeUnit;
}
