package io.fluentqa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.Nullable;


@Getter
@Setter
@MappedSuperclass
public class IdEntity  {
  @Id
  @GeneratedValue
  private Long id;
}
