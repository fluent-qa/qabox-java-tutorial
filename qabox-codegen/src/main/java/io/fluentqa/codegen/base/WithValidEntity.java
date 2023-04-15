package io.fluentqa.codegen.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithValidEntity extends AuditableEntity {
  private boolean valid;
}
