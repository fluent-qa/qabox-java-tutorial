package io.fluent.qabox.security.service;

import io.fluent.qabox.annotation.RecordOperate;
import io.fluent.qabox.context.UserMenuContext;
import io.fluent.qabox.security.config.BoxSecurityProp;
import io.fluent.qabox.security.tl.RequestBodyTL;
import io.fluent.qabox.upms.constant.BoxReqHeaderConst;
import io.fluent.qabox.upms.model.BoxMenu;
import io.fluent.qabox.upms.model.log.OperationLog;
import io.fluent.qabox.upms.service.BoxContextService;
import io.fluent.qabox.upms.util.IpUtil;
import io.fluent.qabox.util.di.IocUtil;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;


@Service
public class OperationService {

    @Resource
    private EntityManager entityManager;

    @Resource
    private BoxSecurityProp boxSecurityProp;

    @Resource
    private HttpServletRequest request;

    @Resource
    private BoxContextService eruptContextService;

    @Transactional
    public void record(Object handler, Exception ex) {
        if (boxSecurityProp.isRecordOperateLog()) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Optional.ofNullable(handlerMethod.getMethodAnnotation(RecordOperate.class)).ifPresent(eruptRecordOperate -> {
                    OperationLog operate = new OperationLog();
                    if (eruptRecordOperate.dynamicConfig().isInterface()) {
                        operate.setApiName(eruptRecordOperate.value());
                    } else {
                        String eruptName = request.getHeader(BoxReqHeaderConst.ERUPT_HEADER_KEY);
                        eruptName = Optional.ofNullable(eruptName).orElse(request.getParameter(BoxReqHeaderConst.URL_ERUPT_PARAM_KEY));
                        RecordOperate.DynamicConfig dynamicConfig = IocUtil.getBean(eruptRecordOperate.dynamicConfig());
                        if (!dynamicConfig.canRecord(eruptName, handlerMethod.getMethod())) return;
                        operate.setApiName(dynamicConfig.naming(eruptRecordOperate.value(),
                                Optional.ofNullable(eruptContextService.getCurrentEruptMenu()).orElse(new BoxMenu()).getName(),
                                eruptName, handlerMethod.getMethod()));
                    }
                    operate.setIp(IpUtil.getIpAddr(request));
                    operate.setRegion(IpUtil.getCityInfo(operate.getIp()));
                    operate.setStatus(true);
                    operate.setReqMethod(request.getMethod());
                    operate.setReqAddr(request.getRequestURL().toString());
                    operate.setOperateUser(UserMenuContext.getUser().getName());
                    operate.setCreateTime(new Date());
                    operate.setTotalTime(operate.getCreateTime().getTime() - RequestBodyTL.get().getDate());
                    Optional.ofNullable(ex).ifPresent(e -> {
                        operate.setErrorInfo(ExceptionUtils.getStackTrace(e));
                        operate.setStatus(false);
                    });
                    Object param = RequestBodyTL.get().getBody();
                    operate.setReqParam(null == param ? findRequestParamVal(request) : param.toString());
                    RequestBodyTL.remove();
                    entityManager.persist(operate);
                });
            }
        }
    }

    private String findRequestParamVal(HttpServletRequest request) {
        if (request.getParameterMap().size() > 0) {
            StringBuilder sb = new StringBuilder();
            request.getParameterMap().forEach((key, value) -> sb.append(key).append("=").append(Arrays.toString(value)).append("\n"));
            return sb.toString();
        }
        return null;
    }

}
