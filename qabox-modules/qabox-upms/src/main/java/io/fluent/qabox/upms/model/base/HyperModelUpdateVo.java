package io.fluent.qabox.upms.model.base;

import io.fluent.qabox.UIField;
import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.frontend.field.*;
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
public class HyperModelUpdateVo extends HyperModel {

    @ManyToOne
    @UIField(
            views = @View(title = "更新人", width = "100px", column = "name"),
            edit = @Edit(title = "更新人", readonly = @Readonly, type = EditType.REFERENCE_TABLE)
    )
    @SmartSkipSerialize
    private BoxUserVo updateUser;

    @UIField(
            views = @View(title = "更新时间", sortable = true),
            edit = @Edit(title = "更新时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
    )
    @SmartSkipSerialize
    private Date updateTime;

}