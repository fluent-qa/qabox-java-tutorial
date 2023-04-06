package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTreeType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "upms_users")
@Box(
        name = "简单用户对象"
)
@EnableI18n
@Getter
@Setter
public class BoxUserPostVo extends BaseModel {

    @UIField(
            views = @View(title = "姓名", sortable = true),
            edit = @Edit(title = "姓名", notNull = true, search = @Search(vague = true))
    )
    private String name;

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

    public BoxUserPostVo() {
    }

    public BoxUserPostVo(Long id) {
        this.setId(id);
    }

}
