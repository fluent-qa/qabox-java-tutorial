package io.fluent.qabox.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.annotation.RecordOperate;
import io.fluent.qabox.component.naming.BoxRowOperationNaming;
import io.fluent.qabox.config.GsonFactory;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.exception.BoxPermissionException;
import io.fluent.qabox.frontend.OperationHandler;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.sub_edit.CheckboxType;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTableType;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTreeType;
import io.fluent.qabox.frontend.model.Row;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.frontend.operation.Tree;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.processor.invoker.ExprResolverInvoke;
import io.fluent.qabox.query.BoxQuery;
import io.fluent.qabox.query.ColumnQuery;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.service.BoxModelService;
import io.fluent.qabox.service.I18NTranslateService;
import io.fluent.qabox.service.PreBoxDataService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.Boxes;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.view.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(BoxRestPath.ERUPT_BUILD)
@RequiredArgsConstructor
@Slf4j
public class BoxViewDataController {

    private final BoxModelService boxModelService;

    private final PreBoxDataService preEruptDataService;

    private final Gson gson = GsonFactory.getGson();

    private final I18NTranslateService i18NTranslateService;

    @PostMapping({"/table/{erupt}"})
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Page getEruptData(@PathVariable("erupt") String eruptName, @RequestBody TableQueryVo tableQueryVo) {
        return boxModelService.getEruptData(BoxCoreService.getBoxModel(eruptName), tableQueryVo, null);
    }

    @GetMapping("/tree/{erupt}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Collection<TreeModel> getEruptTreeData(@PathVariable("erupt") String eruptName) {
        BoxModel boxModel = BoxCoreService.getBoxModel(eruptName);
        Boxes.powerLegal(boxModel, PermissionObject::isQuery);
        Tree tree = boxModel.getBox().tree();
        return preEruptDataService.geneTree(boxModel, tree.id(), tree.label(), tree.pid(), tree.rootPid(), BoxQuery.builder().build());
    }

    //获取初始化数据
    @GetMapping("/init-value/{erupt}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Map<String, Object> initEruptValue(@PathVariable("erupt") String eruptName) throws IllegalAccessException, InstantiationException {
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
        Object obj = eruptModel.getClazz().newInstance();
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.addBehavior(obj)));
        return BoxUtil.generateEruptDataMap(eruptModel, obj);
    }

    @GetMapping("/{erupt}/{id}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Map<String, Object> getEruptDataById(@PathVariable("erupt") String eruptName, @PathVariable("id") String id) {
        BoxModel boxModel = BoxCoreService.getBoxModel(eruptName);
        Boxes.powerLegal(boxModel, powerObject -> powerObject.isEdit() || powerObject.isViewDetails());
        boxModelService.verifyIdPermissions(boxModel, id);
        Object data = DataProcessorManager.getDataProcessor(boxModel.getClazz())
                .findDataById(boxModel, BoxUtil.toEruptId(boxModel, id));
        DataProxyInvoke.invoke(boxModel, (dataProxy -> dataProxy.editBehavior(data)));
        return BoxUtil.generateEruptDataMap(boxModel, data);
    }

    public static final String OPERATOR_PATH_STR = "/operator";

    @PostMapping("/{erupt}" + OPERATOR_PATH_STR + "/{code}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    @RecordOperate(value = "", dynamicConfig = BoxRowOperationNaming.class)
    public BoxApiModel execEruptOperator(@PathVariable("erupt") String eruptName, @PathVariable("code") String code,
                                         @RequestBody JsonObject body) {
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
        RowOperation rowOperation = Arrays.stream(eruptModel.getBox().rowOperation()).filter(it -> code.equals(it.code())).findFirst().orElseThrow(BoxPermissionException::new);
        Boxes.powerLegal(ExprResolverInvoke.getExpr(rowOperation.show()));
        if (rowOperation.eruptClass() != void.class) {
            BoxModel erupt = BoxCoreService.getBoxModel(rowOperation.eruptClass().getSimpleName());
            BoxApiModel BoxApiModel = BoxUtil.validateEruptValue(erupt, body.getAsJsonObject("param"));
            if (BoxApiModel.getStatus() == io.fluent.qabox.view.BoxApiModel.Status.ERROR) return BoxApiModel;
        }
        if (rowOperation.operationHandler().isInterface()) {
            return BoxApiModel.errorApi("Please implement the 'OperationHandler' interface for " + rowOperation.title());
        }
        OperationHandler<Object, Object> operationHandler = IocUtil.getBean(rowOperation.operationHandler());
        Object param = null;
        if (!body.get("param").isJsonNull()) {
            param = gson.fromJson(body.getAsJsonObject("param"), rowOperation.eruptClass());
        }
        if (rowOperation.mode() == RowOperation.Mode.BUTTON) {
            String eval = operationHandler.exec(null, param, rowOperation.operationParam());
            if (StringUtils.isNotBlank(eval)) {
                return BoxApiModel.successApi(eval);
            } else {
                return BoxApiModel.successApi(i18NTranslateService.translate("执行成功"), null);
            }
        }
        if (body.get("ids").isJsonArray() && body.getAsJsonArray("ids").size() > 0) {
            List<Object> list = new ArrayList<>();
            body.getAsJsonArray("ids").forEach(id -> list.add(DataProcessorManager.getDataProcessor(eruptModel.getClazz())
                    .findDataById(eruptModel, BoxUtil.toEruptId(eruptModel, id.getAsString()))));
            String eval = operationHandler.exec(list, param, rowOperation.operationParam());
            if (StringUtils.isNotBlank(eval)) {
                return BoxApiModel.successApi(eval);
            } else {
                return BoxApiModel.successApi(i18NTranslateService.translate("执行成功"), null);
            }
        } else {
            return BoxApiModel.errorApi(i18NTranslateService.translate("执行该操作时请至少选中一条数据"));
        }
    }

    @GetMapping("/tab/tree/{erupt}/{tabFieldName}")
    @BoxWebRouter(authIndex = 3, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Collection<TreeModel> findTabTree(@PathVariable("erupt") String eruptName, @PathVariable("tabFieldName") String tabFieldName) {
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
//        Boxes.powerLegal(eruptModel, powerObject -> powerObject.isViewDetails() || powerObject.isEdit());
        BoxModel tabEruptModel =  BoxCoreService.getBoxModel(eruptModel.getEruptFieldMap().get(tabFieldName).getFieldReturnName());
        Tree tree = tabEruptModel.getBox().tree();
        BoxFieldModel eruptFieldModel = eruptModel.getEruptFieldMap().get(tabFieldName);
       BoxQuery eruptQuery = BoxQuery.builder().conditionStrings(
                Arrays.stream(eruptFieldModel.getUiField().edit().filter()).map(Filter::value).collect(Collectors.toList())
        ).build();
        return preEruptDataService.geneTree(tabEruptModel, tree.id(), tree.label(), tree.pid(), tree.rootPid(), eruptQuery);
    }

    @GetMapping("/{erupt}/checkbox/{fieldName}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Collection<CheckboxModel> findCheckbox(@PathVariable("erupt") String eruptName, @PathVariable("fieldName") String fieldName) {
        BoxModel eruptModel =  BoxCoreService.getBoxModel(eruptName);
//        Boxes.powerLegal(eruptModel, powerObject -> powerObject.isViewDetails() || powerObject.isEdit());
        BoxFieldModel eruptFieldModel = eruptModel.getEruptFieldMap().get(fieldName);
        BoxModel tabEruptModel =  BoxCoreService.getBoxModel(eruptFieldModel.getFieldReturnName());
        CheckboxType checkboxType = eruptFieldModel.getUiField().edit().checkboxType();
        List<ColumnQuery> columns = new ArrayList<>();
        columns.add(new ColumnQuery(checkboxType.id(), AnnotationConst.ID));
        columns.add(new ColumnQuery(checkboxType.label(), AnnotationConst.LABEL));
       BoxQuery eruptQuery = BoxQuery.builder().conditionStrings(
                Arrays.stream(eruptFieldModel.getUiField().edit().filter()).map(Filter::value).collect(Collectors.toList())
        ).build();
        Collection<Map<String, Object>> collection = preEruptDataService.createColumnQuery(tabEruptModel, columns, eruptQuery);
        Collection<CheckboxModel> checkboxModels = new ArrayList<>(collection.size());
        collection.forEach(map -> checkboxModels.add(new CheckboxModel(map.get(AnnotationConst.ID), map.get(AnnotationConst.LABEL))));
        return checkboxModels;
    }

    // REFERENCE API
    @PostMapping("/{erupt}/reference-table/{fieldName}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Page getReferenceTable(@PathVariable("erupt") String eruptName,
                                  @PathVariable("fieldName") String fieldName,
                                  @RequestParam(value = "dependValue", required = false) Serializable dependValue,
                                  @RequestParam(value = "tabRef", required = false) Boolean tabRef,
                                  @RequestBody TableQueryVo tableQueryVo) {
        BoxModel eruptModel =  BoxCoreService.getBoxModel(eruptName);
        BoxFieldModel eruptFieldModel = eruptModel.getEruptFieldMap().get(fieldName);
//        Boxes.powerLegal(eruptModel, powerObject -> powerObject.isEdit() || powerObject.isAdd() || eruptFieldModel.getBoxField().edit().search().value());
        Edit edit = eruptFieldModel.getUiField().edit();
        String dependField = edit.referenceTableType().dependField();
        String dependCondition = "";
        if (!AnnotationConst.EMPTY_STR.equals(dependField)) {
            Boxes.requireNonNull(dependCondition, "请先选择" + eruptModel.getEruptFieldMap().get(dependField).getUiField().edit().title());
            dependCondition = eruptFieldModel.getFieldReturnName() + '.' + edit.referenceTableType().dependColumn() + '=' + dependValue;
        }
        List<String> conditions = Arrays.stream(edit.filter()).map(Filter::value).collect(Collectors.toList());
        conditions.add(dependCondition);
        BoxModel eruptReferenceModel =  BoxCoreService.getBoxModel(eruptFieldModel.getFieldReturnName());
        if (!tabRef) {
            //由于类加载顺序问题，并未选择在启动时检测
            ReferenceTableType referenceTableType = eruptFieldModel.getUiField().edit().referenceTableType();
            Boxes.requireTrue(eruptReferenceModel.getEruptFieldMap().containsKey(referenceTableType.label().split("\\.")[0])
                    , eruptReferenceModel.getEruptName() + " not found '" + referenceTableType.label()
                            + "' field，please use @ReferenceTableType annotation 'label' config");
        }
        return boxModelService.getEruptData(eruptReferenceModel, tableQueryVo,
                null, conditions.toArray(new String[0]));
    }

    @SneakyThrows
    @GetMapping("/depend-tree/{erupt}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Collection<TreeModel> getDependTree(@PathVariable("erupt") String erupt) {
        BoxModel eruptModel =  BoxCoreService.getBoxModel(erupt);
        String field = eruptModel.getBox().linkTree().field();
        if (null == eruptModel.getEruptFieldMap().get(field)) {
            String treeErupt = eruptModel.getClazz().getDeclaredField(field).getType().getSimpleName();
            return this.getEruptTreeData(treeErupt);
        }
        return this.getReferenceTree(eruptModel.getEruptName(), field, null);
    }

    @GetMapping("/{erupt}/reference-tree/{fieldName}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Collection<TreeModel> getReferenceTree(@PathVariable("erupt") String erupt,
                                                  @PathVariable("fieldName") String fieldName,
                                                  @RequestParam(value = "dependValue", required = false) Serializable dependValue) {
        BoxModel eruptModel =  BoxCoreService.getBoxModel(erupt);
        BoxFieldModel boxFieldModel = eruptModel.getEruptFieldMap().get(fieldName);
//        Boxes.powerLegal(eruptModel, powerObject -> powerObject.isEdit() || powerObject.isAdd()
//                || eruptFieldModel.getBoxField().edit().search().value()
//                || StringUtils.isNotBlank(eruptModel.getBox().linkTree().field()));
        String dependField = boxFieldModel.getUiField().edit().referenceTreeType().dependField();
        if (!AnnotationConst.EMPTY_STR.equals(dependField)) {
            Boxes.requireNonNull(dependValue, "请先选择" + eruptModel.getEruptFieldMap().get(dependField).getUiField().edit().title());
        }
        Edit edit = boxFieldModel.getUiField().edit();
        ReferenceTreeType treeType = edit.referenceTreeType();
        BoxModel referenceEruptModel =  BoxCoreService.getBoxModel(boxFieldModel.getFieldReturnName());
        Boxes.requireTrue(referenceEruptModel.getEruptFieldMap().containsKey(treeType.label().split("\\.")[0]),
                referenceEruptModel.getEruptName() + " not found " + treeType.label() + " field, please use @ReferenceTreeType annotation config");
        List<Condition> conditions = new ArrayList<>();
        //处理depend参数代码
        if (StringUtils.isNotBlank(treeType.dependField()) && null != dependValue) {
            conditions.add(new Condition(edit.referenceTreeType().dependColumn(), dependValue));
        }
        List<String> conditionStrings = Arrays.stream(edit.filter()).map(Filter::value).collect(Collectors.toList());
        return preEruptDataService.geneTree(referenceEruptModel, treeType.id(), treeType.label(), treeType.pid(), treeType.rootPid(),
                BoxQuery.builder().orderBy(edit.orderBy()).conditionStrings(conditionStrings).conditions(conditions).build());
    }

    //自定义行
    @PostMapping("/extra-row/{erupt}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public List<Row> extraRow(@PathVariable("erupt") String erupt, @RequestBody TableQueryVo tableQueryVo) {
        List<Row> rows = new ArrayList<>();
        DataProxyInvoke.invoke( BoxCoreService.getBoxModel(erupt), dataProxy ->
                Optional.ofNullable(dataProxy.extraRow(tableQueryVo.getCondition())).ifPresent(rows::addAll));
        return rows;
    }

}