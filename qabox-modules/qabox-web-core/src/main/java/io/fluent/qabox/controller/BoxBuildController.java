package io.fluent.qabox.controller;


import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.processor.invoker.PermissionInvoke;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.view.BoxBuildModel;
import io.fluent.qabox.view.BoxModel;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 页面信息
 *
 */
@RestController
@RequestMapping(BoxRestPath.ERUPT_BUILD)
public class BoxBuildController {

    @GetMapping("/{erupt}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    @SneakyThrows
    public BoxBuildModel getEruptBuild(@PathVariable("erupt") String eruptName) {
        BoxModel eruptView = BoxCoreService.getEruptView(eruptName);
        {
            //default search conditions
            Map<String, Object> conditionsMap = new HashMap<>();
            DataProxyInvoke.invoke(eruptView, it -> it.searchCondition(conditionsMap));
            eruptView.setSearchCondition(conditionsMap);
        }
        BoxBuildModel buildModel = new BoxBuildModel();
        buildModel.setPower(PermissionInvoke.getPermissionObject(eruptView));
        buildModel.setEruptModel(eruptView);
        eruptView.getEruptFieldModels().forEach(fieldModel -> {
            switch (fieldModel.getUiField().edit().type()) {
                case TAB_TREE:
                    buildModel.setTabErupts(Optional.ofNullable(buildModel.getTabErupts()).orElse(new LinkedHashMap<>()));
                  BoxBuildModel m1 = new BoxBuildModel();
                    m1.setEruptModel(BoxCoreService.getEruptView(fieldModel.getFieldReturnName()));
                    buildModel.getTabErupts().put(fieldModel.getFieldName(), m1);
                    break;
                case TAB_TABLE_ADD:
                case TAB_TABLE_REFER:
                    buildModel.setTabErupts(Optional.ofNullable(buildModel.getTabErupts()).orElse(new LinkedHashMap<>()));
                    buildModel.getTabErupts().put(fieldModel.getFieldName(), getEruptBuild(fieldModel.getFieldReturnName()));
                    break;
                case COMBINE:
                    buildModel.setCombineErupts(Optional.ofNullable(buildModel.getCombineErupts()).orElse(new LinkedHashMap<>()));
                    buildModel.getCombineErupts().put(fieldModel.getFieldName(), BoxCoreService.getEruptView(fieldModel.getFieldReturnName()));
                    break;
            }
        });
        Arrays.stream(buildModel.getEruptModel().getBox().rowOperation()).filter(operation ->
                operation.eruptClass() != void.class && operation.type() == RowOperation.Type.ERUPT).forEach(operation -> {
            buildModel.setOperationErupts(Optional.ofNullable(buildModel.getOperationErupts()).orElse(new LinkedHashMap<>()));
            buildModel.getOperationErupts().put(operation.code(), BoxCoreService.getEruptView(operation.eruptClass().getSimpleName()));
        });
        return buildModel;
    }

    @GetMapping("/{erupt}/{field}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public BoxBuildModel getEruptBuildByField(@PathVariable("erupt") String eruptName, @PathVariable("field") String field) {
        return this.getEruptBuild(BoxCoreService.getEruptView(eruptName).getEruptFieldMap().get(field).getFieldReturnName());
    }

}
