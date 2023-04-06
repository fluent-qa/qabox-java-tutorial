package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.data.query.Condition;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.BoolType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.upms.handler.RoleMenuFilter;
import io.fluent.qabox.upms.model.base.HyperModelUpdateVo;
import io.fluent.qabox.upms.service.BoxUserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "upms_roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
@Box(name = "角色管理", dataProxy = BoxRole.class, orderBy = "EruptRole.sort asc")
@EnableI18n
@Getter
@Setter
@Component
public class BoxRole extends HyperModelUpdateVo implements DataProxy<BoxRole> {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            views = @View(title = "编码"),
            edit = @Edit(title = "编码", notNull = true, search = @Search(vague = true))
    )
    private String code;

    @UIField(
            views = @View(title = "名称"),
            edit = @Edit(title = "名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @UIField(
            views = @View(title = "展示顺序", sortable = true),
            edit = @Edit(title = "展示顺序", desc = "数值越小，越靠前")
    )
    private Integer sort;

    @UIField(
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
            name = "upms_role_menu",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"))
    @UIField(
            views = @View(title = "菜单权限"),
            edit = @Edit(
                    filter = @Filter(conditionHandler = RoleMenuFilter.class),
                    title = "菜单权限",
                    type = EditType.TAB_TREE
            )
    )
    private Set<BoxMenu> menus;

    @JoinTable(name = "upms_user_role",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @ManyToMany(fetch = FetchType.EAGER)
    @UIField(
            views = @View(title = "包含用户"),
            edit = @Edit(
                    title = "包含用户",
                    type = EditType.TAB_TABLE_REFER
            )
    )
    private Set<BoxUserByRoleView> users;

    @Resource
    @Transient
    private BoxUserService boxUserService;

    @Resource
    @Transient
    private BoxDao eruptDao;

    @Override
    public String beforeFetch(List<Condition> conditions) {
        if (boxUserService.getCurrentEruptUser().getIsAdmin()) return null;
        return "EruptRole.createUser.id = " + boxUserService.getCurrentUid();
    }

    @Override
    public void addBehavior(BoxRole boxRole) {
        Integer max = (Integer) eruptDao.getEntityManager().createQuery("select max(sort) from " + BoxRole.class.getSimpleName()).getSingleResult();
        if (null == max) {
            boxRole.setSort(10);
        } else {
            boxRole.setSort(max + 10);
        }
    }
}
