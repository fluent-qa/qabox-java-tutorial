package io.fluent.qabox.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnQuery {
    String name;

    String alias;

    public ColumnQuery(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

}
