package io.fluentqa.qabox.server.demo.handler;

import io.fluentqa.qabox.server.demo.model.complex.EruptRoleDemo;
import org.springframework.stereotype.Service;
import xyz.erupt.annotation.fun.DataProxy;
import xyz.erupt.annotation.query.Condition;
import xyz.erupt.jpa.dao.EruptDao;
import xyz.erupt.upms.model.EruptRole;
import xyz.erupt.upms.service.EruptUserService;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author YuePeng
 * date 2022/9/1 22:30
 */
@Service
public class EruptRoleDemoDataProxy implements DataProxy<EruptRoleDemo> {

    @Resource
    @Transient
    private EruptUserService eruptUserService;

    @Resource
    @Transient
    private EruptDao eruptDao;

    @Override
    public String beforeFetch(List<Condition> conditions) {
        if (eruptUserService.getCurrentEruptUser().getIsAdmin()) return null;
        return "EruptRole.createUser.id = " + eruptUserService.getCurrentUid();
    }

    @Override
    public void addBehavior(EruptRoleDemo eruptRole) {
        Integer max = (Integer) eruptDao.getEntityManager().createQuery("select max(sort) from " + EruptRole.class.getSimpleName()).getSingleResult();
        if (null == max) {
            eruptRole.setSort(10);
        } else {
            eruptRole.setSort(max + 10);
        }
    }

}
