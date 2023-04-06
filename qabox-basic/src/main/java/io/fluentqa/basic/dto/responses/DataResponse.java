package io.fluentqa.basic.dto.responses;


import io.fluentqa.basic.dto.GenericResponse;

import java.io.Serial;

public class DataResponse<T> extends GenericResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static DataResponse buildSuccess() {
        DataResponse response = new DataResponse();
        response.setSuccess(true);
        return response;
    }

    public static DataResponse buildFailure(String errCode, String errMessage) {
        DataResponse response = new DataResponse();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static <T> DataResponse<T> of(T data) {
        DataResponse<T> response = new DataResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

}