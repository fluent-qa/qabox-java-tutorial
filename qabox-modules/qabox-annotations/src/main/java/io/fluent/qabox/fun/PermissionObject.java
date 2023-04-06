package io.fluent.qabox.fun;

import io.fluent.qabox.frontend.operation.UIPermission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionObject {

    private boolean add;

    private boolean delete;

    private boolean edit;

    private boolean query;

    private boolean viewDetails;

    private boolean export;

    private boolean importable;

    public PermissionObject(UIPermission permission) {
        this.add = permission.add();
        this.delete = permission.delete();
        this.edit = permission.edit();
        this.query = permission.query();
        this.viewDetails = permission.viewDetails();
        this.export = permission.export();
        this.importable = permission.importable();
    }

    public PermissionObject() {

    }
}
