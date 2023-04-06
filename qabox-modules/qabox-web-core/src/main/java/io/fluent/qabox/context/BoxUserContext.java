package io.fluent.qabox.context;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BoxUserContext {

    private String uid; //用户id

    private String account; //登录用户名

    private String name; //用户姓名

    public BoxUserContext(String uid, String account, String name) {
        this.uid = uid;
        this.account = account;
        this.name = name;
    }

    public BoxUserContext() {
    }
}
