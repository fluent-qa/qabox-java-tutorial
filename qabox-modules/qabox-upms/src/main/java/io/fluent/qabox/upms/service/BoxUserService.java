package io.fluent.qabox.upms.service;

import com.google.gson.Gson;
import eu.bitwalker.useragentutils.UserAgent;
import io.fluent.qabox.config.GsonFactory;
import io.fluent.qabox.config.prop.AppProp;
import io.fluent.qabox.config.prop.BoxProp;
import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.module.MetaUserinfo;
import io.fluent.qabox.service.BoxAppBeanService;
import io.fluent.qabox.upms.annotation.EruptLogin;
import io.fluent.qabox.upms.annotation.LoginProxy;
import io.fluent.qabox.upms.config.UpmsProp;
import io.fluent.qabox.upms.constant.SessionKey;
import io.fluent.qabox.upms.model.BoxMenu;
import io.fluent.qabox.upms.model.BoxRole;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.model.base.LoginModel;
import io.fluent.qabox.upms.model.log.LoginLog;
import io.fluent.qabox.upms.util.IpUtil;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.util.misc.MD5Util;
import io.fluent.qabox.view.BoxApiModel;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BoxUserService {

    @Resource
    private BoxSessionService sessionService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private BoxDao eruptDao;

    @Resource
    private AppProp eruptAppProp;

    @Resource
    private BoxProp eruptProp;

    @Resource
    private UpmsProp eruptUpmsProp;

    @Resource
    private BoxContextService eruptContextService;

    @Resource
    private BoxMenuService boxMenuService;

    private final Gson gson = GsonFactory.getGson();

    public static final String LOGIN_ERROR_HINT = "账号或密码错误";

    public void cacheUserInfo(BoxUser boxUser, String token) {
        List<BoxMenu> boxMenus = boxMenuService.getUserAllMenu(boxUser);
        Map<String, Object> valueMap = new HashMap<>();
        for (BoxMenu menu : boxMenus) {
            if (null != menu.getValue()) {
                valueMap.put(menu.getValue().toLowerCase(), menu);
            }
        }
        sessionService.putMap(SessionKey.MENU_VALUE_MAP + token, valueMap, eruptUpmsProp.getExpireTimeByLogin());
        sessionService.put(SessionKey.MENU_VIEW + token, gson.toJson(boxMenuService.geneMenuListVo(boxMenus)), eruptUpmsProp.getExpireTimeByLogin());
    }

    public void putUserInfo(BoxUser boxUser, String token) {
        MetaUserinfo metaUserinfo = new MetaUserinfo();
        metaUserinfo.setId(boxUser.getId());
        metaUserinfo.setSuperAdmin(boxUser.getIsAdmin());
        metaUserinfo.setAccount(boxUser.getAccount());
        metaUserinfo.setUsername(boxUser.getName());
        metaUserinfo.setRoles(boxUser.getRoles().stream().map(BoxRole::getCode).collect(Collectors.toList()));
        Optional.ofNullable(boxUser.getBoxPosition()).ifPresent(it -> metaUserinfo.setPost(it.getCode()));
        Optional.ofNullable(boxUser.getBoxOrg()).ifPresent(it -> metaUserinfo.setOrg(it.getCode()));
        sessionService.put(SessionKey.USER_TOKEN + token, gson.toJson(metaUserinfo), eruptUpmsProp.getExpireTimeByLogin());
    }

    public static LoginProxy findEruptLogin() {
        if (null == BoxAppBeanService.getPrimarySource()) {
            throw new RuntimeException("Not found '@EruptScan' Annotation");
        }
        EruptLogin eruptLogin = BoxAppBeanService.getPrimarySource().getAnnotation(EruptLogin.class);
        if (null != eruptLogin) {
            return IocUtil.getBean(eruptLogin.value());
        }
        return null;
    }

    private boolean loginErrorCountPlus(String ip) {
        Object loginError = sessionService.get(SessionKey.LOGIN_ERROR + ip);
        int loginErrorCount = 0;
        if (null != loginError) {
            loginErrorCount = Integer.parseInt(loginError.toString());
        }
        sessionService.put(SessionKey.LOGIN_ERROR + ip, ++loginErrorCount + "", eruptUpmsProp.getExpireTimeByLogin());
        return loginErrorCount >= eruptAppProp.getVerifyCodeCount();
    }

    public LoginModel login(String account, String pwd) {
        String requestIp = IpUtil.getIpAddr(request);
        BoxUser boxUser = this.findEruptUserByAccount(account);
        if (null != boxUser) {
            if (!boxUser.getStatus()) {
                return new LoginModel(false, "账号已锁定!");
            }
            if (StringUtils.isNotBlank(boxUser.getWhiteIp())) {
                if (Arrays.stream(boxUser.getWhiteIp().split("\n")).noneMatch(ip -> ip.equals(requestIp))) {
                    return new LoginModel(false, "当前 ip 无权访问");
                }
            }
            if (checkPwd(boxUser, pwd)) {
                request.getSession().invalidate();
                sessionService.put(SessionKey.LOGIN_ERROR + requestIp, "0", eruptUpmsProp.getExpireTimeByLogin());
                return new LoginModel(true, boxUser);
            } else {
                return new LoginModel(false, LOGIN_ERROR_HINT, loginErrorCountPlus(requestIp));
            }
        } else {
            return new LoginModel(false, LOGIN_ERROR_HINT, loginErrorCountPlus(requestIp));
        }
    }

    //校验密码
    public boolean checkPwd(BoxUser boxUser, String pwd) {
        if (eruptAppProp.getPwdTransferEncrypt()) {
            String digestPwd = boxUser.getIsMd5() ? boxUser.getPassword() : MD5Util.digest(boxUser.getPassword());
            String calcPwd = MD5Util.digest(digestPwd + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + boxUser.getAccount());
            return pwd.equalsIgnoreCase(calcPwd);
        } else {
            if (boxUser.getIsMd5()) pwd = MD5Util.digest(pwd);
            return pwd.equals(boxUser.getPassword());
        }
    }

    public LocalDateTime getExpireTime() {
        if (eruptProp.isRedisSession()) {
            return LocalDateTime.now().plusMinutes(eruptUpmsProp.getExpireTimeByLogin());
        } else {
            return LocalDateTime.now().plusSeconds(request.getSession().getMaxInactiveInterval());
        }
    }

    public boolean checkVerifyCode(String verifyCode) {
        String requestIp = IpUtil.getIpAddr(request);
        Object loginError = sessionService.get(SessionKey.LOGIN_ERROR + requestIp);
        long loginErrorCount = 0;
        if (null != loginError) {
            loginErrorCount = Long.parseLong(loginError.toString());
        }
        if (loginErrorCount >= eruptAppProp.getVerifyCodeCount()) {
            if (StringUtils.isBlank(verifyCode)) {
                return false;
            }
            Object vc = sessionService.get(SessionKey.VERIFY_CODE + requestIp);
            sessionService.remove(SessionKey.VERIFY_CODE + requestIp);
            return vc != null && vc.toString().equalsIgnoreCase(verifyCode);
        }
        return true;
    }

    @Transactional
    public void saveLoginLog(BoxUser user, String token) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        LoginLog loginLog = new LoginLog();
        loginLog.setToken(token);
        loginLog.setUserName(user.getName());
        loginLog.setLoginTime(new Date());
        loginLog.setIp(IpUtil.getIpAddr(request));
        loginLog.setSystemName(userAgent.getOperatingSystem().getName());
        loginLog.setRegion(IpUtil.getCityInfo(loginLog.getIp()));
        loginLog.setBrowser(userAgent.getBrowser().getName() + " " + (userAgent.getBrowserVersion() == null ? "" : userAgent.getBrowserVersion().getMajorVersion()));
        loginLog.setDeviceType(userAgent.getOperatingSystem().getDeviceType().getName());
        eruptDao.getEntityManager().persist(loginLog);
    }

    @Transactional
    public BoxApiModel changePwd(String account, String pwd, String newPwd, String newPwd2) {
        if (!newPwd.equals(newPwd2)) {
            return BoxApiModel.errorNoInterceptMessage("修改失败，新密码与确认密码不匹配");
        }
        BoxUser boxUser = findEruptUserByAccount(account);
        LoginProxy loginProxy = BoxUserService.findEruptLogin();
        if (null != loginProxy) {
            loginProxy.beforeChangePwd(boxUser, newPwd);
        }
        if (boxUser.getIsMd5()) {
            pwd = MD5Util.digest(pwd);
            newPwd = MD5Util.digest(newPwd);
        }
        if (boxUser.getPassword().equals(pwd)) {
            if (newPwd.equals(boxUser.getPassword())) {
                return BoxApiModel.errorNoInterceptMessage("修改失败，新密码不能和原始密码一样");
            }
            boxUser.setPassword(newPwd);
            boxUser.setResetPwdTime(new Date());
            eruptDao.getEntityManager().merge(boxUser);
            if (null != loginProxy) {
                loginProxy.afterChangePwd(boxUser, pwd, newPwd);
            }
            return BoxApiModel.successApi();
        } else {
            return BoxApiModel.errorNoInterceptMessage("密码错误");
        }
    }

    private BoxUser findEruptUserByAccount(String account) {
        return eruptDao.queryEntity(BoxUser.class, "account = :account", new HashMap<String, Object>(1) {{
            this.put("account", account);
        }});
    }

    //从当前用户菜单中，通过菜单类型值获取菜单
    public BoxMenu getEruptMenuByValue(String menuValue) {
        return sessionService.getMapValue(SessionKey.MENU_VALUE_MAP + eruptContextService.getCurrentToken(), menuValue.toLowerCase(), BoxMenu.class);
    }

    public List<String> getEruptMenuValues() {
        return sessionService.getMapKeys(SessionKey.MENU_VALUE_MAP + eruptContextService.getCurrentToken());
    }

    public Map<String, Boolean> getEruptMenuValuesMap() {
        return getEruptMenuValues().stream().collect(Collectors.toMap(it -> it, it -> true));
    }

    //获取当前用户ID
    public Long getCurrentUid() {
        MetaUserinfo metaUserinfo = getSimpleUserInfo();
        return null == metaUserinfo ? null : metaUserinfo.getId();
    }

    //获取当前登录用户基础信息（缓存中查找）
    public MetaUserinfo getSimpleUserInfo() {
        Object info = sessionService.get(SessionKey.USER_TOKEN + eruptContextService.getCurrentToken());
        return null == info ? null : gson.fromJson(info.toString(), MetaUserinfo.class);
    }

    public MetaUserinfo getSimpleUserInfoByToken(String token) {
        Object info = sessionService.get(SessionKey.USER_TOKEN + token);
        return null == info ? null : gson.fromJson(info.toString(), MetaUserinfo.class);
    }

    //获取当前登录用户对象(数据库中查找)
    public BoxUser getCurrentEruptUser() {
        Long uid = this.getCurrentUid();
        return null == uid ? null : eruptDao.getEntityManager().find(BoxUser.class, uid);
    }

}
