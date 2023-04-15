package io.fluentqa.entity;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public class ValidStatusEntity extends AuditableEntity{
  private boolean valid;
  private String status;
}
