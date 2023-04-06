package io.fluent.qabox.service;

import io.fluent.qabox.config.QueryExpression;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.exception.BoxPermissionException;
import io.fluent.qabox.frontend.operation.LinkTree;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.Boxes;
import io.fluent.qabox.util.DataHandlerUtil;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.Page;
import io.fluent.qabox.view.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class BoxModelService {

    /**
     * @param boxModel      boxModel
     * @param tableQueryVo    前端查询对象
     * @param serverCondition 自定义条件
     * @param customCondition 条件字符串
     */
    public Page getEruptData(BoxModel boxModel, TableQueryVo tableQueryVo, List<Condition> serverCondition, String... customCondition) {
        Boxes.powerLegal(boxModel, PermissionObject::isQuery);
        List<Condition> legalConditions = BoxUtil.geneEruptSearchCondition(boxModel, tableQueryVo.getCondition());
        List<String> conditionStrings = new ArrayList<>();
        //DependTree logic
        LinkTree dependTree = boxModel.getBox().linkTree();
        if (StringUtils.isNotBlank(dependTree.field())) {
            if (null == tableQueryVo.getLinkTreeVal()) {
                if (dependTree.dependNode()) return new Page();
            } else {
                BoxModel treeErupt = BoxCoreService.getBoxModel(ReflectUtil.findClassField(boxModel.getClazz(), dependTree.field()).getType().getSimpleName());
                conditionStrings.add(dependTree.field() + "." + treeErupt.getBox().primaryKeyCol() + " = '" + tableQueryVo.getLinkTreeVal() + "'");
            }
        }
        conditionStrings.addAll(Arrays.asList(customCondition));
        DataProxyInvoke.invoke(boxModel, (dataProxy -> Optional.ofNullable(dataProxy.beforeFetch(legalConditions)).ifPresent(conditionStrings::add)));
        Optional.ofNullable(serverCondition).ifPresent(legalConditions::addAll);
        Page page = DataProcessorManager.getDataProcessor(boxModel.getClazz())
                .queryList(boxModel, new Page(tableQueryVo.getPageIndex(), tableQueryVo.getPageSize(), tableQueryVo.getSort()),
                        BoxQuery.builder().orderBy(tableQueryVo.getSort()).conditionStrings(conditionStrings).conditions(legalConditions).build());
        DataProxyInvoke.invoke(boxModel, (dataProxy -> dataProxy.afterFetch(page.getList())));
        Optional.ofNullable(page.getList()).ifPresent(it -> DataHandlerUtil.convertDataToEruptView(boxModel, it));
        return page;
    }

    /**
     * 校验id使用权限
     *
     * @param boxModel eruptModel
     * @param id         标识主键
     */
    public void verifyIdPermissions(BoxModel boxModel, String id) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition(boxModel.getBox().primaryKeyCol(), id, QueryExpression.EQ));
        Page page = DataProcessorManager.getDataProcessor(boxModel.getClazz())
                .queryList(boxModel, new Page(0, 1, null),
                        BoxQuery.builder().conditions(conditions).build());
        if (page.getList().size() == 0) {
            throw new BoxPermissionException();
        }
    }

}
