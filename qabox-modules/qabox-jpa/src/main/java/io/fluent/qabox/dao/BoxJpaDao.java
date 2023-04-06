package io.fluent.qabox.dao;

import io.fluent.qabox.annotation.DataProcessor;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.service.EntityManagerService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.Page;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class BoxJpaDao {

    @Resource
    private EntityManagerService entityManagerService;

    public void addEntity(Class<?> eruptClass, Object entity) {
        entityManagerService.entityManagerTran(eruptClass, (em) -> {
            em.persist(entity);
            em.flush();
        });
    }

    public void editEntity(Class<?> eruptClass, Object entity) {
        entityManagerService.entityManagerTran(eruptClass, (em) -> {
            em.merge(entity);
            em.flush();
        });
    }

    public void removeEntity(Class<?> eruptClass, Object entity) {
        entityManagerService.entityManagerTran(eruptClass, (em) -> {
            DataProcessor eruptDataSource = eruptClass.getAnnotation(DataProcessor.class);
            if (null == eruptDataSource) {
                em.remove(entity);
            } else {
                em.remove(em.merge(entity));
            }
            em.flush();
        });
    }

    public Page queryEruptList(BoxModel eruptModel, Page page, BoxQuery eruptQuery) {
        String hql = BoxJpaUtils.generateEruptJpaHql(eruptModel, "new map(" + String.join(",", BoxJpaUtils.getEruptColJpaKeys(eruptModel)) + ")", eruptQuery, false);
        String countHql = BoxJpaUtils.generateEruptJpaHql(eruptModel, "count(*)", eruptQuery, true);
        return entityManagerService.getEntityManager(eruptModel.getClazz(), entityManager -> {
            Query query = entityManager.createQuery(hql);
            Query countQuery = entityManager.createQuery(countHql);
            Map<String, BoxFieldModel> eruptFieldMap = eruptModel.getEruptFieldMap();
            if (null != eruptQuery.getConditions()) {
                for (Condition condition : eruptQuery.getConditions()) {
                  BoxFieldModel eruptFieldModel = eruptFieldMap.get(condition.getKey());
                    switch (condition.getExpression()) {
                        case EQ:
                            countQuery.setParameter(condition.getKey(), BoxUtil.convertObjectType(eruptFieldModel, condition.getValue()));
                            query.setParameter(condition.getKey(), BoxUtil.convertObjectType(eruptFieldModel, condition.getValue()));
                            break;
                        case LIKE:
                            countQuery.setParameter(condition.getKey(), BoxJpaUtils.PERCENT + condition.getValue() + BoxJpaUtils.PERCENT);
                            query.setParameter(condition.getKey(), BoxJpaUtils.PERCENT + condition.getValue() + BoxJpaUtils.PERCENT);
                            break;
                        case RANGE:
                            List<?> list = (List<?>) condition.getValue();
                            countQuery.setParameter(BoxJpaUtils.L_VAL_KEY + condition.getKey(), BoxUtil.convertObjectType(eruptFieldModel, list.get(0)));
                            countQuery.setParameter(BoxJpaUtils.R_VAL_KEY + condition.getKey(), BoxUtil.convertObjectType(eruptFieldModel, list.get(1)));
                            query.setParameter(BoxJpaUtils.L_VAL_KEY + condition.getKey(), BoxUtil.convertObjectType(eruptFieldModel, list.get(0)));
                            query.setParameter(BoxJpaUtils.R_VAL_KEY + condition.getKey(), BoxUtil.convertObjectType(eruptFieldModel, list.get(1)));
                            break;
                        case IN:
                            List<Object> listIn = new ArrayList<>();
                            for (Object o : (List<?>) condition.getValue()) {
                                listIn.add(BoxUtil.convertObjectType(eruptFieldModel, o));
                            }
                            countQuery.setParameter(condition.getKey(), listIn);
                            query.setParameter(condition.getKey(), listIn);
                            break;
                    }
                }
            }
            page.setTotal((Long) countQuery.getSingleResult());
            if (page.getTotal() > 0) {
                page.setList(query.setMaxResults(page.getPageSize()).setFirstResult((page.getPageIndex() - 1) * page.getPageSize()).getResultList());
            } else {
                page.setList(new ArrayList<>(0));
            }
            return page;
        });
    }

}
