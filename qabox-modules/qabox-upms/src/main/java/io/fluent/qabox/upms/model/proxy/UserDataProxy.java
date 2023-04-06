package io.fluent.qabox.upms.model.proxy;

import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.exception.ApiError;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.service.I18NTranslateService;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.service.BoxUserService;
import io.fluent.qabox.util.misc.MD5Util;
import io.fluent.qabox.view.BoxApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class UserDataProxy implements DataProxy<BoxUser> {

    @Resource
    private BoxDao eruptDao;

    @Resource
    private BoxUserService boxUserService;

    @Resource
    private I18NTranslateService i18NTranslateService;

    @Override
    public void beforeAdd(BoxUser boxUser) {
        if (StringUtils.isBlank(boxUser.getPasswordA())) {
            throw new ApiError(BoxApiModel.Status.WARNING, "密码必填", BoxApiModel.PromptWay.MESSAGE);
        }
        this.checkDataLegal(boxUser);
        if (boxUser.getPasswordA().equals(boxUser.getPasswordB())) {
            boxUser.setIsAdmin(false);
            boxUser.setCreateTime(new Date());
            if (boxUser.getIsMd5()) {
                boxUser.setPassword(MD5Util.digest(boxUser.getPasswordA()));
            } else {
                boxUser.setPassword(boxUser.getPasswordA());
            }
        } else {
            throw new WebApiRuntimeException(i18NTranslateService.translate("两次密码输入不一致"));
        }
    }

    @Override
    public void beforeUpdate(BoxUser boxUser) {
        eruptDao.getEntityManager().clear();
        BoxUser eu = eruptDao.getEntityManager().find(BoxUser.class, boxUser.getId());
        if (!boxUser.getIsMd5() && eu.getIsMd5()) {
            throw new WebApiRuntimeException(i18NTranslateService.translate("MD5不可逆", "MD5 irreversible"));
        }
        this.checkDataLegal(boxUser);
        if (StringUtils.isNotBlank(boxUser.getPasswordA())) {
            if (!boxUser.getPasswordA().equals(boxUser.getPasswordB())) {
                throw new WebApiRuntimeException(i18NTranslateService.translate("两次密码输入不一致"));
            }
            if (boxUser.getIsMd5()) {
                boxUser.setPassword(MD5Util.digest(boxUser.getPasswordA()));
            } else {
                boxUser.setPassword(boxUser.getPasswordA());
            }
        }
    }

    private void checkDataLegal(BoxUser boxUser) {
        if (boxUser.getBoxPosition() != null && boxUser.getBoxOrg() == null)
            throw new WebApiRuntimeException("选择岗位时，所属组织必填");
        BoxUser curr = boxUserService.getCurrentEruptUser();
        if (boxUser.getIsAdmin()) {
            if (null == curr.getIsAdmin() || !curr.getIsAdmin()) {
                throw new WebApiRuntimeException(i18NTranslateService.translate("当前用户非超管，无法创建超管用户！"));
            }
        }
    }
}
