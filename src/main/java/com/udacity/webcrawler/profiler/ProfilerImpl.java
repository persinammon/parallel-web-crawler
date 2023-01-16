package com.udacity.webcrawler.profiler;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.io.FileWriter;
import java.io.BufferedWriter;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

import java.lang.reflect.Proxy;

/**
 * Concrete implementation of the {@link Profiler}.
 */
final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state;
  private final ZonedDateTime startTime;

  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
    this.state = new ProfilingState();
  }

  @Override
  public <T> T wrap(Class<T> klass, T delegate) {
    Objects.requireNonNull(klass);
    Object delegateProxy = Proxy.newProxyInstance(klass.getClassLoader(),
            new Class[]{klass}, new ProfilingMethodInterceptor(clock));

    return (T) delegateProxy;
  }

  @Override
  public void writeData(Path path) {
    BufferedWriter writer;
    if (path.toFile().exists()) {
      try {
        writer = new BufferedWriter(new FileWriter(path.toString(), true));
        writeData(writer);
        writer.close();
      } catch(Exception e) {
      }
    } else {
      try {
        writer = new BufferedWriter(new FileWriter(path.toString()));
        writeData(writer);
        writer.close();
      } catch (Exception e) {

      }
    }
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
    writer.write(System.lineSeparator());
    state.write(writer);
    writer.write(System.lineSeparator());
  }
}
