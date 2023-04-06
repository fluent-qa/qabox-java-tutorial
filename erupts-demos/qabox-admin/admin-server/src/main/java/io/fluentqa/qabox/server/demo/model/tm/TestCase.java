package io.fluentqa.qabox.server.demo.model.tm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.EruptI18n;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "test_cases")
@Erupt(
  name = "测试用例"
)
@EruptI18n
@Getter
@Setter
public class TestCase extends BaseModel {

  @EruptField(
    views = @View(title = "名称", sortable = true),
    edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
  )
  private String name;

  @EruptField(
    views = @View(title = "需求", sortable = true),
    edit = @Edit(title = "需求", notNull = true, search = @Search(vague = true))
  )
  private String requirements;

}
