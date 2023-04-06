package io.fluentqa.qabox.server.demo.model.tm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.EruptI18n;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "test_requirements")
@Erupt(
  name = "测试需求"
)
@EruptI18n

public class TestRequirement extends BaseModel {
  @EruptField(
    views = @View(title = "名称", sortable = true),
    edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
  )
  private String name;

  @EruptField(
    views = @View(title = "需求类型", sortable = true),
    edit = @Edit(title = "需求类型", notNull = true, search = @Search(vague = true))
  )
  private String requirementType;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRequirementType() {
    return requirementType;
  }

  public void setRequirementType(String requirementType) {
    this.requirementType = requirementType;
  }

  //TODO: add to test plan as tree view
}
