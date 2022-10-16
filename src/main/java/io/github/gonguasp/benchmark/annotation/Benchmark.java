package io.github.gonguasp.benchmark.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Benchmark {

  TimeUnit timeUnits() default TimeUnit.MILLISECONDS;

  int iterations() default 1;

  int warmups() default 0;
}
