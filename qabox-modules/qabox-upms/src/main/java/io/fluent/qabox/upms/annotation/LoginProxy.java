package io.fluent.qabox.upms.annotation;


import io.fluent.qabox.config.Comment;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.model.base.LoginModel;
import io.fluent.qabox.upms.service.BoxUserService;
import io.fluent.qabox.util.di.IocUtil;

public interface LoginProxy {

    @Comment("登录校验，如要提示校验结果请抛异常")
    @Comment("为安全考虑pwd是加密的，加密逻辑：md5(md5(pwd)+ Calendar.DAY_OF_MONTH +account)")
    @Comment("Calendar.DAY_OF_MONTH → Calendar.getInstance().get(Calendar.DAY_OF_MONTH) //今天月的哪一天")
    @Comment("如果不希望加密，请前往配置文件，将：erupt-app.pwdTransferEncrypt 设置为 false 即可")
    default BoxUser login(String account, String pwd) {
        LoginModel loginModel = IocUtil.getBean(BoxUserService.class).login(account, pwd);
        if (loginModel.isPass()) {
            return loginModel.getBoxUser();
        } else {
            throw new RuntimeException(loginModel.getReason());
        }
    }

    @Comment("登录成功")
    default void loginSuccess(BoxUser boxUser, String token) {
    }

    @Comment("注销事件")
    default void logout(String token) {

    }

    @Comment("修改密码")
    default void beforeChangePwd(BoxUser boxUser, String newPwd) {

    }

    @Comment("完成修改密码")
    default void afterChangePwd(BoxUser boxUser, String originPwd, String newPwd) {
    }

}
