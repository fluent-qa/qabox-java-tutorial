package io.fluent.qabox.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProp {

    public static final String DEFAULT_LANG = "zh-CN";

    //登录失败几次出现验证码
    private Integer verifyCodeCount = 2;

    //登录密码是否加密传输
    private Boolean pwdTransferEncrypt = true;

    //自定义登录页路径
    private String loginPagePath;

    //新用户强制修改密码
    private boolean forceResetPwd = true;

    private Integer hash;

    private String version;

    //多语言配置
    private String[] locales = {
            DEFAULT_LANG, //简体中文
            "zh-TW",      //繁体中文
            "en-US",      //English
            "ja-JP",      //日本語
            "ko-KR"       //한국어
    };

}
