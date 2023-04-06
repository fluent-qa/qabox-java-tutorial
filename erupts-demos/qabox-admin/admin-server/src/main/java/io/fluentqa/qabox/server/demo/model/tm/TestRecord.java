package io.fluentqa.qabox.server.demo.model.tm;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.EruptI18n;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "test_records")
@Erupt(
  name = "测试记录"
)
@EruptI18n
@Getter
@Setter
@Component
public class TestRecord extends TestCase {

  @ManyToOne(fetch = FetchType.EAGER)
  @EruptField(
    views = @View(title = "所属需求", column = "name"),
    edit = @Edit(title = "所属需求",
      type = EditType.REFERENCE_TREE
      )
  )
  private TestRequirement testRequirement;

  @EruptField(
    views = @View(title = "测试状态", column = "status"),
    edit = @Edit(title = "测试状态")
  )
  private String status;
}
