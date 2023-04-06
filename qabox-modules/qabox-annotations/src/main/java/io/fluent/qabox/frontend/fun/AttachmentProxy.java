package io.fluent.qabox.frontend.fun;


import io.fluent.qabox.config.Comment;

import java.io.InputStream;

@Comment("自定义附件上传策略")
public interface AttachmentProxy {

    @Comment("附件上传")
    @Comment("返回值表示存储路径，多数情况下返回 path 即可")
    String upLoad(@Comment("数据流") InputStream inputStream, @Comment("上传位置") String path);

    @Comment("附件所在域名")
    String fileDomain();

    @Comment("是否同时保存到本地服务器")
    default boolean isLocalSave() {
        return true;
    }
}
