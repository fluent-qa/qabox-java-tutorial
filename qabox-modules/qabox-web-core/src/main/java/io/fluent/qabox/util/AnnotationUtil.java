package io.fluent.qabox.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.fluent.qabox.config.*;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.field.EditTypeMapping;
import io.fluent.qabox.frontend.field.EditTypeSearch;
import io.fluent.qabox.util.internal.TypeUtil;
import lombok.SneakyThrows;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;


public class AnnotationUtil {

    private static final String[] ANNOTATION_NUMBER_TYPE = {"short", "int", "long", "float", "double"};

    private static final String[] ANNOTATION_STRING_TYPE = {"String", "byte", "char"};

    private static final ExpressionParser parser = new SpelExpressionParser();

    private static final String EMPTY_ARRAY = "[]";

    private static final String VALUE_VAR = "value";

    private static final String ITEM_VAR = "item";

    @SneakyThrows
    public static JsonObject annotationToJsonByReflect(Annotation annotation) {
        JsonObject jsonObject = new JsonObject();
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            Transient tran = method.getAnnotation(Transient.class);
            if (null != tran && tran.value()) {
                continue;
            }
            String methodName = method.getName();
            QProperty boxProp = method.getAnnotation(QProperty.class);
            if (null != boxProp && !AnnotationConst.EMPTY_STR.equals(boxProp.alias())) {
                methodName = boxProp.alias();
            }
            String returnType = method.getReturnType().getSimpleName();
            Object result = method.invoke(annotation);
            Match match = method.getAnnotation(Match.class);
            if (null != match) {
                EvaluationContext evaluationContext = new StandardEvaluationContext();
                evaluationContext.setVariable(VALUE_VAR, result);
                evaluationContext.setVariable(ITEM_VAR, annotation);
                Object r = parser.parseExpression(match.value()).getValue(evaluationContext);
                if (null == r || !(Boolean) r) {
                    continue;
                }
            }
            if (returnType.endsWith(EMPTY_ARRAY)) {
                returnType = returnType.substring(0, returnType.length() - 2);
                JsonArray jsonArray = new JsonArray();
                ToMap toMap = method.getAnnotation(ToMap.class);
                JsonObject jsonMap = new JsonObject();
                //基本类型无法强转成Object类型数组，所以使用下面的方法进行处理
                if (Arrays.asList(ANNOTATION_NUMBER_TYPE).contains(returnType)) {
                    TypeUtil.simpleNumberTypeArrayToObject(result, returnType, jsonArray::add);
                } else {
                    for (Object res : (Object[]) result) {
                        if (String.class.getSimpleName().equals(returnType)) {
                            jsonArray.add(res.toString());
                        } else if (char.class.getSimpleName().equals(returnType)) {
                            jsonArray.add((Character) res);
                        } else if (byte.class.getSimpleName().equals(returnType)) {
                            jsonArray.add((Character) res);
                        } else if (boolean.class.getSimpleName().equals(returnType)) {
                            jsonArray.add((Boolean) res);
                        } else if (Class.class.getSimpleName().equals(returnType)) {
                            jsonArray.add(((Class<?>) res).getSimpleName());
                        } else if (res.getClass().isEnum()) {
                            jsonArray.add(res.toString());
                        } else {
                            Annotation ann = (Annotation) res;
                            if (null != toMap) {
                                JsonObject jo = annotationToJsonByReflect((Annotation) res);
                                String key = ann.annotationType().getMethod(toMap.key()).invoke(res).toString();
                                jo.remove(toMap.key());
                                jsonMap.add(key, jo);
                            } else {
                                jsonArray.add(annotationToJsonByReflect(ann));
                            }
                        }
                    }
                }
                if (null == toMap) {
                    jsonObject.add(methodName, jsonArray);
                } else {
                    if (jsonMap.size() > 0) {
                        jsonObject.add(methodName, jsonMap);
                    }
                }
            } else {
                if (Arrays.asList(ANNOTATION_STRING_TYPE).contains(returnType)) {
                    jsonObject.addProperty(methodName, result.toString());
                } else if (Arrays.asList(ANNOTATION_NUMBER_TYPE).contains(returnType)) {
                    jsonObject.addProperty(methodName, (Number) result);
                } else if (boolean.class.getSimpleName().equals(returnType)) {
                    jsonObject.addProperty(methodName, (Boolean) result);
                } else if (method.getReturnType().isEnum()) {
                    jsonObject.addProperty(methodName, result.toString());
                } else if (method.getReturnType().isAnnotation()) {
                    jsonObject.add(methodName, annotationToJsonByReflect((Annotation) result));
                } else if (Class.class.getSimpleName().equals(returnType)) {
                    jsonObject.addProperty(methodName, ((Class<?>) result).getSimpleName());
                }
            }
        }
        return jsonObject;
    }



    @SneakyThrows
    public static EditTypeMapping getEditTypeMapping(EditType editType) {
        return EditType.class.getDeclaredField(editType.name()).getAnnotation(EditTypeMapping.class);
    }

    @SneakyThrows
    public static EditTypeSearch getEditTypeSearch(EditType editType) {
        return EditType.class.getDeclaredField(editType.name()).getAnnotation(EditTypeSearch.class);
    }

}
