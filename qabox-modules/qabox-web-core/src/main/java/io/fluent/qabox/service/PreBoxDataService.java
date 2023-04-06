package io.fluent.qabox.service;

import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.expr.Expr;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.processor.invoker.ExprResolverInvoke;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.query.ColumnQuery;
import io.fluent.qabox.util.DataHandlerUtil;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.TreeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class PreBoxDataService {

    /**
     * 根据要素生成树结构
     *
     * @param boxModel eruptModel
     * @param id         id
     * @param label      label
     * @param pid        parent id
     * @param query      查询对象
     * @return 树对象
     */
    public Collection<TreeModel> geneTree(BoxModel boxModel, String id, String label, String pid,
                                          Expr rootId, BoxQuery query) {
        List<ColumnQuery> columns = new ArrayList<>();
        columns.add(new ColumnQuery(id, AnnotationConst.ID));
        columns.add(new ColumnQuery(label, AnnotationConst.LABEL));
        if (!AnnotationConst.EMPTY_STR.equals(pid)) {
            columns.add(new ColumnQuery(pid, AnnotationConst.PID));
        }
        Collection<Map<String, Object>> result = this.createColumnQuery(boxModel, columns, query);
        String root = ExprResolverInvoke.getExpr(rootId);
        List<TreeModel> treeModels = new ArrayList<>();
        result.forEach(it -> treeModels.add(new TreeModel(
                it.get(AnnotationConst.ID), it.get(AnnotationConst.LABEL), it.get(AnnotationConst.PID), root
        )));
        if (StringUtils.isBlank(pid)) {
            return treeModels;
        } else {
            return DataHandlerUtil.quoteTree(treeModels);
        }
    }

    public Collection<Map<String, Object>> createColumnQuery(BoxModel boxModel, List<ColumnQuery> columns,
                                                             BoxQuery query) {
        List<String> conditionStrings = new ArrayList<>();
        DataProxyInvoke.invoke(boxModel, (dataProxy -> {
            String condition = dataProxy.beforeFetch(query.getConditions());
            if (StringUtils.isNotBlank(condition)) {
                conditionStrings.add(condition);
            }
        }));
        for (Filter filter : boxModel.getBox().filter()) {
            conditionStrings.add(filter.value());
        }
        Optional.ofNullable(query.getConditionStrings()).ifPresent(conditionStrings::addAll);
        conditionStrings.removeIf(Objects::isNull);
        String orderBy = StringUtils.isNotBlank(query.getOrderBy()) ? query.getOrderBy() : boxModel.getBox().orderBy();
        Collection<Map<String, Object>> result = DataProcessorManager.getDataProcessor(boxModel.getClazz())
                .queryColumn(boxModel, columns, BoxQuery.builder()
                        .conditions(query.getConditions()).conditionStrings(conditionStrings).orderBy(orderBy).build());
        DataProxyInvoke.invoke(boxModel, (dataProxy -> dataProxy.afterFetch(result)));
        return result;
    }

}
