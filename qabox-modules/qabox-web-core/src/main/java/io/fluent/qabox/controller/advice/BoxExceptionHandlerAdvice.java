package io.fluent.qabox.controller.advice;

import io.fluent.qabox.exception.ApiError;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.view.BoxApiModel;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class BoxExceptionHandlerAdvice {


    @ResponseBody
    @ExceptionHandler(WebApiRuntimeException.class)
    public Object eruptWebApiRuntimeException(WebApiRuntimeException e, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e.isPrintStackTrace()) e.printStackTrace();
        map.put("error", "erupt web api error");
        map.put("message", e.getMessage());
        map.put("timestamp", new Date());
        map.put("path", request.getServletPath());
        map.put("status", response.getStatus());
        Optional.ofNullable(e.getExtraMap()).ifPresent(map::putAll);
        return map;
    }

    @ExceptionHandler(ApiError.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public BoxApiModel eruptApiErrorTip(ApiError e) {
        e.apiModel.setErrorIntercept(false);
        return e.apiModel;
    }

}
