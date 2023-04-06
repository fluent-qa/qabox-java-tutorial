package io.fluent.qabox.controller;

import com.google.gson.JsonObject;
import io.fluent.qabox.Box;
import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.annotation.RecordOperate;
import io.fluent.qabox.component.naming.BoxRecordNaming;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.context.MenuContext;
import io.fluent.qabox.exception.BoxPermissionException;
import io.fluent.qabox.frontend.operation.Link;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.service.BoxModelService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.internal.ReflectUtil;
import io.fluent.qabox.view.BoxApiModel;
import io.fluent.qabox.view.BoxModel;
import io.fluent.qabox.view.Page;
import io.fluent.qabox.view.TableQueryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.stream.Stream;

@RestController
@RequestMapping(BoxRestPath.ERUPT_BUILD)
@RequiredArgsConstructor
public class BoxDrillController {

    private final BoxModifyController eruptModifyController;

    private final BoxModelService eruptService;

    @PostMapping("{erupt}/drill/{code}/{id}")
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public Page drill(@PathVariable("erupt") String eruptName,
                      @PathVariable("code") String code,
                      @PathVariable("id") String id,
                      @RequestBody TableQueryVo tableQueryVo) throws IllegalAccessException {
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
        Link link = findDrillLink(eruptModel.getBox(), code);
        eruptService.verifyIdPermissions(eruptModel, id);
        Field field = ReflectUtil.findClassField(eruptModel.getClazz(), link.column());
        Object data = DataProcessorManager.getDataProcessor(eruptModel.getClazz()).findDataById(eruptModel, BoxUtil.toEruptId(eruptModel, id));
        field.setAccessible(true);
        Object val = field.get(data);
        if (null == val) return new Page();
        UserMenuContext.register(new MenuContext(link.linkErupt().getSimpleName()));
        return eruptService.getEruptData(
                BoxCoreService.getBoxModel(link.linkErupt().getSimpleName()), tableQueryVo, null,
                String.format("%s = '%s'", link.linkErupt().getSimpleName() + "." + link.joinColumn(), val)
        );
    }

    @PostMapping("/add/{erupt}/drill/{code}/{id}")
    @RecordOperate(value = "新增", dynamicConfig = BoxRecordNaming.class)
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public BoxApiModel drillAdd(@PathVariable("erupt") String erupt, @PathVariable("code") String code,
                                @PathVariable("id") String id, @RequestBody JsonObject data,
                                HttpServletRequest request) throws Exception {
        BoxModel eruptModel = BoxCoreService.getBoxModel(erupt);
        Link link = findDrillLink(eruptModel.getBox(), code);
        eruptService.verifyIdPermissions(eruptModel, id);
        JsonObject jo = new JsonObject();
        String joinColumn = link.joinColumn();
        Field field = ReflectUtil.findClassField(eruptModel.getClazz(), link.column());
        field.setAccessible(true);
        Object val = field.get(DataProcessorManager.getDataProcessor(eruptModel.getClazz())
                .findDataById(eruptModel, BoxUtil.toEruptId(eruptModel, id)));
        if (joinColumn.contains(".")) {
            String[] jc = joinColumn.split("\\.");
            JsonObject jo2 = new JsonObject();
            jo2.addProperty(jc[1], val.toString());
            jo.add(jc[0], jo2);
        } else {
            jo.addProperty(joinColumn, val.toString());
        }
        UserMenuContext.register(new MenuContext(link.linkErupt().getSimpleName()));
        return eruptModifyController.addEruptData(link.linkErupt().getSimpleName(), data, jo, request);
    }

    private Link findDrillLink(Box erupt, String code) {
        return Stream.of(erupt.drills()).filter(it -> code.equals(it.code()))
                .findFirst().orElseThrow(BoxPermissionException::new).link();
    }


}
