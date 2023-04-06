package io.fluent.qabox.service;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.query.ColumnQuery;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.Page;


import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface IBoxDataService {

    @Comment("根据主键id获取数据")
    Object findDataById(BoxModel boxModel, @Comment("主键值") Object id);

    @Comment("查询分页数据")
    Page queryList(BoxModel boxModel, @Comment("分页对象") Page page, @Comment("条件") BoxQuery boxQuery);

    @Comment("根据列查询相关数据")
    Collection<Map<String, Object>> queryColumn(BoxModel eruptModel, @Comment("列信息") List<ColumnQuery> columns, @Comment("条件") BoxQuery boxQuery);

    @Comment("添加数据")
    void addData(BoxModel boxModel, @Comment("数据对象") Object object);

    @Comment("修改数据")
    void editData(BoxModel boxModel, @Comment("数据对象") Object object);

    @Comment("删除数据")
    void deleteData(BoxModel boxModel, @Comment("数据对象") Object object);

}
