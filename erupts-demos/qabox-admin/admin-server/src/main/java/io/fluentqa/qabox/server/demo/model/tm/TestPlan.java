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
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "test_plans")
@Erupt(
  name = "测试计划"
)
@EruptI18n
public class TestPlan extends BaseModel {
  @EruptField(
    views = @View(title = "名称", sortable = true),
    edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
  )
  private String name;

  @JoinTable(name = "test_plan_requirement_xref",
    joinColumns = @JoinColumn(name = "test_plan_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "test_requirement_id", referencedColumnName = "id"))
  @ManyToMany(fetch = FetchType.EAGER)
  @EruptField(
    views = @View(title = "添加需求"),
    edit = @Edit(title = "添加测试需求", type = EditType.TAB_TABLE_REFER)
  )
  private Set<TestRequirement> testRequirements;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<TestRequirement> getTestRequirements() {
    return testRequirements;
  }

  public void setTestRequirements(Set<TestRequirement> testRequirements) {
    this.testRequirements = testRequirements;
  }
}
