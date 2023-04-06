package io.fluentqa.qabox.server.demo.model.complex;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.upms.model.base.HyperModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 */
@Erupt(name = "文章发布",
        power = @Power(importable = true, export = true)
)
@Entity
@Table(name = "demo_article")
@Getter
@Setter
public class Article extends HyperModel {

    @EruptField(
            views = @View(title = "封面图"),
            edit = @Edit(title = "封面图", type = EditType.ATTACHMENT,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE, maxLimit = 3))
    )
    private String pic;

    @EruptField(
            views = @View(title = "标题"),
            edit = @Edit(title = "标题", notNull = true, search = @Search(vague = true))
    )
    private String title;

//    @EruptField(
//            views = @View(title = "置顶"),
//            edit = @Edit(title = "置顶", notNull = true, search = @Search)
//    )
//    private Boolean topUp = false;

    @EruptField(
            views = @View(title = "发布状态"),
            edit = @Edit(title = "发布状态", notNull = true, boolType = @BoolType(trueText = "发布", falseText = "草稿"), search = @Search)
    )
    private Boolean publish;

//    @Lob
//    @EruptField(
//            views = @View(title = "内容(UEditor)", type = ViewType.HTML, export = false),
//            edit = @Edit(title = "内容(UEditor)", type = EditType.HTML_EDITOR, notNull = true)
//    )
//    private String content;

//    @Column(length = 5000)
//    @EruptField(
//            views = @View(title = "备注"),
//            edit = @Edit(title = "备注", type = EditType.TEXTAREA)
//    )
//    private String remark;

//
//    @Override
//    public String toString() {
//        return "Article{" +
//                "pic='" + pic + '\'' +
//                ", title='" + title + '\'' +
//                ", topUp=" + topUp +
//                ", publish=" + publish +
//                ", content='" + content + '\'' +
//                ", remark='" + remark + '\'' +
//                '}';
//    }
}
