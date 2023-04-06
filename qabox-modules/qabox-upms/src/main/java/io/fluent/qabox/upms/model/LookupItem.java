package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.UIPermission;
import io.fluent.qabox.model.AuditableUpdateModelVo;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;



@Entity
@Table(name = "lookup_items",
  uniqueConstraints = @UniqueConstraint(columnNames = {"code", "lookup_id"}))
@Box(
        name = "字典项",
        orderBy = "sort",
//        linkTree = @LinkTree(dependNode = true, field = "eruptDict"),
        permission = @UIPermission(export = true)
)
@Getter
@Setter
@EnableI18n
public class LookupItem extends AuditableUpdateModelVo {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            views = @View(title = "编码", sortable = true),
            edit = @Edit(title = "编码", notNull = true, search = @Search)
    )
    private String code;

    @UIField(
            views = @View(title = "名称", sortable = true),
            edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @UIField(
            views = @View(title = "显示顺序", sortable = true),
            edit = @Edit(title = "显示顺序")
    )
    private Integer sort;

    @UIField(
            views = @View(title = "备注"),
            edit = @Edit(
                    title = "备注",
                    type = EditType.TEXTAREA
            )
    )
    private String remark;

    @ManyToOne
    @UIField
    @JoinColumn(name = "lookup_id")
    private LookupCode lookupCode;

}
