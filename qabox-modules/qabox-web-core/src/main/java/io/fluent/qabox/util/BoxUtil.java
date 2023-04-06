package io.fluent.qabox.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.fluent.qabox.UIField;
import io.fluent.qabox.annotation.AttachmentUpload;
import io.fluent.qabox.config.QueryExpression;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.exception.ApiError;
import io.fluent.qabox.frontend.SceneEnum;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.frontend.field.sub_edit.*;
import io.fluent.qabox.frontend.fun.*;
import io.fluent.qabox.service.BoxAppBeanService;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.util.internal.TypeUtil;
import io.fluent.qabox.util.misc.DateUtil;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.util.web.SecurityUtil;
import io.fluent.qabox.view.BoxApiModel;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoxUtil {

    //
    @SneakyThrows
    public static Map<String, Object> generateEruptDataMap(BoxModel boxModel, Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (BoxFieldModel fieldModel : boxModel.getEruptFieldModels()) {
            if (AnnotationConst.EMPTY_STR.equals(fieldModel.getUiField().edit().title()) &&
                    !boxModel.getBox().primaryKeyCol().equals(fieldModel.getFieldName())) {
                continue;
            }
            Field field = fieldModel.getField();
            field.setAccessible(true);
            Object value = field.get(obj);
            if (null != value) {
                UIField eruptField = fieldModel.getUiField();
                switch (eruptField.edit().type()) {
                    case REFERENCE_TREE:
                    case REFERENCE_TABLE:
                        String id;
                        String label;
                        if (eruptField.edit().type() == EditType.REFERENCE_TREE) {
                            ReferenceTreeType referenceTreeType = eruptField.edit().referenceTreeType();
                            id = referenceTreeType.id();
                            label = referenceTreeType.label();
                        } else {
                            ReferenceTableType referenceTableType = eruptField.edit().referenceTableType();
                            id = referenceTableType.id();
                            label = referenceTableType.label();
                        }
                        Map<String, Object> referMap = new HashMap<>();
                        referMap.put(id, ReflectUtil.findFieldChain(id, value));
                        referMap.put(label, ReflectUtil.findFieldChain(label, value));
                        for (View view : eruptField.views()) {
                            //修复表格列无法显示子类属性（例如xxx.yyy.zzz这样的列配置）的缺陷，要配合前端的bug修复。
                            //修复一对多情况下无法显示子类属性的问题
                            String columnKey = view.column().replace(".", "_");
                            Object columnValue = ReflectUtil.findFieldChain(view.column(), value);
                            referMap.put(columnKey, columnValue);
                            map.put(field.getName() + "_" + columnKey, columnValue);

                        }
                        map.put(field.getName(), referMap);
                        break;
                    case COMBINE:
                        map.put(field.getName(), generateEruptDataMap(BoxCoreService.getBoxModel(fieldModel.getFieldReturnName()), value));
                        break;
                    case CHECKBOX:
                    case TAB_TREE:
                        BoxModel tabModel = BoxCoreService.getBoxModel(fieldModel.getFieldReturnName());
                        Collection<?> collection = (Collection<?>) value;
                        if (collection.size() > 0) {
                            Set<Object> idSet = new HashSet<>();
                            Field primaryField = ReflectUtil.findClassField(collection.iterator().next().getClass(),
                                    tabModel.getBox().primaryKeyCol());
                            for (Object o : collection) {
                                idSet.add(primaryField.get(o));
                            }
                            map.put(field.getName(), idSet);
                        }
                        break;
                    case TAB_TABLE_REFER:
                    case TAB_TABLE_ADD:
                        BoxModel tabEruptModelRef = BoxCoreService.getBoxModel(fieldModel.getFieldReturnName());
                        Collection<?> collectionRef = (Collection<?>) value;
                        List<Object> list = new ArrayList<>();
                        for (Object o : collectionRef) {
                            list.add(generateEruptDataMap(tabEruptModelRef, o));
                        }
                        map.put(field.getName(), list);
                        break;
                    default:
                        map.put(field.getName(), value);
                        break;
                }
            }
        }
        return map;
    }

    public static Map<String, String> getChoiceMap(ChoiceType choiceType) {
        Map<String, String> choiceMap = new LinkedHashMap<>();
        for (VL vl : choiceType.vl()) {
            choiceMap.put(vl.value(), vl.label());
        }
        for (Class<? extends ChoiceFetchHandler> clazz : choiceType.fetchHandler()) {
            if (!clazz.isInterface()) {
                List<VLModel> vls = IocUtil.getBean(clazz).fetch(choiceType.fetchHandlerParams());
                if (null != vls) {
                    for (VLModel vl : vls) {
                        choiceMap.put(vl.getValue(), vl.getLabel());
                    }
                }
            }
        }
        return choiceMap;
    }

    public static List<VLModel> getChoiceList(ChoiceType choiceType) {
        List<VLModel> vls = Stream.of(choiceType.vl()).map(vl -> new VLModel(vl.value(), vl.label(), vl.desc(), vl.disable())).collect(Collectors.toList());
        Stream.of(choiceType.fetchHandler()).filter(clazz -> !clazz.isInterface()).forEach(clazz -> {
            Optional.ofNullable(IocUtil.getBean(clazz).fetch(choiceType.fetchHandlerParams())).ifPresent(vls::addAll);
        });
        return vls;
    }

    public static List<String> getTagList(TagsType tagsType) {
        List<String> tags = new ArrayList<>(Arrays.asList(tagsType.tags()));
        Stream.of(tagsType.fetchHandler()).filter(clazz -> !clazz.isInterface())
                .forEach(clazz -> tags.addAll(IocUtil.getBean(clazz).fetchTags(tagsType.fetchHandlerParams())));
        return tags;
    }

    public static Object convertObjectType(BoxFieldModel boxFieldModel, Object obj) {
        if (null == obj) {
            return null;
        }
        if (null == boxFieldModel) {
            return obj.toString();
        }
        String str = obj.toString();
        Edit edit = boxFieldModel.getUiField().edit();
        switch (edit.type()) {
            case DATE:
                if (isDateField(boxFieldModel.getFieldReturnName())) {
                    return DateUtil.getDate(str);
                } else {
                    return str;
                }
            case REFERENCE_TREE:
            case REFERENCE_TABLE:
                String id = null;
                if (edit.type().equals(EditType.REFERENCE_TREE)) {
                    id = boxFieldModel.getUiField().edit().referenceTreeType().id();
                } else if (edit.type().equals(EditType.REFERENCE_TABLE)) {
                    id = edit.referenceTableType().id();
                }
                BoxFieldModel efm = BoxCoreService.getBoxModel(boxFieldModel.getFieldReturnName()).getEruptFieldMap().get(id);
                Map<String, Object> map = (Map<String, Object>) obj;
                return TypeUtil.typeStrConvertObject(map.get(id), efm.getField().getType());
            default:
                return TypeUtil.typeStrConvertObject(str, boxFieldModel.getField().getType());
        }
    }

    //生成一个合法的searchCondition
    public static List<Condition> geneEruptSearchCondition(BoxModel boxModel, List<Condition> searchCondition) {
        checkEruptSearchNotnull(boxModel, searchCondition);
        List<Condition> legalConditions = new ArrayList<>();
        if (null != searchCondition) {
            for (Condition condition : searchCondition) {
                BoxFieldModel boxFieldModel = boxModel.getEruptFieldMap().get(condition.getKey());
                if (null != boxFieldModel) {
                    Edit edit = boxFieldModel.getUiField().edit();
                    EditTypeSearch editTypeSearch = AnnotationUtil.getEditTypeSearch(edit.type());
                    if (null != editTypeSearch && editTypeSearch.value()) {
                        if (edit.search().value() && null != condition.getValue()) {
                            if (condition.getValue() instanceof Collection) {
                                Collection<?> collection = (Collection<?>) condition.getValue();
                                if (collection.size() == 0) {
                                    continue;
                                }
                            }
                            if (edit.search().vague()) {
                                condition.setExpression(editTypeSearch.vagueMethod());
                            } else {
                                condition.setExpression(QueryExpression.EQ);
                            }
                            legalConditions.add(condition);
                        }
                    }
                }
            }
        }
        return legalConditions;
    }

    public static void checkEruptSearchNotnull(BoxModel boxModel, List<Condition> searchCondition) {
        Map<String, Condition> conditionMap = new HashMap<>();
        if (null != searchCondition) {
            searchCondition.forEach(condition -> conditionMap.put(condition.getKey(), condition));
        }
        for (BoxFieldModel fieldModel : boxModel.getEruptFieldModels()) {
            Edit edit = fieldModel.getUiField().edit();
            if (edit.search().value() && edit.search().notNull()) {
                Condition condition = conditionMap.get(fieldModel.getFieldName());
                if (null == condition || null == condition.getValue()) {
                    throw new ApiError(BoxApiModel.Status.INFO, edit.title() + "必填", BoxApiModel.PromptWay.MESSAGE);
                }
                if (condition.getValue() instanceof List) {
                    if (((List<?>) condition.getValue()).size() == 0) {
                        throw new ApiError(BoxApiModel.Status.INFO + edit.title() + "必填", BoxApiModel.PromptWay.MESSAGE);
                    }
                }
            }
        }
    }

    public static BoxApiModel validateEruptValue(BoxModel boxModel, JsonObject jsonObject) {
        for (BoxFieldModel field : boxModel.getEruptFieldModels()) {
            Edit edit = field.getUiField().edit();
            JsonElement value = jsonObject.get(field.getFieldName());
            if (field.getUiField().edit().notNull()) {
                if (null == value || value.isJsonNull()) {
                    return BoxApiModel.errorNoInterceptMessage(field.getUiField().edit().title() + "必填");
                } else if (String.class.getSimpleName().equals(field.getFieldReturnName())) {
                    if (StringUtils.isBlank(value.getAsString())) {
                        return BoxApiModel.errorNoInterceptMessage(field.getUiField().edit().title() + "必填");
                    }
                }
            }
            if (field.getUiField().edit().type() == EditType.COMBINE) {
                BoxApiModel eam = validateEruptValue(BoxCoreService.getBoxModel(field.getFieldReturnName()), jsonObject.getAsJsonObject(field.getFieldName()));
                if (eam.getStatus() == BoxApiModel.Status.ERROR) {
                    return eam;
                }
            }
            if (null != value && !AnnotationConst.EMPTY_STR.equals(edit.title())) {
                //xss 注入处理
                if (edit.type() == EditType.TEXTAREA || edit.type() == EditType.INPUT) {
                    if (SecurityUtil.xssInspect(value.getAsString())) {
                        return BoxApiModel.errorNoInterceptApi(field.getUiField().edit().title() + "检测到有恶意跨站脚本，请重新编辑！");
                    }
                }
                //数据类型校验
                switch (edit.type()) {
                    case NUMBER:
                    case SLIDER:
                        if (!NumberUtils.isCreatable(value.getAsString())) {
                            return BoxApiModel.errorNoInterceptMessage(field.getUiField().edit().title() + "必须为数值");
                        }
                        break;
                    case INPUT:
                        if (!AnnotationConst.EMPTY_STR.equals(edit.inputType().regex())) {
                            String content = value.getAsString();
                            if (StringUtils.isNotBlank(content)) {
                                if (!Pattern.matches(edit.inputType().regex(), content)) {
                                    return BoxApiModel.errorNoInterceptMessage(field.getUiField().edit().title() + "格式不正确");
                                }
                            }
                        }
                        break;
                }
            }
        }
        return BoxApiModel.successApi();
    }

    public static Object toEruptId(BoxModel eruptModel, String id) {
        Field primaryField = ReflectUtil.findClassField(eruptModel.getClazz(), eruptModel.getBox().primaryKeyCol());
        return TypeUtil.typeStrConvertObject(id, primaryField.getType());
    }

    //将对象A的非空数据源覆盖到对象B中
    public static Object dataTarget(BoxModel eruptModel, Object data, Object target, SceneEnum sceneEnum) {
        ReflectUtil.findClassAllFields(eruptModel.getClazz(), f -> Optional.ofNullable(f.getAnnotation(UIField.class)).ifPresent(eruptField -> {
            boolean readonly = sceneEnum == SceneEnum.EDIT ? eruptField.edit().readonly().edit() : eruptField.edit().readonly().add();
            if (StringUtils.isNotBlank(eruptField.edit().title()) && !readonly) {
                try {
                    f.setAccessible(true);
                    if (eruptField.edit().type() == EditType.TAB_TABLE_ADD) {
                        Collection<?> s = (Collection<?>) f.get(target);
                        if (null == s) {
                            f.set(target, f.get(data));
                        } else {
                            s.clear();
                            s.addAll((Collection) f.get(data));
                            f.set(target, s);
                        }
                    } else {
                        f.set(target, f.get(data));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }));
        return target;
    }

    //清理序列化后对象所产生的默认值（通过json串进行校验）
    public static void clearObjectDefaultValueByJson(Object obj, JsonObject data) {
        ReflectUtil.findClassAllFields(obj.getClass(), field -> {
            try {
                field.setAccessible(true);
                if (null != field.get(obj)) {
                    if (!data.has(field.getName())) {
                        field.set(obj, null);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取附件上传代理器
     *
     * @return AttachmentProxy
     */
    public static AttachmentProxy findAttachmentProxy() {
        AttachmentUpload eruptAttachmentUpload = BoxAppBeanService.getPrimarySource().getAnnotation(AttachmentUpload.class);
        return null == eruptAttachmentUpload ? null : IocUtil.getBean(eruptAttachmentUpload.value());
    }

    //是否为时间字段
    public static boolean isDateField(String fieldType) {
        if (Date.class.getSimpleName().equals(fieldType)) {
            return true;
        } else if (LocalDate.class.getSimpleName().equals(fieldType)) {
            return true;
        } else {
            return LocalDateTime.class.getSimpleName().equals(fieldType);
        }
    }

    public static OutputStream downLoadFile(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            String headStr = "attachment; filename=" + java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", headStr);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            return response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
