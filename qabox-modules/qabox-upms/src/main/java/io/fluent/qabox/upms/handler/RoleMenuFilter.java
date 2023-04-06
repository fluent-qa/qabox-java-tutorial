package io.fluent.qabox.upms.handler;

import io.fluent.qabox.frontend.fun.FilterHandler;
import io.fluent.qabox.upms.config.UpmsProp;
import io.fluent.qabox.upms.model.BoxMenu;
import io.fluent.qabox.upms.model.BoxRole;
import io.fluent.qabox.upms.model.BoxUser;
import io.fluent.qabox.upms.service.BoxUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RoleMenuFilter implements FilterHandler {

    @Resource
    private BoxUserService boxUserService;

    @Resource
    private UpmsProp eruptUpmsProp;

    @Override
    public String filter(String condition, String[] params) {
        BoxUser boxUser = boxUserService.getCurrentEruptUser();
        if (boxUser.getIsAdmin() || !eruptUpmsProp.isStrictRoleMenuLegal()) {
            return null;
        } else {
            Set<BoxMenu> menuSet = new HashSet<>();
            boxUser.getRoles().stream().filter(BoxRole::getStatus).map(BoxRole::getMenus).forEach(menuSet::addAll);
            List<String> menus = menuSet.stream().map(it -> it.getId().toString()).collect(Collectors.toList());
            return String.format("EruptMenu.id in (%s)", String.join(",", String.join(",", menus)));
        }
    }

}
