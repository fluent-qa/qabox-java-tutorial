package io.fluent.qabox.controller;

import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.exception.ApiError;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.frontend.field.sub_edit.AutoCompleteType;
import io.fluent.qabox.frontend.fun.VLModel;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.view.BoxApiModel;
import io.fluent.qabox.view.BoxFieldModel;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(BoxRestPath.ERUPT_COMP)
public class BoxComponentController {

    /**
     * 自动完成组件联动接口
     *
     * @param field    自动完成组件字段
     * @param val      输入框的值
     * @param formData 完整表单对象
     * @return 联想结果
     */
    @PostMapping("/auto-complete/{erupt}/{field}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public List<Object> findAutoCompleteValue(@PathVariable("erupt") String eruptName,
                                              @PathVariable("field") String field,
                                              @RequestParam("val") String val,
                                              @RequestBody(required = false) Map<String, Object> formData) {
        BoxFieldModel fieldModel = BoxCoreService.getBoxModel(eruptName).getEruptFieldMap().get(field);
        AutoCompleteType autoCompleteType = fieldModel.getUiField().edit().autoCompleteType();
        if (val.length() < autoCompleteType.triggerLength()) {
            throw new WebApiRuntimeException("char length must >= " + autoCompleteType.triggerLength());
        }
        try {
            return IocUtil.getBean(autoCompleteType.handler()).completeHandler(formData, val, autoCompleteType.param());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError(e.getMessage(), BoxApiModel.PromptWay.MESSAGE);
        }
    }

    //Gets the CHOICE component drop-down list
    @RequestMapping("/choice-item/{erupt}/{field}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public List<VLModel> findChoiceItem(@PathVariable("erupt") String eruptName,
                                        @PathVariable("field") String field) {
        BoxFieldModel fieldModel = BoxCoreService.getBoxModel(eruptName).getEruptFieldMap().get(field);
        return BoxUtil.getChoiceList(fieldModel.getUiField().edit().choiceType());
    }

    //Gets the TAGS component list data
    @RequestMapping("/tags-item/{erupt}/{field}")
    @BoxWebRouter(authIndex = 2, verifyType = BoxWebRouter.VerifyType.ERUPT)
    public List<String> findTagsItem(@PathVariable("erupt") String eruptName,
                                     @PathVariable("field") String field) {
        BoxFieldModel fieldModel = BoxCoreService.getBoxModel(eruptName).getEruptFieldMap().get(field);
        return BoxUtil.getTagList(fieldModel.getUiField().edit().tagsType());
    }

}
