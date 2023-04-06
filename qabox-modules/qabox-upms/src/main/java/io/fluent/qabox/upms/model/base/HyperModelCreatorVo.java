package io.fluent.qabox.upms.model.base;

import io.fluent.qabox.UIField;
import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.field.Readonly;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import io.fluent.qabox.upms.model.BoxUserVo;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
public class HyperModelCreatorVo extends HyperModel {

    @ManyToOne
    @UIField(
            views = @View(title = "创建人", width = "100px", column = "name"),
            edit = @Edit(title = "创建人", readonly = @Readonly, type = EditType.REFERENCE_TABLE)
    )
    @SmartSkipSerialize
    private BoxUserVo createUser;

    @UIField(
            views = @View(title = "创建时间", sortable = true),
            edit = @Edit(title = "创建时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
    )
    @SmartSkipSerialize
    private Date createTime;
}