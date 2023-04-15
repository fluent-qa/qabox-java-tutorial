package io.fluent.qabox.service;

import io.fluent.qabox.config.constant.BoxConst;
import io.fluent.qabox.dao.BoxJpaDao;
import io.fluent.qabox.dao.BoxJpaUtils;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.query.ColumnQuery;
import io.fluent.qabox.support.JpaSupport;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author YuePeng
 * date 2019-03-06.
 */
@Service
public class BoxDataServiceDbImpl implements IBoxDataService {

    static {
        DataProcessorManager.register(BoxConst.DEFAULT_DATA_PROCESSOR, BoxDataServiceDbImpl.class);
    }

    @Resource
    private BoxJpaDao eruptJpaDao;

    @Resource
    private EntityManagerService entityManagerService;

    @Resource
    private JpaSupport jpaSupport;

    @Resource
    private I18NTranslateService i18NTranslateService;

    @Override
    public Object findDataById(BoxModel eruptModel, Object id) {
        return entityManagerService.getEntityManager(eruptModel.getClazz(), (em) -> em.find(eruptModel.getClazz(), id));
    }

    @Override
    public Page queryList(BoxModel eruptModel, Page page, BoxQuery query) {
        return eruptJpaDao.queryEruptList(eruptModel, page, query);
    }

    @Transactional
    @Override
    public void addData(BoxModel eruptModel, Object data) {
        try {
            this.loadSupport(data);
            this.jpaManyToOneConvert(eruptModel, data);
            eruptJpaDao.addEntity(eruptModel.getClazz(), data);
        } catch (Exception e) {
            handlerException(e, eruptModel);
        }
    }

    @Transactional
    @Override
    public void editData(BoxModel eruptModel, Object data) {
        try {
            this.loadSupport(data);
            eruptJpaDao.editEntity(eruptModel.getClazz(), data);
        } catch (Exception e) {
            handlerException(e, eruptModel);
        }
    }

    private void loadSupport(Object jpaEntity) {
        for (Field field : jpaEntity.getClass().getDeclaredFields()) {
            jpaSupport.referencedColumnNameSupport(jpaEntity, field);
        }
    }

    //优化异常提示类
    private void handlerException(Exception e, BoxModel eruptModel) {
        e.printStackTrace();
        if (e instanceof DataIntegrityViolationException) {
            if (e.getMessage().contains("ConstraintViolationException")) {
                throw new WebApiRuntimeException(gcRepeatHint(eruptModel));
            } else if (e.getMessage().contains("DataException")) {
                throw new WebApiRuntimeException(i18NTranslateService.translate("内容超出数据库限制长度"));
            } else {
                throw new WebApiRuntimeException(e.getMessage());
            }
        } else {
            throw new WebApiRuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteData(BoxModel eruptModel, Object object) {
        try {
            eruptJpaDao.removeEntity(eruptModel.getClazz(), object);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            e.printStackTrace();
            throw new WebApiRuntimeException(i18NTranslateService.translate("删除失败，可能存在关联数据，无法直接删除"));
        } catch (Exception e) {
            throw new WebApiRuntimeException(e.getMessage());
        }
    }

    //@ManyToOne数据处理
    private void jpaManyToOneConvert(BoxModel eruptModel, Object object) throws IllegalAccessException {
        for (BoxFieldModel fieldModel : eruptModel.getEruptFieldModels()) {
            if (fieldModel.getUiField().edit().type() == EditType.TAB_TABLE_ADD) {
                Field field = ReflectUtil.findClassField(object.getClass(), fieldModel.getFieldName());
                field.setAccessible(true);
                Collection<?> collection = (Collection<?>) field.get(object);
                if (null != collection) {
                    for (Object o : collection) {
                        //强制删除主键
                        ReflectUtil.findClassField(o.getClass(),
                                BoxCoreService.getBoxModel(fieldModel.getFieldReturnName()).getBox()
                                        .primaryKeyCol()).set(o, null);
                    }
                }
            }
        }
    }

    //生成数据重复的提示字符串
    private String gcRepeatHint(BoxModel eruptModel) {
        StringBuilder str = new StringBuilder();
        for (UniqueConstraint uniqueConstraint : eruptModel.getClazz().getAnnotation(Table.class).uniqueConstraints()) {
            for (String columnName : uniqueConstraint.columnNames()) {
                BoxFieldModel eruptFieldModel = eruptModel.getEruptFieldMap().get(columnName);
                if (null != eruptFieldModel) {
                    str.append(eruptFieldModel.getUiField().views()[0].title()).append("、");
                }
            }
        }
        String repeatTxt = i18NTranslateService.translate("数据重复");
        if (StringUtils.isNotBlank(str)) {
            return str.substring(0, str.length() - 1) + " " + repeatTxt;
        } else {
            return repeatTxt;
        }
    }

    /**
     * 根据列获取相关数据
     *
     * @param eruptModel eruptModel
     * @param columns    列
     * @param query      查询对象
     * @return 数据结果集
     */
    @Override
    public Collection<Map<String, Object>> queryColumn(BoxModel eruptModel, List<ColumnQuery> columns, BoxQuery query) {
        StringBuilder hql = new StringBuilder();
        List<String> columnStrList = new ArrayList<>();
        columns.forEach(column -> columnStrList.add(BoxJpaUtils.completeHqlPath(eruptModel.getEruptName()
                , column.getName()) + " as " + column.getAlias()));
        hql.append("select new map(").append(String.join(", ", columnStrList))
                .append(") from ").append(eruptModel.getEruptName()).append(" as ").append(eruptModel.getEruptName());
        ReflectUtil.findClassAllFields(eruptModel.getClazz(), field -> {
            if (null != field.getAnnotation(ManyToOne.class) || null != field.getAnnotation(OneToOne.class)) {
                hql.append(" left outer join ").append(eruptModel.getEruptName()).append(".")
                        .append(field.getName()).append(" as ").append(field.getName());
            }
        });
        hql.append(" where 1 = 1 ");
        Optional.ofNullable(query.getConditions()).ifPresent(c -> c.forEach(it -> hql.append(BoxJpaUtils.AND).append(it.getKey()).append('=').append(it.getValue())));
        Optional.ofNullable(query.getConditionStrings()).ifPresent(c -> c.forEach(it -> hql.append(BoxJpaUtils.AND).append(it)));
        Arrays.stream(eruptModel.getBox().filter()).map(Filter::value)
                .filter(StringUtils::isNotBlank).forEach(it -> hql.append(BoxJpaUtils.AND).append(it));
        if (StringUtils.isNotBlank(query.getOrderBy())) {
            hql.append(" order by ").append(query.getOrderBy());
        }
        return entityManagerService.getEntityManager(eruptModel.getClazz(), (em) -> em.createQuery(hql.toString()).getResultList());
    }

}
