package io.fluent.qabox.data.fun;

import io.fluent.qabox.config.Comment;
public interface MetaProxy<MODEL> {

    @Comment("Don't call")
    default MetaProxy<? extends MODEL> dual() {
        return null;
    }

}
