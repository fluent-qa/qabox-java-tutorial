package io.fluent.qabox.frontend.operation;



import io.fluent.qabox.config.AutoFill;
import io.fluent.qabox.config.Comment;
import io.fluent.qabox.expr.ExprBool;
import io.fluent.qabox.frontend.OperationHandler;

import java.beans.Transient;

public @interface RowOperation {

    @Deprecated
    @AutoFill("T(Integer).toString(#item.title().hashCode())")
    String code() default "";

    String title();

    @Transient
    ExprBool show() default @ExprBool;

    String tip() default "";

    @Comment("图标请参考Font Awesome")
    String icon() default "fa fa-ravelry";

    @Comment("功能模式")
    Mode mode() default Mode.MULTI;

    @Comment("功能类型")
    Type type() default Type.ERUPT;

    @Comment("控制按钮显示与隐藏 或 能否点击（JS表达式），变量：item 获取整行数据")
    String ifExpr() default "";

    @Comment("控制 ifExpr 的结果是控制按钮的 显示与隐藏 还是 能否点击")
    IfExprBehavior ifExprBehavior() default IfExprBehavior.DISABLE;

    @Comment("type为tpl时可用，可在模板中使用rows变量，可获取选中行的数据")
    @Transient
    Tpl tpl() default @Tpl(path = "");

    @Comment("按钮提交时，需要填写的表单信息")
    Class<?> eruptClass() default void.class;

    @Transient
    @Comment("该配置可在operationHandler中获取")
    String[] operationParam() default {};

    @Transient
    @Comment("type为ERUPT时可用，操作按钮点击后，后台处理逻辑")
    Class<? extends OperationHandler> operationHandler() default OperationHandler.class;

    enum Mode {
        @Comment("依赖单行数据")
        SINGLE,
        @Comment("依赖多行数据")
        MULTI,
        @Comment("不依赖行数据")
        BUTTON
    }

    enum Type {
        @Comment("通过erupt表单渲染，operationHandler进行逻辑处理")
        ERUPT,
        @Comment("通过自定义模板渲染")
        TPL
    }

    enum IfExprBehavior {
        @Comment("IfExpr处理按钮显示或隐藏")
        HIDE,
        @Comment("IfExpr处理按钮可否点击")
        DISABLE
    }
}
