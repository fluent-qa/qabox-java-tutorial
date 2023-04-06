package io.fluent.qabox.model;

import io.fluent.qabox.UIField;
import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.field.Readonly;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class AuditableModelVo extends AuditableModel {

  @Transient
  @UIField(
    edit = @Edit(title = "数据审计", type = EditType.DIVIDE)
  )
  @SmartSkipSerialize
  private String divide;

  @UIField(
    views = @View(title = "创建人", width = "100px"),
    edit = @Edit(title = "创建人", readonly = @Readonly)
  )
  @SmartSkipSerialize
  private String createBy;

  @UIField(
    views = @View(title = "创建时间", sortable = true),
    edit = @Edit(title = "创建时间", readonly = @Readonly,
      dateType = @DateType(type = DateType.Type.DATE_TIME))
  )
  @SmartSkipSerialize
  private LocalDateTime createTime;

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
