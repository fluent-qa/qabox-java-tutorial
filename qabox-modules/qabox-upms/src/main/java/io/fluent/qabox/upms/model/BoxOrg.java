package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTreeType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.Tree;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "upms_org", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Box(
        name = "组织维护",
        tree = @Tree(pid = "parentOrg.id", expandLevel = 5),
        orderBy = "EruptOrg.sort asc"
)
@EnableI18n
@Getter
@Setter
@NoArgsConstructor
public class BoxOrg extends BaseModel {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            views = @View(title = "组织编码", sortable = true),
            edit = @Edit(title = "组织编码", notNull = true, search = @Search(vague = true))
    )
    private String code;

    @UIField(
            views = @View(title = "组织名称", sortable = true),
            edit = @Edit(title = "组织名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @ManyToOne
    @UIField(
            edit = @Edit(
                    title = "上级组织",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(pid = "parentOrg.id", expandLevel = 3)
            )
    )
    private BoxOrg parentOrg;

    @UIField(
            edit = @Edit(
                    title = "显示顺序"
            )
    )
    private Integer sort;


}
