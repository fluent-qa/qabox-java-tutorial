package io.fluent.qabox.upms.model.proxy;

import io.fluent.qabox.data.fun.DataProxy;
import io.fluent.qabox.upms.model.BoxUserVo;
import io.fluent.qabox.upms.model.base.HyperModel;
import io.fluent.qabox.upms.service.BoxUserService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Date;


@Service
public class HyperDataProxy implements DataProxy<HyperModel> {

    @Resource
    private BoxUserService boxUserService;

    @Override
    public void beforeAdd(HyperModel hyperModel) {
        hyperModel.setCreateTime(new Date());
        hyperModel.setCreateUser(new BoxUserVo(boxUserService.getCurrentUid()));
        hyperModel.setUpdateTime(new Date());
        hyperModel.setUpdateUser(new BoxUserVo(boxUserService.getCurrentUid()));
    }

    @Override
    public void beforeUpdate(HyperModel hyperModel) {
        hyperModel.setUpdateTime(new Date());
        hyperModel.setUpdateUser(new BoxUserVo(boxUserService.getCurrentUid()));
    }
}
