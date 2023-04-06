package io.fluentqa.qabox.server.demo.model.complex;

import io.fluentqa.qabox.server.demo.handler.EruptRoleDemoDataProxy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.EruptI18n;
import xyz.erupt.annotation.constant.AnnotationConst;
import xyz.erupt.annotation.sub_erupt.Filter;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.upms.handler.RoleMenuFilter;
import xyz.erupt.upms.helper.HyperModelUpdateVo;
import xyz.erupt.upms.model.EruptMenu;
import xyz.erupt.upms.model.EruptUserByRoleView;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "e_upms_role_demo", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
@Erupt(name = "角色管理", dataProxy = EruptRoleDemoDataProxy.class, orderBy = "EruptRoleDemo.sort asc")
@EruptI18n
@Getter
@Setter
@Component
public class EruptRoleDemo extends HyperModelUpdateVo {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @EruptField(
            views = @View(title = "编码"),
            edit = @Edit(title = "编码", notNull = true, search = @Search(vague = true))
    )
    private String code;

    @EruptField(
            views = @View(title = "名称"),
            edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @EruptField(
            views = @View(title = "展示顺序", sortable = true),
            edit = @Edit(title = "展示顺序", desc = "数值越小，越靠前")
    )
    private Integer sort;

    @EruptField(
            views = @View(title = "状态", sortable = true),
            edit = @Edit(
                    title = "状态",
                    type = EditType.BOOLEAN,
                    notNull = true,
                    search = @Search(vague = true),
                    boolType = @BoolType(trueText = "启用", falseText = "禁用")
            )
    )
    private Boolean status = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "e_upms_role_menu_demo",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"))
    @EruptField(
            views = @View(title = "菜单权限"),
            edit = @Edit(
                    filter = @Filter(conditionHandler = RoleMenuFilter.class),
                    title = "菜单权限",
                    type = EditType.TAB_TREE
            )
    )
    private Set<EruptMenu> menus;

    @JoinTable(name = "e_upms_user_role_demo",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @ManyToMany(fetch = FetchType.EAGER)
    @EruptField(
            views = @View(title = "包含用户"),
            edit = @Edit(
                    title = "包含用户",
                    type = EditType.TAB_TABLE_REFER
            )
    )
    private Set<EruptUserByRoleView> users;


}
