package io.fluent.qabox.upms.model.online;

import io.fluent.qabox.config.prop.BoxProp;
import io.fluent.qabox.frontend.OperationHandler;
import io.fluent.qabox.upms.constant.SessionKey;
import io.fluent.qabox.upms.service.BoxSessionService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;


@Service
public class LogOutOperationHandler implements OperationHandler<UserOnline, Void> {

    @Resource
    private BoxProp eruptProp;

    @Resource
    private BoxSessionService eruptSessionService;

    @Override
    public String exec(List<UserOnline> data, Void v, String[] param) {
        if (eruptProp.isRedisSession()) {
            data.forEach(it -> eruptSessionService.remove(SessionKey.USER_TOKEN + it.getToken()));
        }
        return null;
    }

}
