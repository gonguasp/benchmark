package io.github.gonguasp.benchmark.executor;

import io.github.gonguasp.benchmark.annotation.Benchmark;
import io.github.gonguasp.benchmark.dto.Executions;
import io.github.gonguasp.benchmark.util.BenchmarkPrinter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class BenchmarkExecutor implements CommandLineRunner {

  private final ApplicationContext context;

  private Map<String, Executions> statistics = new HashMap<>();

  @Override
  public void run(String... args) throws Exception {
    log.info("STARTING BENCHMARK...");
    for (String beanName : context.getBeanDefinitionNames()) {
      Method[] methods = AopUtils.getTargetClass(context.getBean(beanName)).getDeclaredMethods();

      for (Method method : methods) {
        if (method.isAnnotationPresent(Benchmark.class)) {
          if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalAccessException("Method " + method.getName() + " in bean " + beanName
                + " must be public to execute benchmark."
                + " To solve this remove @Benchmark annotation from it or make it public.");
          } else if (method.getParameters().length > 0) {
            throw new InvocationTargetException(new Throwable(),
                "Method " + method.getName() + " in bean " + beanName
                    + " can not have parameters to execute benchmark."
                    + " To solve this remove @Benchmark annotation from it or remove its parameter/s.");
          } else {
            Object bean = context.getBean(beanName);
            String execution = "Class " + bean.getClass().getName() + " method " + method.getName();
            log.info("Starting benchmark for " + execution);
            Benchmark benchmark = method.getDeclaredAnnotation(Benchmark.class);
            executeBenchmark(execution, bean, method, benchmark.warmups(), benchmark.iterations(),
                benchmark.timeUnits());
            log.info("Finished benchmark for " + execution + System.lineSeparator());
          }
        }
      }
    }
    log.info("FINISH BENCHMARK");
    BenchmarkPrinter.printStatistics(statistics);
  }

  private void executeBenchmark(String execution, Object bean, Method method, Integer warmups,
      Integer iterations, TimeUnit timeUnit)
      throws InvocationTargetException, IllegalAccessException {
    for (int i = 0; i < warmups; i++) {
      log.info("Starting warmup " + (i + 1));
      method.invoke(bean);
    }

    log.info("Starting iterations" + System.lineSeparator());
    for (int i = 0; i < iterations; i++) {
      log.info("Starting iteration " + (i + 1));
      long start = System.nanoTime();
      method.invoke(bean);
      long executionTime = System.nanoTime() - start;
      if (statistics.get(execution) == null) {
        statistics.put(execution, new Executions(new ArrayList<>(), timeUnit));
      }
      executionTime = timeUnit.convert(executionTime, TimeUnit.NANOSECONDS);
      statistics.get(execution).getExecutions().add(executionTime);
      log.info("Iteration " + (i + 1) + " had a cost of " + executionTime + " " + timeUnit.name()
          + System.lineSeparator());
    }
  }
}
