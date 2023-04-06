package io.fluent.qabox;



import io.fluent.qabox.config.Comment;
import io.fluent.qabox.config.Match;
import io.fluent.qabox.config.ToMap;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.frontend.operation.*;

import java.beans.Transient;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Box {

    @Comment("主键列名")
    String primaryKeyCol() default "id";

    @Transient
    @Comment("功能名称")
    String name();

    @Comment("功能描述")
    String desc() default "";

    @Transient
    @Comment("访问该功能是否进行校验权限")
    boolean authVerify() default true;

    @Transient
    @Comment("操作权限配置")
    UIPermission permission() default @UIPermission;

//    @Comment("多页签配置")
//    Tab[] tabs() default {};

    @Comment("自定义功能按钮")
    RowOperation[] rowOperation() default {};

    @Comment("数据钻取功能")
    Drill[] drills() default {};

    @Transient
    @Comment("数据过滤表达式")
    Filter[] filter() default {};

    @Transient
    @Comment("排序表达式")
    String orderBy() default "";

    @Transient
    @Comment("数据行为代理接口，对增、删、改、查等行为做逻辑处理")
    Class<? extends DataProxy<?>>[] dataProxy() default {};

    @Comment("树节点配置")
    Tree tree() default @Tree;

    @Match("#value.field() != ''")
    @Comment("左树右表配置项")
    LinkTree linkTree() default @LinkTree(field = "");

    @ToMap(key = "key")
    @Comment("自定义扩展参数")
    KV[] param() default {};

    @Deprecated
    @Transient
    Class<? extends Annotation> extra() default Annotation.class;

}
