package io.fluent.qabox.component.naming;

import io.fluent.qabox.annotation.RecordOperate;
import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.service.BoxCoreService;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;


@Component
public class BoxRecordNaming implements RecordOperate.DynamicConfig {

    @Override
    public String naming(String desc, String menuName, String eruptName, Method method) {
        BoxWebRouter eruptRouter = method.getAnnotation(BoxWebRouter.class);
        if (null != eruptRouter && eruptRouter.verifyType() == BoxWebRouter.VerifyType.ERUPT) {
            String prefix = desc + " | ";
            if (null != menuName) {
                return prefix + menuName;
            } else if (null != BoxCoreService.getBoxModel(eruptName)) {
                return prefix + BoxCoreService.getBoxModel(eruptName).getBox().name();
            } else {
                return prefix + eruptName;
            }
        } else {
            throw new RuntimeException("incorrect use " + BoxRecordNaming.class.getSimpleName());
        }
    }

}