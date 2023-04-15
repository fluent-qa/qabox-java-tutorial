package io.fluentqa.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
public class NamedEntity {
  private String name;
  private String details;
}
