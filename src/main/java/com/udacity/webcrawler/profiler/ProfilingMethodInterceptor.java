package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Objects;
import java.time.Instant;
import java.time.Duration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;

  private ProfilingState state;

  ProfilingMethodInterceptor(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.state = new ProfilingState();
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object obj = proxy;
    if (method.getAnnotation(Profiled.class) != null) {
      Instant startTime = clock.instant();
      try {
        obj = method.invoke(proxy, args);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      } finally {
        Instant endTime = clock.instant();
        state.record(method.getDeclaringClass(), method, Duration.between(startTime, endTime));
      }
    }
    return obj;
  }
}
