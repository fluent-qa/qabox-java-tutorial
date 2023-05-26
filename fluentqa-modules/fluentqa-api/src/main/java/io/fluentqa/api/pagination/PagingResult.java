
package io.fluentqa.api.pagination;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果对象
 * TODO: Add different alias for handler page request
 */

@Data
public class PagingResult<T> implements Serializable {
    private static final long serialVersionUID = 4784961132604516495L;

    private long total = 0;

    private List<T> records = Collections.emptyList();

    private Long pageIndex;

    private Long pageSize;

    public PagingResult() {

    }
}
