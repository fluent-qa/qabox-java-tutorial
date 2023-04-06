package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.Drill;
import io.fluent.qabox.frontend.operation.Link;
import io.fluent.qabox.frontend.operation.UIPermission;
import io.fluent.qabox.model.AuditableUpdateModelVo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "lookup_codes", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@EnableI18n
@Box(
        name = "数据字典",
        permission = @UIPermission(export = true),
        drills = @Drill(
                title = "字典项",
                link = @Link(
                        linkErupt = LookupItem.class, joinColumn = "eruptDict.id"
                )
        )
)
@Getter
@Setter
public class LookupCode extends AuditableUpdateModelVo {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            views = @View(title = "编码", sortable = true),
            edit = @Edit(title = "编码", notNull = true, search = @Search(vague = true))
    )
    private String code;

    @UIField(
            views = @View(title = "名称", sortable = true),
            edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @UIField(
            views = @View(title = "备注"),
            edit = @Edit(
                    title = "备注"
            )
    )
    private String remark;

}
