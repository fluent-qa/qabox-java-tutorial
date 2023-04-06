package io.fluent.qabox.upms.model.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.fluent.qabox.Box;
import io.fluent.qabox.EnableI18n;
import io.fluent.qabox.UIField;
import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.frontend.field.*;
import io.fluent.qabox.frontend.field.sub_edit.CodeEditorType;
import io.fluent.qabox.frontend.field.sub_edit.DateType;
import io.fluent.qabox.frontend.field.sub_edit.Search;
import io.fluent.qabox.frontend.operation.UIPermission;
import io.fluent.qabox.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


@Entity
@Table(name = "e_upms_operate_log")
@EnableI18n
@Box(
        name = "操作日志",
        desc = "记录平台内各用户操作过程",
        permission = @UIPermission(add = false, edit = false, viewDetails = false,
                delete = false, permissionHandler = SuperAdminPermissionHandler.class),
        orderBy = "createTime desc",
        dataProxy = OperationLog.class
)
@Getter
@Setter
public class OperationLog extends BaseModel implements DataProxy<OperationLog> {

    @UIField(
            views = @View(title = "操作人"),
            edit = @Edit(title = "操作人", search = @Search(vague = true))
    )
    private String operateUser;

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
            views = @View(title = "功能名称"),
            edit = @Edit(title = "功能名称", search = @Search(vague = true))
    )
    private String apiName;

    @Column(length = 4000)
    @UIField(
            views = @View(title = "请求参数", type = ViewType.CODE),
            edit = @Edit(title = "请求参数", type = EditType.CODE_EDITOR, codeEditType = @CodeEditorType(language = "json"))
    )
    private String reqParam;

    @UIField(
            views = @View(title = "是否成功", sortable = true),
            edit = @Edit(title = "是否成功", search = @Search)
    )
    private Boolean status;

    @UIField(
            views = @View(title = "错误信息", type = ViewType.HTML)
    )
    private String errorInfo;

    @UIField(
            views = @View(title = "请求耗时", template = "value && value+'ms'", sortable = true),
            edit = @Edit(title = "请求耗时", search = @Search(vague = true))
    )
    private Long totalTime;

    @UIField(
            views = @View(title = "记录时间", sortable = true),
            edit = @Edit(title = "记录时间", search = @Search(vague = true), dateType = @DateType(type = DateType.Type.DATE_TIME))
    )
    private Date createTime;

    @Column(length = 4000)
    @UIField(
            views = @View(title = "请求地址", type = ViewType.HTML)
    )
    private String reqAddr;

    @UIField(
            views = @View(title = "请求方法")
    )
    private String reqMethod;

    @Override
    public void afterFetch(Collection<Map<String, Object>> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        for (Map<String, Object> map : list) {
            Object reqParam = map.get("reqParam");
            if (null != reqParam) {
                try {
                    map.put("reqParam", gson.toJson(gson.fromJson(reqParam.toString(), Object.class)));
                } catch (Exception ignore) {
                }
            }
        }
    }
}
