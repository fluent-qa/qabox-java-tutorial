package io.fluent.qabox.util.cache.impl;

import com.google.gson.reflect.TypeToken;
import io.fluent.qabox.config.GsonFactory;
import io.fluent.qabox.util.cache.BoxCache;
import org.springframework.data.redis.core.StringRedisTemplate;

public class BoxRedisCache<V> extends BoxCache<V> {

    private final StringRedisTemplate stringRedisTemplate;

    public BoxRedisCache(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected V put(String key, long timeout, V v) {
        stringRedisTemplate.opsForValue().set(key, GsonFactory.getGson().toJson(v));
        return v;
    }

    @Override
    protected V get(String key) {
        return GsonFactory.getGson().fromJson(stringRedisTemplate.opsForValue().get(key), new TypeToken<V>() {
        }.getType());
    }
}
