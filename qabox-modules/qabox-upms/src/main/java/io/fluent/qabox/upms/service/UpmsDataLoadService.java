package io.fluent.qabox.upms.service;

import io.fluent.qabox.config.constant.BoxConst;
import io.fluent.qabox.config.constant.MenuTypeEnum;
import io.fluent.qabox.config.prop.BoxProp;
import io.fluent.qabox.config.prop.InitMethodEnum;
import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.frontend.operation.UIPermission;
import io.fluent.qabox.module.BoxModuleInvoke;
import io.fluent.qabox.module.MetaMenu;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.upms.enums.FunPermission;
import io.fluent.qabox.upms.model.BoxMenu;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.util.UPMSUtil;
import io.fluent.qabox.util.ProjectUtil;
import io.fluent.qabox.util.misc.MD5Util;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Order
@Slf4j
public class UpmsDataLoadService implements CommandLineRunner {

    @Resource
    private BoxDao eruptDao;

    @Resource
    private BoxProp eruptProp;

    public static final String DEFAULT_ACCOUNT = "erupt";

    @Transactional
    @Override
    public void run(String... args) {
        Optional.ofNullable(eruptDao.getJdbcTemplate()
                .queryForObject("select count(*) from upms_user", Integer.class)).ifPresent(it -> {
            if (it <= 0) {
                try {
                    FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + "/" + BoxConst.BOX));
                } catch (IOException e) {
                    log.error("Table 'e_upms_user' no user data. Re-initialization failed ：" + e.getMessage());
                }
            }
        });
        if (InitMethodEnum.NONE != eruptProp.getInitMethodEnum()) {
            BoxModuleInvoke.invoke(module -> Optional.ofNullable(module.initMenus()).ifPresent(metaMenus ->
                    new ProjectUtil().projectStartLoaded(module.info().getName(), first -> {
                        Runnable runnable = (() -> {
                            module.initFun();
                            for (MetaMenu metaMenu : metaMenus) {
                                BoxMenu boxMenu = eruptDao.persistIfNotExist(BoxMenu.class, BoxMenu.fromMetaMenu(metaMenu), BoxMenu.CODE, metaMenu.getCode());
                                metaMenu.setId(boxMenu.getId());
                                if (null != boxMenu.getType() && null != boxMenu.getValue()) {
                                    if (MenuTypeEnum.TABLE.getCode().equals(boxMenu.getType()) || MenuTypeEnum.TREE.getCode().equals(boxMenu.getType())) {
                                        AtomicInteger i = new AtomicInteger();
                                        Optional.ofNullable(BoxCoreService.getBoxModel(boxMenu.getValue())).ifPresent(it -> {
                                            UIPermission power = it.getBox().permission();
                                            for (FunPermission value : FunPermission.values()) {
                                                if (value.verifyPower(power)) {
                                                    eruptDao.persistIfNotExist(BoxMenu.class, new BoxMenu(
                                                            UPMSUtil.getEruptFunPermissionsCode(boxMenu.getValue(), value),
                                                            value.getName(), MenuTypeEnum.BUTTON.getCode(),
                                                            UPMSUtil.getEruptFunPermissionsCode(boxMenu.getValue(), value),
                                                      boxMenu, i.addAndGet(10)
                                                    ), BoxMenu.CODE, UPMSUtil.getEruptFunPermissionsCode(boxMenu.getValue(), value));
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        switch (eruptProp.getInitMethodEnum()) {
                            case EVERY:
                                runnable.run();
                                break;
                            case FILE:
                                if (first) runnable.run();
                                break;
                        }
                    })
            ));
        }
        new ProjectUtil().projectStartLoaded("erupt-upms-user", first -> {
            if (first) {
                //用户
                if (eruptDao.queryEntityList(BoxUser.class, "isAdmin = true").size() <= 0) {
                    BoxUser boxUser = new BoxUser();
                    boxUser.setIsAdmin(true);
                    boxUser.setIsMd5(true);
                    boxUser.setStatus(true);
                    boxUser.setCreateTime(new Date());
                    boxUser.setAccount(DEFAULT_ACCOUNT);
                    boxUser.setPassword(MD5Util.digest(DEFAULT_ACCOUNT));
                    boxUser.setName(DEFAULT_ACCOUNT);
                    eruptDao.persistIfNotExist(BoxUser.class, boxUser, "account", boxUser.getAccount());
                }
            }
        });
    }

}
