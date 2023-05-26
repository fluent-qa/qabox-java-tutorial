package com.admin4j.api;

/**
 * @author andanyang
 * @since 2023/4/24 15:28
 */
public interface PipelinePlugin<T> extends Prioritized {

    void start(T args);
}
