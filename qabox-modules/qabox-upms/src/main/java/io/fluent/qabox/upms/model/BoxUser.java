package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.config.constant.RegexConst;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.BoolType;
import io.fluent.qabox.frontend.field.sub_edit.InputType;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTreeType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.LinkTree;
import io.fluent.qabox.upms.model.keeper.Keeper;
import io.fluent.qabox.upms.model.proxy.UserDataProxy;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "upms_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "account")
})
@Box(
        name = "用户配置",
        dataProxy = UserDataProxy.class,
        linkTree = @LinkTree(field = "eruptOrg")
)
@EnableI18n
@Getter
@Setter
public class BoxUser extends Keeper {

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            views = @View(title = "用户名", sortable = true),
            edit = @Edit(title = "用户名", desc = "登录用户名", notNull = true, search = @Search(vague = true))
    )
    private String account;

    @UIField(
            views = @View(title = "姓名", sortable = true),
            edit = @Edit(title = "姓名", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @UIField(
            views = @View(title = "账户状态", sortable = true),
            edit = @Edit(
                    title = "账户状态",
                    search = @Search,
                    type = EditType.BOOLEAN,
                    notNull = true,
                    boolType = @BoolType(
                            trueText = "激活",
                            falseText = "锁定"
                    )
            )
    )
    private Boolean status = true;

    @UIField(
            edit = @Edit(title = "手机号码", search = @Search(vague = true), inputType = @InputType(regex = RegexConst.PHONE_REGEX))
    )
    private String phone;

    @UIField(
            edit = @Edit(title = "邮箱", search = @Search(vague = true), inputType = @InputType(regex = RegexConst.EMAIL_REGEX))
    )
    private String email;

    @UIField(
            views = @View(title = "超管用户", sortable = true),
            edit = @Edit(
                    title = "超管用户", notNull = true, search = @Search(vague = true)
            )
    )
    private Boolean isAdmin = false;

    @ManyToOne
    @UIField(
            views = @View(title = "首页菜单", column = "name"),
            edit = @Edit(
                    title = "首页菜单",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(pid = "parentMenu.id")
            )
    )
    private BoxMenu boxMenu;

    @ManyToOne
    @UIField(
            views = @View(title = "所属组织", column = "name"),
            edit = @Edit(title = "所属组织", type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(pid = "parentOrg.id"))
    )
    private BoxOrg boxOrg;

    @ManyToOne
    @UIField(
            views = @View(title = "岗位", column = "name"),
            edit = @Edit(title = "岗位", type = EditType.REFERENCE_TREE, search = @Search)
    )
    private BoxPosition boxPosition;

    @Transient
    @UIField(
            edit = @Edit(title = "密码", type = EditType.DIVIDE)
    )
    private String pwdDivide;

    private String password;

    @Transient
    @UIField(
            edit = @Edit(title = "密码")
    )
    private String passwordA;

    @Transient
    @UIField(
            edit = @Edit(title = "确认密码")
    )
    private String passwordB;

    @UIField(
            views = @View(title = "重置密码时间", width = "100px")
    )
    private Date resetPwdTime;

    @UIField(
            edit = @Edit(
                    title = "md5加密",
                    type = EditType.BOOLEAN,
                    notNull = true,
                    boolType = @BoolType(
                            trueText = "加密",
                            falseText = "不加密"
                    )
            )
    )
    private Boolean isMd5 = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "upms_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @UIField(
            views = @View(title = "所属角色"),
            edit = @Edit(
                    title = "所属角色",
                    type = EditType.CHECKBOX
            )
    )
    private Set<BoxRole> roles;

    @Column(length = AnnotationConst.REMARK_LENGTH)
    @UIField(
            edit = @Edit(
                    title = "ip白名单",
                    desc = "ip与ip之间使用换行符间隔，不填表示不鉴权",
                    type = EditType.TEXTAREA
            )

    )
    private String whiteIp;

    @Column(length = AnnotationConst.REMARK_LENGTH)
    @UIField(
            edit = @Edit(
                    title = "备注",
                    type = EditType.TEXTAREA
            )
    )
    private String remark;

    public BoxUser() {
    }

    public BoxUser(Long id) {
        this.setId(id);
    }

}
