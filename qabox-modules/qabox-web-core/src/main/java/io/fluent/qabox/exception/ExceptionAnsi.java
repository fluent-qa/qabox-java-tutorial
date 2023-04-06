package io.fluent.qabox.exception;

import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class ExceptionAnsi {

    public static BoxFieldAnnotationException styleEruptFieldException(BoxFieldModel boxFieldModel, String message) {
        return new BoxFieldAnnotationException(
                ansi().fg(Ansi.Color.RED).a(message).fg(Ansi.Color.BLUE)
                        .a("(" + boxFieldModel.getField().getDeclaringClass().getName() + "."
                                + boxFieldModel.getField().getName() + ")").reset().toString()
        );
    }

    public static BoxFieldAnnotationException styleEruptException(BoxModel boxModel, String message) {
        return new BoxFieldAnnotationException(
                ansi().fg(Ansi.Color.RED).a(message)
                        + ansi().fg(Ansi.Color.BLUE).
                        a("(" + boxModel.getClazz().getName() + ")").reset().toString()
        );
    }

}
