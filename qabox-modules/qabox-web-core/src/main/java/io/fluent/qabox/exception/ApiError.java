package io.fluent.qabox.exception;


import io.fluent.qabox.view.BoxApiModel;

public class ApiError extends RuntimeException {

    public BoxApiModel apiModel;

    public ApiError(String message) {
        apiModel = BoxApiModel.errorApi(message);
    }

    public ApiError(String message, BoxApiModel.PromptWay promptWay) {
        this.apiModel = new BoxApiModel(BoxApiModel.Status.ERROR, message, promptWay);
    }

    public ApiError(BoxApiModel.Status status, String message, BoxApiModel.PromptWay promptWay) {
        this.apiModel = new BoxApiModel(status, message, promptWay);
    }

    public ApiError(BoxApiModel eruptApiModel) {
        this.apiModel = eruptApiModel;
    }

}
