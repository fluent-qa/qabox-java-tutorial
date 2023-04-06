package io.fluent.qabox.upms.model.log;

import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.UIPermission;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "upms_login_log")
@Box(
        name = "登录日志",
        permission = @UIPermission(add = false, edit = false, viewDetails = false, delete = false,
                export = true, permissionHandler = SuperAdminPermissionHandler.class),
        orderBy = "loginTime desc"
)
@EnableI18n
@Getter
@Setter
public class LoginLog extends BaseModel {

    @UIField(
            views = @View(title = "用户"),
            edit = @Edit(title = "用户", search = @Search(vague = true))
    )
    private String userName;

    @UIField(
            views = @View(title = "登录时间", sortable = true),
            edit = @Edit(title = "登录时间", search = @Search(vague = true), dateType = @DateType(type = DateType.Type.DATE_TIME))
    )
    private Date loginTime;

    @UIField(
            views = @View(title = "IP地址"),
            edit = @Edit(title = "IP地址", search = @Search)
    )
    private String ip;

    @UIField(
            views = @View(title = "IP来源", desc = "格式：国家 | 大区 | 省份 | 城市 | 运营商", template = "value&&value.replace(/\\|/g,' | ')"),
            edit = @Edit(title = "IP来源", search = @Search(vague = true))
    )
    private String region;

    @UIField(
            views = @View(title = "操作系统"),
            edit = @Edit(title = "操作系统", search = @Search)
    )
    private String systemName;

    @UIField(
            views = @View(title = "浏览器"),
            edit = @Edit(title = "浏览器", search = @Search)
    )
    private String browser;

    @UIField(
            views = @View(title = "设备类型"),
            edit = @Edit(title = "设备类型", search = @Search)
    )
    private String deviceType;

    private String token;
}
