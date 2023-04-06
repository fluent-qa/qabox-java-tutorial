package io.fluent.qabox.frontend;

import io.fluent.qabox.config.Comment;

import java.util.List;

public interface OperationHandler<@Comment("行数据类型") Target, @Comment("表单输入对象类型") EruptObject> {

    /**
     * @param data        行数据
     * @param eruptObject 表单输入数据
     * @param param       注解回传参数
     * @return 事件触发成功后需要前端执行的 js 表达式
     */
    @Comment("按钮事件触发类")
    @Comment("返回值：事件触发成功后需要前端执行的 js 表达式，不需要此参数返回空即可")
    String exec(List<Target> data, EruptObject eruptObject, String[] param);

}
