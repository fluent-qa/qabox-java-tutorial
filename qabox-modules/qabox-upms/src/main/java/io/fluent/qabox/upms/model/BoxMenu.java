package io.fluent.qabox.upms.model;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.config.constant.MenuStatus;
import io.fluent.qabox.config.constant.MenuTypeEnum;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.ChoiceType;
import io.fluent.qabox.frontend.field.sub_edit.CodeEditorType;
import io.fluent.qabox.frontend.field.sub_edit.ReferenceTreeType;
import io.fluent.qabox.frontend.operation.Tree;
import io.fluent.qabox.model.AuditableModel;
import io.fluent.qabox.module.MetaMenu;
import io.fluent.qabox.upms.service.BoxMenuService;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "upms_menus", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Box(
        name = "菜单管理",
        orderBy = "EruptMenu.sort asc",
        tree = @Tree(pid = "parentMenu.id", expandLevel = 5),
        dataProxy = BoxMenuService.class
)
@EnableI18n
@Getter
@Setter
public class BoxMenu extends AuditableModel {

    public static final String CODE = "code";

    @UIField(
            views = @View(title = "名称"),
            edit = @Edit(
                    title = "名称",
                    notNull = true
            )
    )
    private String name;

    @UIField(
            edit = @Edit(
                    notNull = true,
                    title = "状态",
                    type = EditType.CHOICE,
                    choiceType = @ChoiceType(fetchHandler = MenuStatus.ChoiceFetch.class)
            )
    )
    private Integer status;

    @ManyToOne
    @UIField(
            edit = @Edit(
                    title = "上级菜单",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(pid = "parentMenu.id", expandLevel = 3)
            )
    )
    private BoxMenu parentMenu;

    @UIField(
            edit = @Edit(
                    title = "菜单类型",
                    type = EditType.CHOICE,
                    choiceType = @ChoiceType(fetchHandler = MenuTypeEnum.ChoiceFetch.class)
            )
    )
    private String type;

    @UIField(
            edit = @Edit(
                    title = "类型值"
            )
    )
    private String value;

    @UIField(
            edit = @Edit(
                    title = "顺序"
            )
    )
    private Integer sort = 0;

    @UIField(
            edit = @Edit(
                    title = "图标",
                    desc = "请参考图标库font-awesome"
            )
    )
    private String icon;

    @Column(length = AnnotationConst.CODE_LENGTH)
    @UIField(
            edit = @Edit(
                    title = "编码", readonly = @Readonly
            )
    )
    private String code;

    @Column(length = AnnotationConst.REMARK_LENGTH)
    @UIField(
            edit = @Edit(
                    title = "自定义参数",
                    desc = "json格式，通过上下文获取，根据业务需求解析",
                    type = EditType.CODE_EDITOR,
                    codeEditType = @CodeEditorType(language = "json")
            )
    )
    private String param;

    public BoxMenu() {
    }

    public BoxMenu(String code, String name, String type, String value, Integer status, Integer sort, String icon, BoxMenu parentMenu) {
        this.code = code;
        this.name = name;
        this.status = status;
        this.type = type;
        this.value = value;
        this.sort = sort;
        this.icon = icon;
        this.parentMenu = parentMenu;
        this.setCreateTime(LocalDateTime.now());
    }

    public BoxMenu(String code, String name, String type, String value, BoxMenu parentMenu, Integer sort) {
        this.code = code;
        this.name = name;
        this.parentMenu = parentMenu;
        this.type = type;
        this.value = value;
        this.sort = sort;
        this.setStatus(MenuStatus.OPEN.getValue());
        this.setCreateTime(LocalDateTime.now());
    }

    public static BoxMenu fromMetaMenu(MetaMenu metaMenu) {
        if (null == metaMenu) return null;
        BoxMenu boxMenu = new BoxMenu(metaMenu.getCode(),
                metaMenu.getName(), null == metaMenu.getType() ? null : metaMenu.getType(),
                metaMenu.getValue(), null == metaMenu.getStatus() ? null : metaMenu.getStatus().getValue(),
                metaMenu.getSort(), metaMenu.getIcon(), fromMetaMenu(metaMenu.getParentMenu()));
        boxMenu.setId(metaMenu.getId());
        return boxMenu;
    }

}
