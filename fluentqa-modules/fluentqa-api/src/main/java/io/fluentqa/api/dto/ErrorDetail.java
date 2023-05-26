package io.fluentqa.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ErrorDetail implements Serializable {

    private static final long serialVersionUID = 2616876803298549771L;

    private int code;

    private String message;

    private String type;
}