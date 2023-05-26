package io.fluentqa.oneserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.fluentqa.oneserver.base.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * @author csieflyman
 */
@Setter
@Getter
@MappedSuperclass
public abstract class EntityModel<ID> extends BaseModel<ID> {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimeConstants.DATE_TIME_PATTERN, timezone = Constants.DateTimeConstants.UTC_ZONE)
  @Column(updatable = false, insertable = false, columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP")
  protected Instant createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimeConstants.DATE_TIME_PATTERN, timezone = Constants.DateTimeConstants.UTC_ZONE)
  @Column(updatable = false, insertable = false, columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  protected Instant updatedAt;
}
