package io.fluent.qabox.util.cache;

import io.fluent.qabox.util.cache.impl.BoxLocalMemCache;

import java.util.Optional;
import java.util.function.Supplier;


public abstract class BoxCache<V> {

  protected abstract V put(String key, long timeout, V v);

  protected abstract V get(String key);

  public static <V> BoxCache<V> newInstance() {
    return new BoxLocalMemCache<>();
  }

  public V getAndSet(String key, long timeout, Supplier<V> supplier) {
    return Optional.ofNullable(this.get(key)).orElseGet(() -> this.put(key, timeout, supplier.get()));
  }

}
