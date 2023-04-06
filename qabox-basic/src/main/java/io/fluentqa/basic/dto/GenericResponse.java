package io.fluentqa.basic.dto;

import java.io.Serial;

public class GenericResponse extends GenericDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean success;

    private String errCode;

    private String errMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    @Override
    public String toString() {
        return "Response [success=" + success + ", errCode=" + errCode + ", errMessage=" + errMessage + "]";
    }

    public static GenericResponse buildSuccess() {
        GenericResponse response = new GenericResponse();
        response.setSuccess(true);
        return response;
    }

    public static GenericResponse failure(String errCode, String errMessage) {
        GenericResponse response = new GenericResponse();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

}
