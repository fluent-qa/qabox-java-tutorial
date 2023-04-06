package io.fluent.qabox.support;

import io.fluent.qabox.UIField;
import io.fluent.qabox.annotation.DataProcessor;
import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.util.internal.ReflectUtil;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;


import java.lang.reflect.Field;

@Component
public class JpaSupport {

    @Resource
    private BoxDao eruptDao;

    /**
     * 对jpa @JoinColumn提供的referencedColumnName配置实现适配
     */
    @SneakyThrows
    public void referencedColumnNameSupport(Object obj, Field field) {
      UIField eruptField = field.getAnnotation(UIField.class);
        if (null != eruptField) {
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null != joinColumn && !"".equals(joinColumn.referencedColumnName())) {
                String id;
                if (eruptField.edit().type() == EditType.REFERENCE_TREE) {
                    id = eruptField.edit().referenceTreeType().id();
                } else if (eruptField.edit().type() == EditType.REFERENCE_TABLE) {
                    id = eruptField.edit().referenceTableType().id();
                } else {
                    return;
                }
                field.setAccessible(true);
                Object refObject = field.get(obj);
                if (null != refObject) {
                    Field idField = ReflectUtil.findClassField(refObject.getClass(), id);
                    idField.setAccessible(true);
                    EntityManager em = eruptDao.getEntityManager();
                    DataProcessor eruptDataSource = refObject.getClass().getAnnotation(DataProcessor.class);
                    if (eruptDataSource != null) {
                        em = eruptDao.getEntityManager(eruptDataSource.value());
                    }
                    Object result = em.createQuery("from " + refObject.getClass().getSimpleName() + " I where I.id = :id")
                            .setParameter("id", idField.get(refObject)).getSingleResult();
                    em.close();
                    field.set(obj, result);
                }
            }
        }

    }

}