package io.fluent.qabox.util;

import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.TreeModel;
import org.springframework.util.CollectionUtils;


import java.util.*;


public class DataHandlerUtil {

    // 引用方式 生成树结构数据
    public static List<TreeModel> quoteTree(List<TreeModel> treeModels) {
        Map<String, TreeModel> treeModelMap = new LinkedHashMap<>(treeModels.size());
        treeModels.forEach(treeModel -> treeModelMap.put(treeModel.getId(), treeModel));
        List<TreeModel> resultTreeModels = new ArrayList<>();
        treeModels.forEach(treeModel -> {
            if (treeModel.isRoot()) {
                treeModel.setLevel(1);
                resultTreeModels.add(treeModel);
                return;
            }
            Optional.ofNullable(treeModelMap.get(treeModel.getPid())).ifPresent(parentTreeModel -> {
                Collection<TreeModel> children = CollectionUtils.isEmpty(parentTreeModel.getChildren()) ? new ArrayList<>() : parentTreeModel.getChildren();
                children.add(treeModel);
                children.forEach(child -> child.setLevel(Optional.ofNullable(parentTreeModel.getLevel()).orElse(1) + 1));
                parentTreeModel.setChildren(children);
            });
        });
        return resultTreeModels;
    }

    private static BoxFieldModel cycleFindFieldByKey(BoxModel boxModel, String key) {
      BoxFieldModel fieldModel = boxModel.getEruptFieldMap().get(key);
        if (null != fieldModel) {
            return fieldModel;
        }
        if (key.contains("_")) {
            return cycleFindFieldByKey(boxModel, key.substring(0, key.lastIndexOf("_")));
        }
        return null;
    }

    public static void convertDataToEruptView(BoxModel boxModel, Collection<Map<String, Object>> list) {
        Map<String, Map<String, String>> choiceItems = new HashMap<>();
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                BoxFieldModel fieldModel = cycleFindFieldByKey(boxModel, entry.getKey());
                if (null == fieldModel) {
                    continue;
                }
                Edit edit = fieldModel.getUiField().edit();
                switch (edit.type()) {
                    case REFERENCE_TREE:
                    case REFERENCE_TABLE:
                    case COMBINE:
                        String[] _keys = entry.getKey().split("_");
                        for (View view : fieldModel.getUiField().views()) {
                            if (view.column().equals(_keys[_keys.length - 1])) {
                                BoxFieldModel vef = BoxCoreService.getBoxModel(fieldModel.getFieldReturnName()).getEruptFieldMap().get(view.column());
                                map.put(entry.getKey(), convertColumnValue(vef, entry.getValue(), choiceItems));
                            }
                        }
                        break;
                    default:
                        map.put(entry.getKey(), convertColumnValue(fieldModel, entry.getValue(), choiceItems));
                        break;
                }
            }
        }
    }

    private static Object convertColumnValue(BoxFieldModel fieldModel, Object value, Map<String, Map<String, String>> choiceItems) {
        if (null == value) return null;
        Edit edit = fieldModel.getUiField().edit();
        switch (edit.type()) {
            case CHOICE:
                Map<String, String> cm = choiceItems.get(fieldModel.getFieldName());
                if (null == cm) {
                    cm = BoxUtil.getChoiceMap(edit.choiceType());
                    choiceItems.put(fieldModel.getFieldName(), cm);
                }
                return cm.get(value.toString());
            case BOOLEAN:
                return (Boolean) value ? edit.boolType().trueText() : edit.boolType().falseText();
        }
        return value;
    }


}