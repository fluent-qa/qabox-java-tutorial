package io.fluentqa.codegen.base;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class IdEntity  {
  private Integer id;
}
