package io.fluentqa.seeds.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 */
@Data
public class CursorPageResult {
    private Instant firstId;

    private Instant lastId;

    List<PageItem> pageItems;
}
