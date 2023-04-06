package io.fluent.qabox.security.interceptor;

import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.config.prop.BoxProp;
import io.fluent.qabox.context.BoxUserContext;
import io.fluent.qabox.context.MenuContext;
import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.module.MetaUserinfo;
import io.fluent.qabox.security.config.BoxSecurityProp;
import io.fluent.qabox.security.service.OperationService;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.upms.config.UpmsProp;
import io.fluent.qabox.upms.constant.BoxReqHeaderConst;
import io.fluent.qabox.upms.constant.SessionKey;
import io.fluent.qabox.upms.service.BoxContextService;
import io.fluent.qabox.upms.service.BoxSessionService;
import io.fluent.qabox.upms.service.BoxUserService;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.view.BoxFieldModel;
import io.fluent.qabox.view.BoxModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Order
public class BoxSecurityInterceptor implements AsyncHandlerInterceptor {

  @Resource
  private BoxUserService eruptUserService;

  @Resource
  private EntityManager entityManager;

  @Resource
  private BoxProp eruptProp;

  @Resource
  private UpmsProp eruptUpmsProp;

  @Resource
  private BoxSecurityProp eruptSecurityProp;

  @Resource
  private BoxContextService eruptContextService;

  @Resource
  private OperationService operationService;

  private static final String ERUPT_PARENT_HEADER_KEY = "eruptParent";

  private static final String ERUPT_PARENT_PARAM_KEY = "_eruptParent";

  @Resource
  private BoxSessionService sessionService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
    BoxWebRouter eruptRouter = null;
    if (handler instanceof HandlerMethod) {
      eruptRouter = ((HandlerMethod) handler).getMethodAnnotation(BoxWebRouter.class);
    }
    if (null == eruptRouter) {
      return true;
    }
    String token = null;
    String eruptName = null;
    String parentEruptName = null;
    if (eruptRouter.verifyMethod() == BoxWebRouter.VerifyMethod.HEADER) {
      token = request.getHeader(BoxReqHeaderConst.ERUPT_HEADER_TOKEN);
      eruptName = request.getHeader(BoxReqHeaderConst.ERUPT_HEADER_KEY);
      parentEruptName = request.getHeader(ERUPT_PARENT_HEADER_KEY);
    } else if (eruptRouter.verifyMethod() == BoxWebRouter.VerifyMethod.PARAM) {
      token = request.getParameter(BoxReqHeaderConst.URL_ERUPT_PARAM_TOKEN);
      eruptName = request.getParameter(BoxReqHeaderConst.URL_ERUPT_PARAM_KEY);
      parentEruptName = request.getHeader(ERUPT_PARENT_PARAM_KEY);
    }
    if (eruptRouter.verifyType().equals(BoxWebRouter.VerifyType.ERUPT)) {
      UserMenuContext.register(new MenuContext(eruptName, eruptName));
      BoxModel erupt = BoxCoreService.getBoxModel(eruptName);
      if (null == erupt) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return false;
      }
      if (!erupt.getBox().authVerify()) {
        return true;
      }
    }
    if (null == token || null == sessionService.get(SessionKey.USER_TOKEN + token)) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.sendError(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
    MetaUserinfo metaUserinfo = eruptUserService.getSimpleUserInfo();
    UserMenuContext.registerToken(token);
    UserMenuContext.register(new BoxUserContext(metaUserinfo.getId() + "", metaUserinfo.getAccount(), metaUserinfo.getUsername()));
    //权限校验
    String authStr = request.getServletPath().split("/")[eruptRouter.skipAuthIndex() + eruptRouter.authIndex()];
    switch (eruptRouter.verifyType()) {
      case LOGIN:
        break;
      case MENU:
        if (!eruptRouter.verifyHandler().isInterface()) {
          authStr = IocUtil.getBean(eruptRouter.verifyHandler()).convertAuthStr(eruptRouter, request, authStr);
        }
        if (null == eruptUserService.getEruptMenuByValue(authStr)) {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.sendError(HttpStatus.FORBIDDEN.value());
          return false;
        }
        UserMenuContext.register(new MenuContext(null, authStr));
        break;
      case ERUPT:
        BoxModel eruptModel = BoxCoreService.getBoxModel(eruptName);
        $ep:
        if (StringUtils.isNotBlank(parentEruptName)) {
          BoxModel eruptParentModel = BoxCoreService.getBoxModel(parentEruptName);
          for (BoxFieldModel model : eruptParentModel.getEruptFieldModels()) {
            if (eruptModel.getEruptName().equals(model.getFieldReturnName())) {
              if (authStr.equals(eruptModel.getEruptName())) {
                authStr = eruptParentModel.getEruptName();
              }
              eruptModel = eruptParentModel;
              break $ep;
            }
          }
          for (RowOperation operation : eruptParentModel.getBox().rowOperation()) {
            if (void.class != operation.eruptClass()) {
              if (eruptModel.getEruptName().equals(operation.eruptClass().getSimpleName())) {
                authStr = eruptParentModel.getEruptName();
                eruptModel = eruptParentModel;
                break $ep;
              }
            }
          }
          response.setStatus(HttpStatus.NOT_FOUND.value());
          return false;
        }
        if (!authStr.equalsIgnoreCase(eruptModel.getEruptName())) {
          response.setStatus(HttpStatus.NOT_FOUND.value());
          return false;
        }
        if (null == eruptUserService.getEruptMenuByValue(eruptModel.getEruptName())) {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.sendError(HttpStatus.FORBIDDEN.value());
          return false;
        }
        break;
    }
    if (eruptProp.isRedisSessionRefresh()) {
      for (String uk : SessionKey.USER_KEY_GROUP) {
        sessionService.expire(uk + token, eruptUpmsProp.getExpireTimeByLogin(), TimeUnit.MINUTES);
      }
    }
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    try {
      operationService.record(handler, ex);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      UserMenuContext.remove();
    }
  }

}
