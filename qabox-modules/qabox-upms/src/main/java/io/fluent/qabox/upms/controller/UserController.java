package io.fluent.qabox.upms.controller;

import com.google.gson.reflect.TypeToken;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.fluent.qabox.annotation.BoxWebRouter;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.config.prop.AppProp;
import io.fluent.qabox.upms.annotation.LoginProxy;
import io.fluent.qabox.upms.constant.SessionKey;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.model.base.LoginModel;
import io.fluent.qabox.upms.service.BoxContextService;
import io.fluent.qabox.upms.service.BoxSessionService;
import io.fluent.qabox.upms.service.BoxUserService;
import io.fluent.qabox.upms.util.IpUtil;
import io.fluent.qabox.upms.vo.EruptMenuVo;
import io.fluent.qabox.util.Boxes;
import io.fluent.qabox.view.BoxApiModel;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(BoxRestPath.ERUPT_API)
public class UserController {

    @Resource
    private BoxUserService boxUserService;

    @Resource
    private BoxSessionService sessionService;

    @Resource
    private AppProp eruptAppProp;

    @Resource
    private BoxContextService eruptContextService;

    @Resource
    private HttpServletRequest request;

    //登录
    @SneakyThrows
    @RequestMapping("/login")
    public LoginModel login(@RequestParam("account") String account, @RequestParam("pwd") String pwd,
                            @RequestParam(name = "verifyCode", required = false) String verifyCode
    ) {
        if (!boxUserService.checkVerifyCode(verifyCode)) {
            LoginModel loginModel = new LoginModel();
            loginModel.setUseVerifyCode(true);
            loginModel.setReason("验证码错误");
            loginModel.setPass(false);
            return loginModel;
        }
        LoginProxy loginProxy = BoxUserService.findEruptLogin();
        LoginModel loginModel;
        if (null == loginProxy) {
            loginModel = boxUserService.login(account, pwd);
        } else {
            loginModel = new LoginModel();
            try {
                BoxUser boxUser = loginProxy.login(account, pwd);
                if (null == boxUser) {
                    loginModel.setReason("账号或密码错误");
                    loginModel.setPass(false);
                } else {
                    loginModel.setBoxUser(boxUser);
                    loginModel.setPass(true);
                }
            } catch (Exception e) {
                if (0 == eruptAppProp.getVerifyCodeCount()) {
                    loginModel.setUseVerifyCode(true);
                }
                loginModel.setReason(e.getMessage());
                loginModel.setPass(false);
            }
        }
        if (loginModel.isPass()) {
            request.getSession().invalidate();
            BoxUser boxUser = loginModel.getBoxUser();
            loginModel.setToken(Boxes.generateCode(16));
            loginModel.setExpire(this.boxUserService.getExpireTime());
            loginModel.setResetPwd(null == boxUser.getResetPwdTime());
            boxUserService.putUserInfo(boxUser, loginModel.getToken());
            if (null != loginProxy) {
                loginProxy.loginSuccess(boxUser, loginModel.getToken());
            }
            boxUserService.cacheUserInfo(boxUser, loginModel.getToken());
            boxUserService.saveLoginLog(boxUser, loginModel.getToken()); //记录登录日志
        }
        return loginModel;
    }

    //获取菜单列表
    @GetMapping("/menu")
    @BoxWebRouter(verifyType = BoxWebRouter.VerifyType.LOGIN)
    public List<EruptMenuVo> getMenu() {
        return sessionService.get(SessionKey.MENU_VIEW + eruptContextService.getCurrentToken(), new TypeToken<List<EruptMenuVo>>() {
        }.getType());
    }

    //登出
    @PostMapping("/logout")
    @BoxWebRouter(verifyType = BoxWebRouter.VerifyType.LOGIN)
    public BoxApiModel logout(HttpServletRequest request) {
        String token = eruptContextService.getCurrentToken();
        LoginProxy loginProxy = BoxUserService.findEruptLogin();
        Optional.ofNullable(loginProxy).ifPresent(it -> it.logout(token));
        request.getSession().invalidate();
        for (String uk : SessionKey.USER_KEY_GROUP) {
            sessionService.remove(uk + token);
        }
        return BoxApiModel.successApi();
    }

    // 修改密码
    @PostMapping("/change-pwd")
    @BoxWebRouter(verifyType = BoxWebRouter.VerifyType.LOGIN)
    public BoxApiModel changePwd(@RequestParam("account") String account,
                                   @RequestParam("pwd") String pwd,
                                   @RequestParam("newPwd") String newPwd,
                                   @RequestParam("newPwd2") String newPwd2) {
        return boxUserService.changePwd(account, pwd, newPwd, newPwd2);
    }

    // 生成验证码
    @GetMapping
    @RequestMapping("/code-img")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg"); // 设置响应的类型格式为图片格式
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache"); // 禁止图像缓存。
        response.setHeader("Cache-Control", "no-cache");
        Captcha captcha = new SpecCaptcha(150, 38, 4);
        sessionService.put(SessionKey.VERIFY_CODE + IpUtil.getIpAddr(request), captcha.text(), 60, TimeUnit.SECONDS);
        captcha.out(response.getOutputStream());
    }

}
