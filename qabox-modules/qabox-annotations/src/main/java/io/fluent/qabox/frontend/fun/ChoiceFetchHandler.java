package io.fluent.qabox.frontend.fun;


import io.fluent.qabox.config.Comment;

import java.util.List;


public interface ChoiceFetchHandler {

    @Comment("获取下拉列表")
    List<VLModel> fetch(String[] params);

}
