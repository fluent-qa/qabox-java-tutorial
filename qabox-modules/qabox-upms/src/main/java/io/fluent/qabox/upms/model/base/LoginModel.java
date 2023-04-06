package io.fluent.qabox.upms.model.base;

import io.fluent.qabox.upms.model.BoxMenu;
import io.fluent.qabox.upms.model.BoxUser;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;


@Getter
@Setter
public class LoginModel {

    private transient BoxUser boxUser;

    private boolean useVerifyCode = false; //是否需要验证码

    private boolean pass; //校验是否通过

    private boolean resetPwd = false; //重置密码

    private String reason;  //未校验通过原因

    private String token;

    //--------------------------

    private LocalDateTime expire;

    private String userName;

    private String indexMenu;

    public LoginModel(boolean pass, BoxUser boxUser) {
        this.pass = pass;
        this.setBoxUser(boxUser);
    }

    public LoginModel(boolean pass, String reason) {
        this.pass = pass;
        this.reason = reason;
    }

    public LoginModel(boolean pass, String reason, boolean useVerifyCode) {
        this.pass = pass;
        this.reason = reason;
        this.useVerifyCode = useVerifyCode;
    }

    public LoginModel() {
    }

    public void setBoxUser(BoxUser boxUser) {
        this.boxUser = boxUser;
        BoxMenu indexMenu = boxUser.getBoxMenu();
        this.setUserName(boxUser.getName());
        if (null != indexMenu) {
            this.setIndexMenu(indexMenu.getType() + "||" + indexMenu.getValue());
        }
    }
}
