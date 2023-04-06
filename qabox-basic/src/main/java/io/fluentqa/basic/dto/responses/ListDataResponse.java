package io.fluentqa.basic.dto.responses;


import io.fluentqa.basic.dto.GenericResponse;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListDataResponse<T> extends GenericResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private Collection<T> data;

    public List<T> getData() {
        if (null == data) {
            return Collections.emptyList();
        }
        if (data instanceof List) {
            return (List<T>) data;
        }
        return new ArrayList<>(data);
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public static ListDataResponse buildSuccess() {
        ListDataResponse response = new ListDataResponse();
        response.setSuccess(true);
        return response;
    }

    public static ListDataResponse buildFailure(String errCode, String errMessage) {
        ListDataResponse response = new ListDataResponse();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static <T> ListDataResponse<T> of(Collection<T> data) {
        ListDataResponse<T> response = new ListDataResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

}