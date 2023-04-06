package io.fluent.qabox.upms.model.keeper;

import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.data.fun.PreDataProxy;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.upms.model.base.HyperModelCreatorVo;
import io.fluent.qabox.upms.service.BoxUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;


@MappedSuperclass
@PreDataProxy(Keeper.Comp.class)
public class Keeper extends HyperModelCreatorVo implements DataProxy<Void> {

    @Component
    static class Comp implements DataProxy<Void> {

        @Resource
        @Transient
        private BoxUserService boxUserService;

        @Override
        public String beforeFetch(List<Condition> conditions) {
            if (boxUserService.getCurrentEruptUser().getIsAdmin()) return null;
            return UserMenuContext.getMenuContext().getName() + ".createUser.id = " + boxUserService.getCurrentUid();
        }

    }

}
