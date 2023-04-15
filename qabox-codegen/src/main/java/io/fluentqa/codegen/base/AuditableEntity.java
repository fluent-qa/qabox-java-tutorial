package io.fluentqa.codegen.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class AuditableEntity extends IdEntity {
   private String createdBy;
   private String updatedBy;
   private LocalDate createdDate;
   private LocalDate updatedDate;
}
