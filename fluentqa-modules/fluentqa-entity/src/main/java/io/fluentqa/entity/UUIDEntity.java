package io.fluentqa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class UUIDEntity extends IdEntity {
  @GeneratedValue
  @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
  private UUID uuid;
}
