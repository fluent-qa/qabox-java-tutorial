package io.fluentqa.oneserver.entity;

import java.io.Serializable;

public interface Identifiable<ID> extends Serializable {

    ID getId();
}
