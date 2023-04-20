package io.fluentqa.codegen.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;



@Data
@MappedSuperclass
public class IdEntity  {
  private Integer id;
}
