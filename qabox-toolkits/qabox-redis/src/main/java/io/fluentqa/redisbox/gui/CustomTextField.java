package io.fluentqa.redisbox.gui;

import javafx.beans.NamedArg;
import javafx.scene.control.TextField;

public class CustomTextField extends TextField{
    public CustomTextField(@NamedArg("validator") TextFieldValidator validator) {
        this.setTextFormatter(validator.getFormatter());
    }
}