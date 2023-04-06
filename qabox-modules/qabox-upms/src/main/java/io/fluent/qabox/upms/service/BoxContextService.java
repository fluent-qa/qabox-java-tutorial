package io.fluent.qabox.upms.service;

import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.context.MenuContext;
import io.fluent.qabox.upms.constant.BoxReqHeaderConst;
import io.fluent.qabox.upms.constant.SessionKey;
import io.fluent.qabox.upms.model.BoxMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Service
public class BoxContextService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private BoxSessionService sessionService;


    //获取当前请求token
    public String getCurrentToken() {
        String token = request.getHeader(BoxReqHeaderConst.ERUPT_HEADER_TOKEN);
        return StringUtils.isNotBlank(token) ? token : request.getParameter(BoxReqHeaderConst.URL_ERUPT_PARAM_TOKEN);
    }

    //获取当前菜单对象
    public BoxMenu getCurrentEruptMenu() {
        MenuContext metaErupt = UserMenuContext.getMenuContext();
        return sessionService.getMapValue(SessionKey.MENU_VALUE_MAP + getCurrentToken()
                , (metaErupt.getMenuValue() == null ? metaErupt.getName() : metaErupt.getMenuValue()).toLowerCase(),
                BoxMenu.class);
    }

}
