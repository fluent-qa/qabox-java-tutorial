package io.fluent.qabox.view;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BoxApiModel {

    private Status status;

    private PromptWay promptWay;

    private String message;

    private Object data;

    private boolean errorIntercept = true;

    public BoxApiModel(Status status, String message, Object data, PromptWay promptWay) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.promptWay = promptWay;
    }

    public BoxApiModel(Status status, String message, PromptWay promptWay) {
        this.status = status;
        this.message = message;
        this.promptWay = promptWay;
    }

    public static BoxApiModel successApi() {
        return new BoxApiModel(Status.SUCCESS, null, null, PromptWay.MESSAGE);
    }

    public static BoxApiModel successApi(String message, Object data) {
        return new BoxApiModel(Status.SUCCESS, message, data, PromptWay.MESSAGE);
    }

    public static BoxApiModel successApi(Object data) {
        return new BoxApiModel(Status.SUCCESS, null, data, PromptWay.MESSAGE);
    }

    public static BoxApiModel errorApi(String message) {
        return new BoxApiModel(Status.ERROR, message, null, PromptWay.DIALOG);
    }

    public static BoxApiModel errorNoInterceptApi(String message) {
        BoxApiModel eruptApiModel = new BoxApiModel(Status.ERROR, message, null, PromptWay.DIALOG);
        eruptApiModel.errorIntercept = false;
        return eruptApiModel;
    }

    public static BoxApiModel errorNoInterceptMessage(String message) {
        BoxApiModel eruptApiModel = new BoxApiModel(Status.ERROR, message, null, PromptWay.MESSAGE);
        eruptApiModel.errorIntercept = false;
        return eruptApiModel;
    }

    public static BoxApiModel errorApi(Exception e) {
        e.printStackTrace();
        return new BoxApiModel(Status.ERROR, e.getMessage(), null, PromptWay.DIALOG);
    }

    public enum Status {
        SUCCESS, ERROR, INFO, WARNING
    }

    public enum PromptWay {
        DIALOG, MESSAGE, NOTIFY, NONE
    }

}


