package com.admin4j.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author andanyang
 * @since 2023/4/20 11:13
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Wrapper {
    /**
     * the extension names that need to be wrapped.
     */
    String[] matches() default {};

    /**
     * the extension names that need to be excluded.
     */
    String[] mismatches() default {};
}
