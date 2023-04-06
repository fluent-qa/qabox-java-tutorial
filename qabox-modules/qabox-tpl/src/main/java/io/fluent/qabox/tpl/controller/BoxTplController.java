package io.fluent.qabox.tpl.controller;


import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.exception.BoxPermissionException;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.frontend.operation.Tpl;
import io.fluent.qabox.processor.DataProcessorManager;
import io.fluent.qabox.processor.invoker.ExprResolverInvoke;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.tpl.annotation.TplAction;
import io.fluent.qabox.tpl.annotation.UITemplate;
import io.fluent.qabox.tpl.engine.EngineConst;
import io.fluent.qabox.tpl.service.BoxTplService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.Boxes;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.view.BoxModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.fluent.qabox.config.constant.BoxRestPath.ERUPT_API;


/**
 *  页面结构构建信息
 */
@RestController
@RequestMapping(ERUPT_API + BoxTplController.TPL)
public class BoxTplController implements BoxWebRouter.VerifyHandler {

    static final String TPL = "/tpl";

    @Resource
    private BoxTplService boxTplService;

    @GetMapping(value = "/**", produces = {"text/html;charset=utf-8"})
    @BoxWebRouter(authIndex = 1, verifyType = BoxWebRouter.VerifyType.MENU, verifyHandler = BoxTplController.class,
            verifyMethod = BoxWebRouter.VerifyMethod.PARAM)
    public void eruptTplPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String path = request.getRequestURI().split(ERUPT_API + BoxTplController.TPL + "/")[1];
        Method method = boxTplService.getAction(path);
        if (null == method) {
            boxTplService.tplRender(Tpl.Engine.Native, TPL + "/" + path, null, response.getWriter());
            return;
        }
        Object obj = IocUtil.getBean(method.getDeclaringClass());
        UITemplate eruptTpl = obj.getClass().getAnnotation(UITemplate.class);
        TplAction tplAction = method.getAnnotation(TplAction.class);
        path = TPL + "/" + path;
        if (StringUtils.isNotBlank(tplAction.path())) {
            path = tplAction.path();
        }
        boxTplService.tplRender(eruptTpl.engine(), path, (Map) method.invoke(obj), response.getWriter());
    }

    @Override
    public String convertAuthStr(BoxWebRouter eruptRouter, HttpServletRequest request, String authStr) {
        return request.getRequestURI().split(ERUPT_API + BoxTplController.TPL + "/")[1];
    }

    @GetMapping(value = "/html-field/{erupt}/{field}", produces = {"text/html;charset=UTF-8"})
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.MENU, verifyMethod = BoxWebRouter.VerifyMethod.PARAM)
    public void getEruptFieldHtml(@PathVariable("erupt") String eruptName, @PathVariable("field") String field, HttpServletResponse response) {
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Tpl tpl = eruptModel.getEruptFieldMap().get(field).getUiField().edit().tplType();
        boxTplService.tplRender(tpl, null, response);
    }

    @GetMapping(value = "/operation_tpl/{erupt}/{code}", produces = {"text/html;charset=utf-8"})
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT, verifyMethod = BoxWebRouter.VerifyMethod.PARAM)
    public void getOperationTpl(@PathVariable("erupt") String eruptName, @PathVariable("code") String code,
                                @RequestParam(value = "ids", required = false) String[] ids, HttpServletResponse response) {
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
        RowOperation operation = Arrays.stream(eruptModel.getBox().rowOperation()).filter(it ->
                it.code().equals(code)).findFirst().orElseThrow(BoxPermissionException::new);
        Boxes.powerLegal(ExprResolverInvoke.getExpr(operation.show()));
        if (operation.tpl().engine() == Tpl.Engine.Native || operation.mode() == RowOperation.Mode.BUTTON) {
            boxTplService.tplRender(operation.tpl(), null, response);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(EngineConst.INJECT_ROWS, Stream.of(ids).map(id -> DataProcessorManager.getDataProcessor(eruptModel.getClazz())
                .findDataById(eruptModel, BoxUtil.toEruptId(eruptModel, id))).collect(Collectors.toList()));
        boxTplService.tplRender(operation.tpl(), map, response);
    }
}
