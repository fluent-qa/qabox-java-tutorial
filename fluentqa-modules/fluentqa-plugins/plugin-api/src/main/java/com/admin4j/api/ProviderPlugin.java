package com.admin4j.api;

/**
 * 策略模式
 *
 * @author andanyang
 * @since 2023/4/24 15:31
 */
public interface ProviderPlugin<T> extends Prioritized {

    boolean supports(T t);
}
