package io.fluent.qabox.component.naming;

import io.fluent.qabox.annotation.RecordOperate;
import io.fluent.qabox.controller.BoxViewDataController;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.view.BoxModel;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;


@Component
public class BoxRowOperationNaming implements RecordOperate.DynamicConfig {

    @Resource
    private HttpServletRequest request;

    @Override
    public String naming(String desc, String menuName, String boxName, Method method) {
        BoxModel erupt = BoxCoreService.getBoxModel(boxName);
        if (null == erupt) {
            return menuName + " | @RowOperation";
        }
        return findRowOperation(erupt).title() + " | " + erupt.getBox().name();
    }

    private RowOperation findRowOperation(BoxModel boxModel) {
        String code = request.getServletPath().split(BoxViewDataController.OPERATOR_PATH_STR + "/")[1];
        return Arrays.stream(boxModel.getBox().rowOperation())
                .filter(operation -> operation.code().equals(code)).findFirst()
                .orElseThrow(() -> new RuntimeException(boxModel.getEruptName() + " RowOperation not found " + code));
    }
}