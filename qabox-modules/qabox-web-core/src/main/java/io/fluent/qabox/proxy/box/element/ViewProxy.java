package io.fluent.qabox.proxy.box.element;

import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.constant.JavaType;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.AttachmentType;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.proxy.box.BoxProxyContext;
import io.fluent.qabox.util.BoxUtil;
import org.aopalliance.intercept.MethodInvocation;


public class ViewProxy extends AnnotationProxy<View, UIField> {

    @Override
    protected Object invocation(MethodInvocation invocation) {
        if ("type".equals(invocation.getMethod().getName())) {
            if (ViewType.AUTO == this.rawAnnotation.type()) {
                Edit edit = this.parent.proxyAnnotation.edit();
                if (!AnnotationConst.EMPTY_STR.equals(edit.title())) {
                    switch (edit.type()) {
                        case ATTACHMENT:
                            if (edit.attachmentType().type() == AttachmentType.Type.IMAGE) {
                                return ViewType.IMAGE;
                            } else {
                                return ViewType.ATTACHMENT;
                            }
                        case CHOICE:
                            return ViewType.TEXT;
                        case DATE:
                            if (edit.dateType().type() == DateType.Type.DATE_TIME) {
                                return ViewType.DATE_TIME;
                            } else {
                                return ViewType.DATE;
                            }
                        case HTML_EDITOR:
                            return ViewType.HTML;
                        case CODE_EDITOR:
                            return ViewType.CODE;
                        case MAP:
                            return ViewType.MAP;
                        case TAB_TABLE_ADD:
                        case TAB_TREE:
                        case TAB_TABLE_REFER:
                        case CHECKBOX:
                            return ViewType.TAB_VIEW;
                    }
                }
                String returnType = BoxProxyContext.get().getField().getType().getSimpleName();
                if (boolean.class.getSimpleName().equalsIgnoreCase(returnType.toLowerCase())) {
                    return ViewType.BOOLEAN;
                } else if (BoxUtil.isDateField(returnType)) {
                    return ViewType.DATE;
                } else if (JavaType.NUMBER.equals(returnType)) {
                    return ViewType.NUMBER;
                }
                return ViewType.TEXT;
            }
        }
        return this.invoke(invocation);
    }

}
