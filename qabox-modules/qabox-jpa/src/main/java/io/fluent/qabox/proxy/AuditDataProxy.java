package io.fluent.qabox.proxy;


import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.model.AuditableModel;

import java.time.LocalDateTime;

public class AuditDataProxy implements DataProxy<AuditableModel> {

    @Override
    public void beforeAdd(AuditableModel metaModel) {
        metaModel.setCreateTime(LocalDateTime.now());
        metaModel.setCreateBy(UserMenuContext.getUser().getName());
        metaModel.setUpdateTime(metaModel.getCreateTime());
        metaModel.setUpdateBy(metaModel.getCreateBy());
    }

    @Override
    public void beforeUpdate(AuditableModel metaModel) {
        metaModel.setUpdateTime(LocalDateTime.now());
        metaModel.setUpdateBy(UserMenuContext.getUser().getName());
    }

}
