package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.BoolType;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTreeType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "upms_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "account")
})
@Box(
        name = "用户配置"
)
@EnableI18n
@Getter
@Setter
public class BoxUserByRoleView extends BaseModel {

    @UIField(
            views = @View(title = "用户名", sortable = true),
            edit = @Edit(title = "用户名", desc = "登录用户名", notNull = true, search = @Search(vague = true))
    )
    private String account;

    @UIField(
            views = @View(title = "姓名", sortable = true),
            edit = @Edit(title = "姓名", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @UIField(
            views = @View(title = "账户状态", sortable = true),
            edit = @Edit(
                    title = "账户状态",
                    search = @Search,
                    type = EditType.BOOLEAN,
                    notNull = true,
                    boolType = @BoolType(
                            trueText = "激活",
                            falseText = "锁定"
                    )
            )
    )
    private Boolean status = true;

    @UIField(
            views = @View(title = "超管用户", sortable = true),
            edit = @Edit(
                    title = "超管用户", notNull = true
            )
    )
    private Boolean isAdmin = false;

    @ManyToOne
    @UIField(
            views = @View(title = "所属组织", column = "name"),
            edit = @Edit(title = "所属组织", type = EditType.REFERENCE_TREE, referenceTreeType = @ReferenceTreeType(pid = "parentOrg.id"))
    )
    private BoxOrg boxOrg;

    @ManyToOne
    @UIField(
            views = @View(title = "岗位", column = "name"),
            edit = @Edit(title = "岗位", type = EditType.REFERENCE_TREE)
    )
    private BoxPosition boxPosition;

}
