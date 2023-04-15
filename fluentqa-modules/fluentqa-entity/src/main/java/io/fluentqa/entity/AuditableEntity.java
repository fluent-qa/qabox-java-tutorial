package io.fluentqa.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class AuditableEntity extends IdEntity {

  private String createdBy;
  private LocalDateTime createTime = LocalDateTime.now();
  private String updateBy;
  private LocalDateTime updateTime = LocalDateTime.now();
}
