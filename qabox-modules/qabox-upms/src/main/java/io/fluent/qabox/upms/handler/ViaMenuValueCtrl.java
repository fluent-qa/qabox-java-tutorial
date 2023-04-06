package io.fluent.qabox.upms.handler;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.expr.ExprBool;
import io.fluent.qabox.upms.service.BoxUserService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import static org.apache.commons.lang3.Validate.notNull;


@Service
@Comment("通过菜单类型值控制是否显示")
public class ViaMenuValueCtrl implements ExprBool.ExprHandler {

    @Resource
    private BoxUserService boxUserService;

    @Override
    @Comment("params必填，值为菜单类型值")
    public boolean handler(boolean expr, String[] params) {
        notNull(params,ViaMenuValueCtrl.class.getSimpleName() + " → params[0] not found");
        return null != boxUserService.getEruptMenuByValue(params[0]);
    }

}
