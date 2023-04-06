package io.fluent.qabox.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.config.GsonFactory;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.view.BoxApiModel;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.function.Consumer;

@Slf4j
@RestController
@RequestMapping(BoxRestPath.ERUPT_DATA_MODIFY)
@RequiredArgsConstructor
public class BoxTabController {

    private final Gson gson = GsonFactory.getGson();

    //TAB组件新增行为
    @PostMapping({"/tab-add/{erupt}/{tabName}"})
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public BoxApiModel addTabEruptData(@PathVariable("erupt") String erupt, @PathVariable("tabName") String tabName, @RequestBody JsonObject data) {
        BoxModel eruptModel = getTabErupt(erupt, tabName);
        Object obj = gson.fromJson(data.toString(), eruptModel.getClazz());
        BoxApiModel BoxApiModel = this.tabValidate(eruptModel, data, dp -> dp.beforeAdd(obj));
        BoxApiModel.setData(obj);
        return BoxApiModel;
    }

    //TAB组件更新行为
    @PostMapping({"/tab-update/{erupt}/{tabName}"})
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public BoxApiModel updateTabEruptData(@PathVariable("erupt") String erupt, @PathVariable("tabName") String tabName, @RequestBody JsonObject data) {
        BoxModel eruptModel = getTabErupt(erupt, tabName);
        Object obj = gson.fromJson(data.toString(), eruptModel.getClazz());
        BoxApiModel BoxApiModel = this.tabValidate(eruptModel, data, dp -> dp.beforeUpdate(obj));
        BoxApiModel.setData(obj);
        return BoxApiModel;
    }

    //TAB组件删除行为
    @PostMapping({"/tab-delete/{erupt}/{tabName}"})
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public BoxApiModel deleteTabEruptData(@PathVariable("erupt") String erupt, @PathVariable("tabName") String tabName, @RequestBody JsonObject data) {
        BoxApiModel BoxApiModel = io.fluent.qabox.view.BoxApiModel.successApi();
        BoxModel eruptModel = getTabErupt(erupt, tabName);
        DataProxyInvoke.invoke(eruptModel, dp -> dp.beforeDelete(gson.fromJson(data.toString(), eruptModel.getClazz())));
        BoxApiModel.setPromptWay(io.fluent.qabox.view.BoxApiModel.PromptWay.MESSAGE);
        return BoxApiModel;
    }

    private BoxApiModel tabValidate(BoxModel eruptModel, JsonObject data, Consumer<DataProxy<Object>> consumer) {
        BoxApiModel BoxApiModel = BoxUtil.validateEruptValue(eruptModel, data);
        if (BoxApiModel.getStatus() == io.fluent.qabox.view.BoxApiModel.Status.SUCCESS) {
            DataProxyInvoke.invoke(eruptModel, consumer);
        }
        BoxApiModel.setErrorIntercept(false);
        BoxApiModel.setPromptWay(io.fluent.qabox.view.BoxApiModel.PromptWay.MESSAGE);
        return BoxApiModel;
    }

    private BoxModel getTabErupt(String erupt, String tabName) {
        BoxFieldModel tabField = BoxCoreService.getBoxModel(erupt).getEruptFieldMap().get(tabName);
        if (null == tabField) {
            throw new WebApiRuntimeException(tabName + "not found");
        }
        return BoxCoreService.getBoxModel(tabField.getFieldReturnName());
    }


}
