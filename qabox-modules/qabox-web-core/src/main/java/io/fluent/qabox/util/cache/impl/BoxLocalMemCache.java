package io.fluent.qabox.util.cache.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import io.fluent.qabox.util.cache.BoxCache;

public class BoxLocalMemCache<V> extends BoxCache<V> {

    private final TimedCache<String, V> cache;

    public BoxLocalMemCache() {
        cache = CacheUtil.newTimedCache(1000);
        cache.schedulePrune(1000 * 60);
    }

    @Override
    protected V put(String key, long timeout, V v) {
        cache.put(key, v, timeout);
        return v;
    }

    @Override
    protected V get(String key) {
        return cache.get(key);
    }
}
