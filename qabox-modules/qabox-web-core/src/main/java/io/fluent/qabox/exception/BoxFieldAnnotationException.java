package io.fluent.qabox.exception;


import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.view.BoxFieldModel;


public class BoxFieldAnnotationException extends RuntimeException {

    public BoxFieldAnnotationException(String message) {
        super(message);
    }

    public static void validateEruptFieldInfo(BoxFieldModel boxFieldModel) {
        Edit edit = boxFieldModel.getUiField().edit();
        switch (edit.type()) {
            case REFERENCE_TREE:
            case REFERENCE_TABLE:
                if (boxFieldModel.getUiField().views().length > 0) {
                    for (View view : boxFieldModel.getUiField().views()) {
                        if ("".equals(view.column())) {
                            throw ExceptionAnsi.styleEruptFieldException(boxFieldModel, "@View注解修饰复杂对象，'" + view.title() + "' 必须配置column值");
                        }
                    }
                }
                break;
        }
    }
}
