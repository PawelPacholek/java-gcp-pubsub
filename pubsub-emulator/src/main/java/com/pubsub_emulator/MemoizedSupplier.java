package com.pubsub_emulator;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Both cached and lazy value.
 * See https://en.wikipedia.org/wiki/Memoization
 */
public class MemoizedSupplier<T> implements Supplier<T> {

  private final Supplier<T> supplier;
  private T value;

  public MemoizedSupplier(@NotNull Supplier<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  public synchronized T get() {
    if (value == null) {
      T result = supplier.get();
      if (result == null)
        throw new IllegalArgumentException();
      value = result;
    }
    return value;
  }

}
