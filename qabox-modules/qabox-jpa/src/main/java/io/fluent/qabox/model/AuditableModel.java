package io.fluent.qabox.model;

import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.data.fun.PreDataProxy;
import io.fluent.qabox.proxy.AuditDataProxy;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@PreDataProxy(AuditDataProxy.class)
public class AuditableModel extends IdModel {

  @SmartSkipSerialize
  private String createBy;

  @SmartSkipSerialize
  private LocalDateTime createTime;

  @SmartSkipSerialize
  private String updateBy;

  @SmartSkipSerialize
  private LocalDateTime updateTime;

}
