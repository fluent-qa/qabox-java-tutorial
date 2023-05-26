package com.admin4j.api;

/**
 * @author andanyang
 * @since 2023/4/24 15:28
 */
public interface FilterPlugin<T> extends Prioritized {
    
    default void start(T t) {
    }

    void doFilter(T t, FilterChain<T> filterChain);


}
