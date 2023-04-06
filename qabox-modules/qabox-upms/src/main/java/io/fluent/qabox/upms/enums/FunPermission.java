package io.fluent.qabox.upms.enums;

import io.fluent.qabox.frontend.operation.UIPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
@Getter
public enum FunPermission {

    ADD("新增"),
    EDIT("修改"),
    DELETE("删除"),
    EXPORT("导出"),
    IMPORTABLE("导入"),
    VIEW_DETAIL("详情");

    private final String name;

    public boolean verifyPower(UIPermission permission) {
        if (permission.add() && FunPermission.ADD == this) {
            return true;
        } else if (permission.edit() && FunPermission.EDIT == this) {
            return true;
        } else if (permission.delete() && FunPermission.DELETE == this) {
            return true;
        } else if (permission.export() && FunPermission.EXPORT == this) {
            return true;
        } else if (permission.importable() && FunPermission.IMPORTABLE == this) {
            return true;
        }
        return permission.viewDetails() && FunPermission.VIEW_DETAIL == this;
    }

}
