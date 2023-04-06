package io.fluent.qabox.upms.handler;

import io.fluent.qabox.dao.BoxDao;
import io.fluent.qabox.frontend.fun.ChoiceFetchHandler;
import io.fluent.qabox.frontend.fun.VLModel;
import io.fluent.qabox.upms.constant.FetchConst;
import io.fluent.qabox.upms.model.LookupItem;
import io.fluent.qabox.util.cache.BoxCache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.Assert.notNull;


@Component
public class DictChoiceFetchHandler implements ChoiceFetchHandler {

    @Resource
    private BoxDao eruptDao;

    private final BoxCache<List<VLModel>> dictCache = BoxCache.newInstance();

    @Override
    public List<VLModel> fetch(String[] params) {
        notNull(params, DictChoiceFetchHandler.class.getSimpleName() + " → params[0] must dict → code");
        return dictCache.getAndSet(DictChoiceFetchHandler.class.getName() + ":" + params[0],
                params.length == 2 ? Long.parseLong(params[1]) : FetchConst.DEFAULT_CACHE_TIME, () ->
                        eruptDao.queryEntityList(LookupItem.class, "eruptDict.code = :code order by sort", new HashMap<String, Object>() {{
                            this.put("code", params[0]);
                        }}).stream().map((item) -> new VLModel(item.getId(), item.getName())).collect(Collectors.toList()));
    }

}
