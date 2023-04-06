package io.fluent.qabox.upms.service;

import io.fluent.qabox.config.constant.MenuStatus;
import io.fluent.qabox.config.constant.MenuTypeEnum;
import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.exception.WebApiRuntimeException;
import io.fluent.qabox.service.BoxCoreService;
import io.fluent.qabox.upms.enums.FunPermission;
import io.fluent.qabox.upms.model.BoxMenu;
import io.fluent.qabox.upms.model.BoxRole;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.util.UPMSUtil;
import io.fluent.qabox.upms.vo.EruptMenuVo;
import io.fluent.qabox.util.Boxes;
import io.fluent.qabox.util.di.IocUtil;
import io.fluent.qabox.view.BoxModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BoxMenuService implements DataProxy<BoxMenu> {

    @Resource
    private BoxDao eruptDao;

    @Resource
    private BoxContextService eruptContextService;

    public List<EruptMenuVo> geneMenuListVo(List<BoxMenu> menus) {
        List<EruptMenuVo> list = new ArrayList<>();
        menus.stream().filter(menu -> menu.getStatus() == MenuStatus.OPEN.getValue()).forEach(menu -> {
            Long pid = null == menu.getParentMenu() ? null : menu.getParentMenu().getId();
            list.add(new EruptMenuVo(menu.getId(), menu.getCode(), menu.getName(), menu.getType(), menu.getValue(), menu.getIcon(), pid));
        });
        return list;
    }

    public List<BoxMenu> getUserAllMenu(BoxUser boxUser) {
        if (null != boxUser.getIsAdmin() && boxUser.getIsAdmin()) {
            return eruptDao.queryEntityList(BoxMenu.class, "1=1 order by sort");
        } else {
            Set<BoxMenu> menuSet = new HashSet<>();
            boxUser.getRoles().stream().filter(BoxRole::getStatus).map(BoxRole::getMenus).forEach(menuSet::addAll);
            return menuSet.stream().sorted(Comparator.comparing(BoxMenu::getSort, Comparator.nullsFirst(Integer::compareTo))).collect(Collectors.toList());
        }
    }

    @Override
    public void addBehavior(BoxMenu boxMenu) {
        Integer obj = (Integer) eruptDao.getEntityManager().createQuery("select max(sort) from " + BoxMenu.class.getSimpleName()).getSingleResult();
        Optional.ofNullable(obj).ifPresent(it -> boxMenu.setSort(it + 10));
        boxMenu.setStatus(MenuStatus.OPEN.getValue());
    }

    @Override
    public void beforeAdd(BoxMenu boxMenu) {
        if (null == boxMenu.getCode()) boxMenu.setCode(Boxes.generateCode());
        if (StringUtils.isNotBlank(boxMenu.getType()) && StringUtils.isBlank(boxMenu.getValue())) {
            throw new WebApiRuntimeException("When selecting a menu type, the type value cannot be empty");
        } else if (StringUtils.isNotBlank(boxMenu.getValue()) && StringUtils.isBlank(boxMenu.getType())) {
            throw new WebApiRuntimeException("When has menu value, the menu type cannot be empty");
        }
    }

    @Override
    public void beforeUpdate(BoxMenu boxMenu) {
        this.beforeAdd(boxMenu);
    }

    /**
     * The reason for that:
     * <p>
     * The dependencies of some of the beans in the application context form a cycle:
     * mvcInterceptor
     * ↓
     * eruptSecurityInterceptor
     * ┌─────┐
     * |  eruptUserService
     * ↑     ↓
     * |  eruptMenuService
     * └─────┘
     */
    private void flushCache() {
        BoxUserService boxUserService = IocUtil.getBean(BoxUserService.class);
        boxUserService.cacheUserInfo(boxUserService.getCurrentEruptUser(), eruptContextService.getCurrentToken());
    }


    @Override
    public void afterAdd(BoxMenu boxMenu) {
        if (null != boxMenu.getValue()) {
            if (MenuTypeEnum.TABLE.getCode().equals(boxMenu.getType()) || MenuTypeEnum.TREE.getCode().equals(boxMenu.getType())) {
                int i = 0;
                Integer counter = eruptDao.getJdbcTemplate().queryForObject(
                        String.format("select count(*) from e_upms_menu where parent_menu_id = %d", boxMenu.getId()), Integer.class
                );
                if (null != counter){
                    if (counter > 0){
                        // 查询有权限菜单
                        Integer realCounter = eruptDao.getJdbcTemplate().queryForObject(
                                String.format("select count(*) from e_upms_menu where parent_menu_id = %d and value like '%s@%%'", boxMenu.getId(), boxMenu.getValue()), Integer.class
                        );
                        // 如果没有查询出权限菜单，那么本次修改Value
                        if (null != realCounter && realCounter == 0) {
                            eruptDao.getJdbcTemplate().update(String.format("delete from e_upms_menu where parent_menu_id = %d and value like '%%@%%'", boxMenu.getId()));
                            counter = 0;
                        }
                    }
                    if (counter <= 0){
                        BoxModel eruptModel = BoxCoreService.getBoxModel(boxMenu.getValue());
                        for (FunPermission value : FunPermission.values()) {
                            if (eruptModel == null || value.verifyPower(eruptModel.getBox().permission())) {
                                eruptDao.persist(new BoxMenu(
                                        Boxes.generateCode(), value.getName(), MenuTypeEnum.BUTTON.getCode(),
                                        UPMSUtil.getEruptFunPermissionsCode(boxMenu.getValue(), value), boxMenu, i += 10)
                                );
                            }
                        }
                    }
                }
            }
        }
        this.flushCache();
    }

    @Override
    public void afterUpdate(BoxMenu boxMenu) {
        this.afterAdd(boxMenu);
    }

    @Override
    public void afterDelete(BoxMenu boxMenu) {
        this.flushCache();
    }

}
