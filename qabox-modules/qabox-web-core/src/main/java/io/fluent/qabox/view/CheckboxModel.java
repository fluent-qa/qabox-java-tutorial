package io.fluent.qabox.view;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CheckboxModel {

    private Object id;

    private Object label;

    public CheckboxModel(Object id, Object label) {
        this.id = id;
        this.label = label;
    }

    public CheckboxModel() {
    }
}
