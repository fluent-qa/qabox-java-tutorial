package io.fluent.qabox.upms.model.keeper;

import io.fluent.qabox.UIField;
import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.data.fun.PreDataProxy;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import io.fluent.qabox.model.BaseModel;
import io.fluent.qabox.service.I18NTranslateService;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.model.BoxUserPostVo;
import io.fluent.qabox.upms.service.BoxUserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;


@MappedSuperclass
@PreDataProxy(KeeperOrg.Comp.class)
@Getter
@Setter
public class KeeperOrg extends BaseModel {

    @ManyToOne
    @UIField(
            views = {
                    @View(title = "创建人", column = "name"),
                    @View(title = "所属组织", column = "eruptOrg.name")
            },
            edit = @Edit(title = "创建人", readonly = @Readonly, type = EditType.REFERENCE_TABLE)
    )
    @SmartSkipSerialize
    private BoxUserPostVo createUser;

    @UIField(
            views = @View(title = "创建时间", sortable = true),
            edit = @Edit(title = "创建时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
    )
    @SmartSkipSerialize
    private Date createTime;

    @SmartSkipSerialize
    private Date updateTime;

    @SmartSkipSerialize
    @ManyToOne
    private BoxUserPostVo updateUser;

    @Component
    static class Comp implements DataProxy<KeeperOrg> {

        @Resource
        private BoxUserService boxUserService;

        @Resource
        private I18NTranslateService i18NTranslateService;

        @Override
        public String beforeFetch(List<Condition> conditions) {
            BoxUser boxUser = boxUserService.getCurrentEruptUser();
            if (boxUser.getIsAdmin()) return null;
            if (null == boxUser.getBoxOrg()) {
                throw new WebApiRuntimeException(boxUser.getName() + " " + i18NTranslateService.translate("未绑定的组织无法查看数据"));
            } else {
                return UserMenuContext.getMenuContext().getName() + ".createUser.eruptOrg.id = " + boxUser.getBoxOrg().getId();
            }
        }

        @Override
        public void beforeAdd(KeeperOrg keeperOrg) {
            keeperOrg.setCreateTime(new Date());
            keeperOrg.setCreateUser(new BoxUserPostVo(boxUserService.getCurrentUid()));
        }

        @Override
        public void beforeUpdate(KeeperOrg keeperOrg) {
            keeperOrg.setUpdateTime(new Date());
            keeperOrg.setUpdateUser(new BoxUserPostVo(boxUserService.getCurrentUid()));
        }

    }

}
