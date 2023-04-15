package io.fluentqa.agile.entity;


import io.fluentqa.entity.NamedEntity;
import io.fluentqa.entity.ValidStatusEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "test_cases")
@Getter
@Setter
public class TestCase extends ValidStatusEntity {
  @Embedded
  private NamedEntity named;
  private String precondition;
  private String steps;
  private String expectedResult;

}
