package io.fluent.qabox.model;

import io.fluent.qabox.UIField;
import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.Readonly;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
public class AuditableUpdateModelVo extends AuditableModel {

  @UIField(
    views = @View(title = "更新人", width = "100px"),
    edit = @Edit(title = "更新人", readonly = @Readonly)
  )
  @SmartSkipSerialize
  private String updateBy;

  @UIField(
    views = @View(title = "更新时间", sortable = true),
    edit = @Edit(title = "更新时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
  )
  @SmartSkipSerialize
  private LocalDateTime updateTime;

}