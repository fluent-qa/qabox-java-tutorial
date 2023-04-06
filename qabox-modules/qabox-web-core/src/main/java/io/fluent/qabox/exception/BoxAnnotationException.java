package io.fluent.qabox.exception;

import io.fluent.qabox.view.BoxModel;

public class BoxAnnotationException extends RuntimeException {

    public BoxAnnotationException(String message) {
        super(message);
    }

    public static void validateBoxInfo(BoxModel boxModel) {
        if (null == boxModel.getEruptFieldMap().get(boxModel.getBox().primaryKeyCol())) {
            throw ExceptionAnsi.styleEruptException(boxModel, "找不到主键,请确认主键列名是否为" + boxModel.getBox().primaryKeyCol() +
                    "，如果你不想将主键名定义为'" + boxModel.getBox().primaryKeyCol() + "'则可以修改@erupt->primaryKeyCol值解决此异常");
        }
    }

}
