package io.fluent.qabox.query;

import io.fluent.qabox.data.query.Condition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BoxQuery {
    List<Condition> conditions;

    List<String> conditionStrings;

    String orderBy;
}
