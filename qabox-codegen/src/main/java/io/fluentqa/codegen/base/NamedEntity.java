package io.fluentqa.codegen.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NamedEntity extends AuditableEntity {
  private String name;
  private String detail;
}
