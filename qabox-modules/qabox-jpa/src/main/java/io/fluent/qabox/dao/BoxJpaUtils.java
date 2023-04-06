package io.fluent.qabox.dao;

import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author YuePeng date 2018-11-05.
 */
public class BoxJpaUtils {

    public static final String L_VAL_KEY = "l_";

    public static final String R_VAL_KEY = "r_";

    public static final String PERCENT = "%";

    public static final String AND = " and ";

    public static final String AS = " as ";

    public static final String LEFT_JOIN = " left outer join ";

    public static Set<String> getEruptColJpaKeys(BoxModel eruptModel) {
        Set<String> cols = new HashSet<>();
        String eruptNameSymbol = eruptModel.getEruptName() + ".";
        cols.add(eruptNameSymbol + eruptModel.getBox().primaryKeyCol() + AS + eruptModel.getBox().primaryKeyCol());
        eruptModel.getEruptFieldModels().forEach(field -> {
            if (null != field.getField().getAnnotation(OneToMany.class) || null != field.getField().getAnnotation(ManyToMany.class)) {
                return;
            }
            if (null != field.getField().getAnnotation(Transient.class)) {
                return;
            }
            for (View view : field.getUiField().views()) {
                if (view.column().length() == 0) {
                    cols.add(eruptNameSymbol + field.getFieldName() + AS + field.getFieldName());
                } else {
                    cols.add(eruptNameSymbol + field.getFieldName() + "." + view.column() + AS + field.getFieldName() + "_"
                            + view.column().replace(".", "_"));
                }
            }
        });
        return cols;
    }

    //erupt 注解信息映射成hql语句
    public static String generateEruptJpaHql(BoxModel eruptModel, String cols, BoxQuery query, boolean countSql) {
        StringBuilder hql = new StringBuilder();
        if (StringUtils.isNotBlank(cols)) {
            hql.append("select ").append(cols).append(" from ")
                    .append(eruptModel.getEruptName()).append(AS).append(eruptModel.getEruptName());
            // 修复view配置多级显示时查询结果不正确的缺陷
            // 如果view配置了多级显示，则必须手动进行left join 关联，否则会因jpa自动生成的cross join 导致查询结果不完整。
            // 在这里调用改写的 generateEruptJoinHql 方法
            hql.append(generateEruptJoinHql(eruptModel));

        } else {
            hql.append("from ").append(eruptModel.getEruptName());
        }
        hql.append(geneEruptHqlCondition(eruptModel, query.getConditions(), query.getConditionStrings()));
        if (!countSql) {
            hql.append(geneEruptHqlOrderBy(eruptModel, query.getOrderBy()));
        }
        return hql.toString();
    }

    public static String generateEruptJoinHql(BoxModel eruptModel) {
        StringBuffer hql = new StringBuffer();
        ReflectUtil.findClassAllFields(eruptModel.getClazz(), field -> {
            if (null != field.getAnnotation(ManyToOne.class) || null != field.getAnnotation(OneToOne.class)) {
                BoxFieldModel model = eruptModel.getEruptFieldMap().get(field.getName());
                if (model != null) {
                    Set<String> pathSet = new HashSet<>();
                    View[] views = model.getUiField().views();
                    for (View v : views) {
                        String columnPath = v.column();
                        if (columnPath.contains(".")) {
                            String path = eruptModel.getEruptName() + "." + field.getName() + "." + columnPath.substring(0, columnPath.lastIndexOf("."));
                            if (!pathSet.contains(path)) {
                                hql.append(LEFT_JOIN).append(path);
                                pathSet.add(path);
                            }
                        } else {
                            hql.append(LEFT_JOIN).append(eruptModel.getEruptName()).append(".").append(field.getName()).append(AS).append(field.getName());
                        }
                    }
                }
            }
        });
        return hql.toString();
    }

    public static String geneEruptHqlCondition(BoxModel eruptModel, List<Condition> conditions, List<String> customCondition) {
        StringBuilder hql = new StringBuilder();
        hql.append(" where 1 = 1 ");
        //condition
        if (null != conditions) {
            for (Condition condition : conditions) {
                BoxFieldModel eruptFieldModel = eruptModel.getEruptFieldMap().get(condition.getKey());
                if (null != eruptFieldModel) {
                    Edit edit = eruptFieldModel.getUiField().edit();
                    if (edit.type() == EditType.REFERENCE_TREE) {
                        hql.append(BoxJpaUtils.AND).append(condition.getKey()).append(".").append(edit.referenceTreeType().id()).append("=:").append(condition.getKey());
                        continue;
                    } else if (edit.type() == EditType.REFERENCE_TABLE) {
                        hql.append(BoxJpaUtils.AND).append(condition.getKey()).append(".").append(edit.referenceTableType().id()).append("=:").append(condition.getKey());
                        continue;
                    }
                    String _key = BoxJpaUtils.completeHqlPath(eruptModel.getEruptName(), condition.getKey());

                    switch (condition.getExpression()) {
                        case EQ:
                            hql.append(BoxJpaUtils.AND).append(_key).append("=:").append(condition.getKey());
                            break;
                        case LIKE:
                            hql.append(BoxJpaUtils.AND).append(_key).append(" like :").append(condition.getKey());
                            break;
                        case RANGE:
                            hql.append(BoxJpaUtils.AND).append(_key).append(" between :")
                                    .append(L_VAL_KEY).append(condition.getKey()).append(" and :")
                                    .append(R_VAL_KEY).append(condition.getKey());
                            break;
                        case IN:
                            hql.append(BoxJpaUtils.AND).append(_key).append(" in (:").append(condition.getKey()).append(")");
                            break;
                    }
                } else {
                    hql.append(BoxJpaUtils.AND).append(condition.getKey()).append("=:").append(condition.getKey());
                }
            }
        }
        for (Filter filter : eruptModel.getBox().filter()) {
            hql.append(AND).append(filter.value());
        }
        Optional.ofNullable(customCondition).ifPresent(it -> it.forEach(str -> {
            if (StringUtils.isNotBlank(str)) hql.append(BoxJpaUtils.AND).append(str);
        }));
        return hql.toString();
    }

    public static String geneEruptHqlOrderBy(BoxModel eruptModel, String orderBy) {
        if (StringUtils.isNotBlank(orderBy)) {
            return " order by " + BoxJpaUtils.completeHqlPath(eruptModel.getEruptName(), orderBy);
        } else if (StringUtils.isNotBlank(eruptModel.getBox().orderBy())) {
            return " order by " + BoxJpaUtils.completeHqlPath(eruptModel.getEruptName(), eruptModel.getBox().orderBy());
        } else {
            return "";
        }
    }

    //在left join的情况下要求必须指定表信息，通过此方法生成；
    public static String completeHqlPath(String eruptName, String hqlPath) {
        if (hqlPath.contains(".")) {
            return hqlPath;
        } else {
            return eruptName + "." + hqlPath;
        }
    }

}
