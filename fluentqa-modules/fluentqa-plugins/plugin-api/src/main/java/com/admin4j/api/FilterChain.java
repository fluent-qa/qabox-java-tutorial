package com.admin4j.api;

/**
 * @author andanyang
 * @since 2023/4/25 15:58
 */
public interface FilterChain<T> {


    void doFilter(T t);

}
