package io.fluent.qabox.upms.model.keeper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fluent.qabox.exception.WebApiRuntimeException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;
import io.fluent.qabox.UIField;
import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.data.fun.PreDataProxy;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import io.fluent.qabox.model.BaseModel;
import io.fluent.qabox.service.I18NTranslateService;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.model.BoxUserPostVo;
import io.fluent.qabox.upms.service.BoxUserService;


@MappedSuperclass
@PreDataProxy(KeeperPositionLevel.Comp.class)
@Getter
@Setter
public class KeeperPositionLevel extends BaseModel {

    @ManyToOne
    @UIField(
            views = {
                    @View(title = "创建人", column = "name"),
                    @View(title = "所属组织", column = "eruptOrg.name"),
                    @View(title = "岗位", column = "eruptPost.name"),
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @SmartSkipSerialize
    private BoxUser updateUser;

    @Component
    static class Comp implements DataProxy<KeeperPositionLevel> {

        @Resource
        private BoxUserService boxUserService;

        @Resource
        private I18NTranslateService i18NTranslateService;

        @Override
        public String beforeFetch(List<Condition> conditions) {
            BoxUser boxUser = boxUserService.getCurrentEruptUser();
            if (boxUser.getIsAdmin()) return null;
            if (null == boxUser.getBoxOrg() || null == boxUser.getBoxPosition()) {
                throw new WebApiRuntimeException(boxUser.getName() + " " + i18NTranslateService.translate("未绑定的岗位无法查看数据"));
            }
            String eruptName = UserMenuContext.getMenuContext().getName();
            return "(" + eruptName + ".createUser.id = " + boxUserService.getCurrentUid()
                    + " or " + eruptName + ".createUser.eruptOrg.id = " + boxUser.getBoxOrg().getId() + " and "
                    + eruptName + ".createUser.eruptPost.weight < " + boxUser.getBoxPosition().getWeight() + ")";
        }

        @Override
        public void beforeAdd(KeeperPositionLevel keeperPositionLevel) {
            keeperPositionLevel.setCreateTime(new Date());
            keeperPositionLevel.setCreateUser(new BoxUserPostVo(boxUserService.getCurrentUid()));
        }

        @Override
        public void beforeUpdate(KeeperPositionLevel keeperPositionLevel) {
            keeperPositionLevel.setUpdateTime(new Date());
            keeperPositionLevel.setUpdateUser(new BoxUser(boxUserService.getCurrentUid()));
        }
    }


}