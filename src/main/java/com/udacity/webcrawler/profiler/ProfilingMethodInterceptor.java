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

  private Object target;

  ProfilingMethodInterceptor(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.state = new ProfilingState();
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object obj = null;
    if (method.getAnnotation(Profiled.class) != null) {
      Instant startTime = clock.instant();
      try {
        obj = method.invoke(target, args);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      } finally {
        Instant endTime = clock.instant();
        state.record(method.getDeclaringClass(), method, Duration.between(startTime, endTime));
      }
    }
    if (obj != null) return obj;
    throw new IllegalArgumentException("Method is not annotated with @Profiled");
  }

  Clock getClock() {
    return clock;
  }

  ProfilingState getState() {
    return state;
  }

  @Override
  public boolean equals(Object obj){
    if (obj == this) return true;
    if(obj instanceof ProfilingMethodInterceptor) {
      ProfilingMethodInterceptor obj2 = (ProfilingMethodInterceptor) obj;
      return this.clock.equals(obj2.getClock()) && this.state.equals(obj2.getState());
    }
    return false;
  }


}
