package io.fluent.qabox.upms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "box.upms", ignoreUnknownFields = false)
public class UpmsProp {

    //redis session时长
    private Integer expireTimeByLogin = 100;

    //严格的角色菜单策略，如果非管理员用户拥有角色权限则仅能编辑已有权限的菜单
    private boolean strictRoleMenuLegal = true;

}
