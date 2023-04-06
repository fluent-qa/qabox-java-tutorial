package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "upms_users")
@Box(
        name = "简单用户对象"
)
@EnableI18n
@Getter
@Setter
public class BoxUserVo extends BaseModel {

    @UIField(
            views = @View(title = "姓名", sortable = true),
            edit = @Edit(title = "姓名", notNull = true, search = @Search(vague = true))
    )
    private String name;

    public BoxUserVo() {
    }

    public BoxUserVo(Long id) {
        this.setId(id);
    }

}
