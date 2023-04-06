package io.fluent.qabox.security.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReqBody {

    private long date;

    private Object body;

    public ReqBody() {
    }
}
