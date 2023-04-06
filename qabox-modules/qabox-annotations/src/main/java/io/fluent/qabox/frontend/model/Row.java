package io.fluent.qabox.frontend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class Row {

    private List<Column> columns;

    private String className;

}
