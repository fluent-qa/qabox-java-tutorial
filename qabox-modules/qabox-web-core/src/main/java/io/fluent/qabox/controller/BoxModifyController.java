package io.fluent.qabox.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.annotation.RecordOperate;
import io.fluent.qabox.component.naming.BoxRecordNaming;
import io.fluent.qabox.config.GsonFactory;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.frontend.SceneEnum;
import io.fluent.qabox.frontend.operation.LinkTree;
import io.fluent.qabox.fun.PermissionObject;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.processor.invoker.DataProxyInvoke;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.service.BoxModelService;
import io.fluent.qabox.service.IBoxDataService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.Boxes;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.BoxApiModel;
import io.fluent.qabox.view.BoxModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * Erupt 对数据的增删改查
 */
@Slf4j
@RestController
@RequestMapping(BoxRestPath.ERUPT_BUILD)
@RequiredArgsConstructor
public class BoxModifyController {

    private final Gson gson = GsonFactory.getGson();

    private final BoxModelService eruptService;

    @SneakyThrows
    @PostMapping({"/{erupt}"})
    @RecordOperate(value = "新增", dynamicConfig = BoxRecordNaming.class)
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public BoxApiModel addEruptData(@PathVariable("erupt") String erupt, @RequestBody JsonObject data,
                                    JsonObject jsonObject, HttpServletRequest request) {
        BoxModel eruptModel = BoxCoreService.getBoxModel(erupt);
        Boxes.powerLegal(eruptModel, PermissionObject::isAdd);
        LinkTree dependTree = eruptModel.getBox().linkTree();
        if (StringUtils.isNotBlank(dependTree.field()) && dependTree.dependNode()) {
            String linkVal = request.getHeader("link");
            //必须是强依赖才会自动注入值
            if (dependTree.dependNode()) {
                if (StringUtils.isBlank(linkVal)) {
                    return BoxApiModel.errorApi("请选择树节点");
                } else {
                    if (null == jsonObject) jsonObject = new JsonObject();
                    String rm = ReflectUtil.findClassField(eruptModel.getClazz(), dependTree.field()).getType().getSimpleName();
                    JsonObject sub = new JsonObject();
                    sub.addProperty(BoxCoreService.getBoxModel(rm).getBox().primaryKeyCol(), linkVal);
                    jsonObject.add(dependTree.field(), sub);
                }
            }
        }
        BoxApiModel BoxApiModel = BoxUtil.validateEruptValue(eruptModel, data);
        if (BoxApiModel.getStatus() == io.fluent.qabox.view.BoxApiModel.Status.ERROR) return BoxApiModel;
        Object o = gson.fromJson(data.toString(), eruptModel.getClazz());
        BoxUtil.clearObjectDefaultValueByJson(o, data);
        Object obj = BoxUtil.dataTarget(eruptModel, o, eruptModel.getClazz().newInstance(), SceneEnum.ADD);
        if (null != jsonObject) {
            for (String key : jsonObject.keySet()) {
                Field field = ReflectUtil.findClassField(eruptModel.getClazz(), key);
                field.setAccessible(true);
                field.set(obj, gson.fromJson(jsonObject.get(key).toString(), field.getType()));
            }
        }
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.beforeAdd(obj)));
        DataProcessorManager.getDataProcessor(eruptModel.getClazz()).addData(eruptModel, obj);
        this.modifyLog(eruptModel, "ADD", data.toString());
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.afterAdd(obj)));
        return io.fluent.qabox.view.BoxApiModel.successApi();
    }

    @PutMapping("/{erupt}")
    @RecordOperate(value = "修改", dynamicConfig = BoxRecordNaming.class)
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    @Transactional
    public BoxApiModel editEruptData(@PathVariable("erupt") String erupt, @RequestBody JsonObject data) throws IllegalAccessException {
        BoxModel eruptModel = BoxCoreService.getBoxModel(erupt);
        Boxes.powerLegal(eruptModel, PermissionObject::isEdit);
        BoxApiModel BoxApiModel = BoxUtil.validateEruptValue(eruptModel, data);
        if (BoxApiModel.getStatus() == io.fluent.qabox.view.BoxApiModel.Status.ERROR) return BoxApiModel;
        eruptService.verifyIdPermissions(eruptModel, data.get(eruptModel.getBox().primaryKeyCol()).getAsString());
        Object o = this.gson.fromJson(data.toString(), eruptModel.getClazz());
        BoxUtil.clearObjectDefaultValueByJson(o, data);
        Object obj = BoxUtil.dataTarget(eruptModel, o, DataProcessorManager.getDataProcessor(eruptModel.getClazz())
                .findDataById(eruptModel, ReflectUtil.findClassField(eruptModel.getClazz(), eruptModel.getBox().primaryKeyCol()).get(o)), SceneEnum.EDIT);
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.beforeUpdate(obj)));
        DataProcessorManager.getDataProcessor(eruptModel.getClazz()).editData(eruptModel, obj);
        this.modifyLog(eruptModel, "EDIT", data.toString());
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.afterUpdate(obj)));
        return io.fluent.qabox.view.BoxApiModel.successApi();
    }

    @DeleteMapping("/{erupt}/{id}")
    @RecordOperate(value = "删除", dynamicConfig = BoxRecordNaming.class)
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    @Transactional
    public BoxApiModel deleteEruptData(@PathVariable("erupt") String erupt, @PathVariable("id") String id) {
        BoxModel eruptModel = BoxCoreService.getBoxModel(erupt);
        Boxes.powerLegal(eruptModel, PermissionObject::isDelete);
        eruptService.verifyIdPermissions(eruptModel, id);
        IBoxDataService dataService = DataProcessorManager.getDataProcessor(eruptModel.getClazz());
        //获取对象数据信息用于DataProxy函数中
        Object obj = dataService.findDataById(eruptModel, BoxUtil.toEruptId(eruptModel, id));
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.beforeDelete(obj)));
        dataService.deleteData(eruptModel, obj);
        this.modifyLog(eruptModel, "DELETE", id);
        DataProxyInvoke.invoke(eruptModel, (dataProxy -> dataProxy.afterDelete(obj)));
        return BoxApiModel.successApi();
    }

    @Transactional
    @DeleteMapping("/{erupt}")
    @BoxWebRouter(skipAuthIndex = 3, authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    @RecordOperate(value = "批量删除", dynamicConfig = BoxRecordNaming.class)
    public BoxApiModel deleteEruptDataList(@PathVariable("erupt") String erupt, @RequestParam("ids") String[] ids) {
        BoxApiModel BoxApiModel = io.fluent.qabox.view.BoxApiModel.successApi();
        for (String id : ids) {
            BoxApiModel = this.deleteEruptData(erupt, id);
            if (BoxApiModel.getStatus() == io.fluent.qabox.view.BoxApiModel.Status.ERROR) {
                break;
            }
        }
        return BoxApiModel;
    }

    private void modifyLog(BoxModel eruptModel, String placeholder, String content) {
        log.info("[" + eruptModel.getEruptName() + " -> " + placeholder + "]:" + content);
    }
}
