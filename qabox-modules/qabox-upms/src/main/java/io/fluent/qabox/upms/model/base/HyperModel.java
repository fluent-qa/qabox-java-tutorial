package io.fluent.qabox.upms.model.base;

import io.fluent.qabox.config.SmartSkipSerialize;
import io.fluent.qabox.data.fun.PreDataProxy;
import io.fluent.qabox.model.BaseModel;
import io.fluent.qabox.upms.model.BoxUserVo;
import io.fluent.qabox.upms.model.proxy.HyperDataProxy;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
@PreDataProxy(HyperDataProxy.class)
public class HyperModel extends BaseModel {

    @SmartSkipSerialize
    private Date createTime;

    @SmartSkipSerialize
    private Date updateTime;

    @ManyToOne
    @SmartSkipSerialize
    private BoxUserVo createUser;

    @ManyToOne
    @SmartSkipSerialize
    private BoxUserVo updateUser;
}
