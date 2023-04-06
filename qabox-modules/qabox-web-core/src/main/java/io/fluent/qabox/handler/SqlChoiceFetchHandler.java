package io.fluent.qabox.handler;

import io.fluent.qabox.frontend.fun.ChoiceFetchHandler;
import io.fluent.qabox.frontend.fun.VLModel;
import io.fluent.qabox.util.cache.BoxCache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;


@Component
public class SqlChoiceFetchHandler implements ChoiceFetchHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final BoxCache<List<VLModel>> eruptCache = BoxCache.newInstance();

    @Override
    public List<VLModel> fetch(String[] params) {
        notNull(params, SqlChoiceFetchHandler.class.getSimpleName() + " → params not found");
        return eruptCache.getAndSet(SqlChoiceFetchHandler.class.getName() + ":" + params[0],
                params.length == 2 ? Long.parseLong(params[1]) : 3000,
                () -> jdbcTemplate.query(params[0], (rs, i) -> {
                    if (rs.getMetaData().getColumnCount() == 1) {
                        return new VLModel(rs.getString(1), rs.getString(1));
                    } else {
                        return new VLModel(rs.getString(1), rs.getString(2));
                    }
                })
        );
    }

}
