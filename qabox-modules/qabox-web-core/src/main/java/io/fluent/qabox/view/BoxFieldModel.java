package io.fluent.qabox.view;


import com.google.gson.JsonObject;
import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.JavaType;
import io.fluent.qabox.exception.BoxFieldAnnotationException;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.fun.VLModel;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.proxy.box.BoxProxyContext;
import io.fluent.qabox.proxy.box.UiFieldProxy;
import io.fluent.qabox.util.AnnotationUtil;
import io.fluent.qabox.util.CloneSupport;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.util.internal.TypeUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;


@Getter
@Setter
public class BoxFieldModel extends CloneSupport<BoxFieldModel> {

    private transient UIField uiField;

    private transient Field field;

    private transient String fieldReturnName;

    private transient AnnotationProxy<UIField, Void> fieldProxy = new UiFieldProxy();

    private String fieldName;

    private JsonObject eruptFieldJson;

    private Object value;

    //仅前端使用
    private List<VLModel> choiceList;

    //仅前端使用
    private List<String> tagList;

    public BoxFieldModel(Field field) {
        this.field = field;
        this.uiField = field.getAnnotation(UIField.class);
        Edit edit = uiField.edit();
        this.fieldName = field.getName();
        //数字类型转换
        if (TypeUtil.isNumberType(field.getType().getSimpleName())) {
            this.fieldReturnName = JavaType.NUMBER;
        } else {
            this.fieldReturnName = field.getType().getSimpleName();
        }
        switch (edit.type()) {
            //如果是Tab类型视图，数据必须为一对多关系管理，需要用泛型集合来存放，所以取出泛型的名称重新赋值到fieldReturnName中
            case TAB_TREE:
            case TAB_TABLE_ADD:
            case TAB_TABLE_REFER:
            case CHECKBOX:
                this.fieldReturnName = ReflectUtil.getFieldGenericName(field).get(0);
                break;
        }
        this.uiField = fieldProxy.newProxy(this.getUiField(), null);
        //校验注解的正确性
        BoxFieldAnnotationException.validateEruptFieldInfo(this);
    }

    public UIField getUiField() {
        BoxProxyContext.set(field);
        return uiField;
    }

    public void serializable() {
        this.eruptFieldJson = AnnotationUtil.annotationToJsonByReflect(this.getUiField());
    }

}
