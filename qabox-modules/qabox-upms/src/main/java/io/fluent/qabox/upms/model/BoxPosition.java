package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "upms_positions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
@Box(name = "岗位维护", orderBy = "weight desc")
@EnableI18n
@Getter
@Setter
public class BoxPosition extends BaseModel {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            views = @View(title = "岗位编码", sortable = true),
            edit = @Edit(title = "岗位编码", notNull = true, search = @Search(vague = true))
    )
    private String code;

    @UIField(
            views = @View(title = "岗位名称", sortable = true),
            edit = @Edit(title = "岗位名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @UIField(
            views = @View(title = "岗位权重"),
            edit = @Edit(title = "岗位权重", desc = "数值越高，岗位级别越高")
    )
    private Integer weight;

}
