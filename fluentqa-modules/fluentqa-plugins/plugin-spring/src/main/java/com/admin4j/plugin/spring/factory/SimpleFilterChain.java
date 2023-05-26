package com.admin4j.plugin.spring.factory;

import com.admin4j.api.FilterChain;
import com.admin4j.api.FilterPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andanyang
 * @since 2023/4/25 16:06
 */
public class SimpleFilterChain<T> implements FilterChain<T> {

    private final List<FilterPlugin<T>> filters;
    private int index;

    public SimpleFilterChain() {
        filters = new ArrayList<>();
        index = -1;
    }

    public SimpleFilterChain(List<FilterPlugin<T>> filters) {
        this.filters = filters;
        index = -1;
    }


    @Override
    public void doFilter(T t) {
        int size = filters.size();
        index++;
        if (index < size) {

            filters.get(index).doFilter(t, this);
        }
    }


}
