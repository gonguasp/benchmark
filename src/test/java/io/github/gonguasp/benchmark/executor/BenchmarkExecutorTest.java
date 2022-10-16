package io.github.gonguasp.benchmark.executor;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.gonguasp.benchmark.dto.TestIllegalAccess;
import io.github.gonguasp.benchmark.dto.TestInvocationTarget;
import io.github.gonguasp.benchmark.dto.TestOk;
import java.lang.reflect.InvocationTargetException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

class BenchmarkExecutorTest {

  private static ApplicationContext context;

  private static BenchmarkExecutor benchmarkExecutor;

  @BeforeAll
  static void beforeAll() {
    context = Mockito.mock(ApplicationContext.class);
    benchmarkExecutor = new BenchmarkExecutor(context);
  }

  @Test
  @SneakyThrows
  void runOkTest() {
    try(MockedStatic<AopUtils> applicationConfigMockedStatic = Mockito.mockStatic(AopUtils.class)) {
      TestOk clazz = Mockito.mock(TestOk.class);
      applicationConfigMockedStatic.when(()->AopUtils.getTargetClass(Mockito.any())).thenReturn(clazz.getClass());
      Mockito.when(context.getBean(Mockito.anyString())).thenReturn(new TestOk());
      Mockito.when(context.getBeanDefinitionNames()).thenReturn(new String[]{""});

      benchmarkExecutor.run(new String[0]);
    }
  }

  @Test
  @SneakyThrows
  void runIllegalAccessExceptionTest() {
    try(MockedStatic<AopUtils> applicationConfigMockedStatic = Mockito.mockStatic(AopUtils.class)) {
      TestIllegalAccess clazz = Mockito.mock(TestIllegalAccess.class);
      applicationConfigMockedStatic.when(()->AopUtils.getTargetClass(Mockito.any())).thenReturn(clazz.getClass());
      Mockito.when(context.getBean(Mockito.anyString())).thenReturn(new TestOk());
      Mockito.when(context.getBeanDefinitionNames()).thenReturn(new String[]{""});

      assertThrows(IllegalAccessException.class, () -> benchmarkExecutor.run(new String[0]));
    }
  }

  @Test
  @SneakyThrows
  void runInvocationTargetExceptionTest() {
    try(MockedStatic<AopUtils> applicationConfigMockedStatic = Mockito.mockStatic(AopUtils.class)) {
      TestInvocationTarget clazz = Mockito.mock(TestInvocationTarget.class);
      applicationConfigMockedStatic.when(()->AopUtils.getTargetClass(Mockito.any())).thenReturn(clazz.getClass());
      Mockito.when(context.getBean(Mockito.anyString())).thenReturn(new TestOk());
      Mockito.when(context.getBeanDefinitionNames()).thenReturn(new String[]{""});

      assertThrows(InvocationTargetException.class, () -> benchmarkExecutor.run(new String[0]));
    }
  }

}
