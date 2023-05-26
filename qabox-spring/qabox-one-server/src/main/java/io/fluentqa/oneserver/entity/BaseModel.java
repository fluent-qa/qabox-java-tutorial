package io.fluentqa.oneserver.entity;


import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
public abstract class BaseModel<ID> implements Identifiable<ID> {

}
